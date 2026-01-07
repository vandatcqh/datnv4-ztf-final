import grpc from 'k6/net/grpc';
import { check, sleep } from 'k6';

const client = new grpc.Client();
client.load(['.'], 'token.proto'); // đường dẫn tới token.proto

export let options = {
    stages: [
        { duration: '1m', target: 200 },   // ramp-up 0 -> 10 VU
        { duration: '1m', target: 250 },    // peak load 50 VU
        { duration: '1m', target: 300 },    // peak load 50 VU
        { duration: '1m', target: 400 },
        { duration: '1m', target: 0 },    // ramp-down 50 -> 0 VU
    ],
};

const VALID_TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxIiwidXNlcl9pZCI6MSwiand0X3ZlcnNpb24iOjAsImlhdCI6MTc2NDAyMjg4MSwiZXhwIjoxNzY0MDMyODgxfQ.zCPO00daHTHTylIFZNfO-o4Eq6LkrcKOhqVbwg_tMD4";
const USER_ID = 1053943;

export default function () {
    client.connect('10.40.30.233:9052', { plaintext: true });

    // --- ValidateAccessToken ---
    const valRes = client.invoke('token.service.AccessTokenService/ValidateAccessToken', {
        accessToken: VALID_TOKEN,
        // user_id không cần gửi nếu server chỉ check token
    });

    // if (valRes.status !== grpc.StatusOK) {
    //     console.log(`ValidateAccessToken failed for token: ${VALID_TOKEN}`, JSON.stringify(valRes.error));
    // } else {
    //     console.log(`ValidateAccessToken OK for token: ${VALID_TOKEN}, valid=${valRes.message.valid}`);
    // }

    check(valRes, {
        'ValidateAccessToken status OK': (r) => r.status === grpc.StatusOK,
        'ValidateAccessToken returns valid': (r) => r.status === grpc.StatusOK && r.message.valid === true,
        'ValidateAccessToken matches user_id': (r) => r.status === grpc.StatusOK && r.message.user_id === USER_ID,
    });

    client.close();
    sleep(0.05); // 50ms delay giữa iterations
}
