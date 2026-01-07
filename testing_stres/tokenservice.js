import grpc from 'k6/net/grpc';
import { check, sleep } from 'k6';

const client = new grpc.Client();
client.load(['.'], 'token.proto'); // đường dẫn tới token.proto

export let options = {
    stages: [
        { duration: '30s', target: 10 },  // ramp-up
        { duration: '1m', target: 50 },   // load test
        { duration: '30s', target: 0 },   // ramp-down
    ],
};

// Hàm tạo user_id trong range 1040000 -> 1053943
function randomUserId() {
    return Math.floor(Math.random() * (1053943 - 1040000 + 1)) + 1040000;
}

export default function () {
    client.connect('10.40.30.233:9052', { plaintext: true });

    // --- AccessTokenService: GenerateAccessToken ---
    const genUserId = randomUserId();
    const genRes = client.invoke('token.service.AccessTokenService/GenerateAccessToken', {
        user_id: genUserId,
    });

    if (genRes.status !== grpc.StatusOK) {
        console.log(`GenerateAccessToken failed for user_id=${genUserId}:`, JSON.stringify(genRes.error));
    }

    const accessToken = (genRes.message && genRes.message.accessToken) || '';

    check(genRes, {
        'GenerateAccessToken status OK': (r) => r.status === grpc.StatusOK,
        'GenerateAccessToken has token': (r) => r.status === grpc.StatusOK && accessToken.length > 0,
    });

    // --- AccessTokenService: ValidateAccessToken ---
    if (accessToken) {
        const valRes = client.invoke('token.service.AccessTokenService/ValidateAccessToken', {
            accessToken: accessToken,
        });

        if (valRes.status !== grpc.StatusOK) {
            console.log(`ValidateAccessToken failed for token=${accessToken}:`, JSON.stringify(valRes.error));
        }

        check(valRes, {
            'ValidateAccessToken status OK': (r) => r.status === grpc.StatusOK,
        });
    }

    // --- RefreshTokenService: CreateRefreshToken ---
    const createUserId = randomUserId();
    const createRes = client.invoke('token.service.RefreshTokenService/CreateRefreshToken', {
        user_id: createUserId,
    });

    if (createRes.status !== grpc.StatusOK) {
        console.log(`CreateRefreshToken failed for user_id=${createUserId}:`, JSON.stringify(createRes.error));
    }

    const refreshToken = (createRes.message && createRes.message.refreshToken) || '';

    check(createRes, {
        'CreateRefreshToken OK': (r) => r.status === grpc.StatusOK && refreshToken.length > 0,
    });

    // --- RefreshTokenService: RotateRefreshToken ---
    if (refreshToken) {
        const rotateRes = client.invoke('token.service.RefreshTokenService/RotateRefreshToken', {
            old_token: refreshToken,
            user_id: createUserId,
        });

        if (rotateRes.status !== grpc.StatusOK) {
            console.log(`RotateRefreshToken failed for token=${refreshToken}:`, JSON.stringify(rotateRes.error));
        }

        const newToken = (rotateRes.message && rotateRes.message.new_token) || '';

        check(rotateRes, {
            'RotateRefreshToken OK': (r) => r.status === grpc.StatusOK && newToken.length > 0,
        });

        // --- RefreshTokenService: IsValid ---
        if (newToken) {
            const validRes = client.invoke('token.service.RefreshTokenService/IsValid', {
                token: newToken,
            });

            if (validRes.status !== grpc.StatusOK) {
                console.log(`IsValid failed for token=${newToken}:`, JSON.stringify(validRes.error));
            }

            check(validRes, {
                'IsValid OK': (r) => r.status === grpc.StatusOK,
            });
        }
    }

    client.close();
    sleep(0.1); // delay 100ms giữa các iterations
}
