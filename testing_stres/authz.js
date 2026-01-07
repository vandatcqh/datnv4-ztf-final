import grpc from 'k6/net/grpc';
import { check, sleep } from 'k6';

const client = new grpc.Client();
client.load(['.'], 'authz.proto'); // đường dẫn tới authz.proto

export let options = {
    stages: [
        // { duration: '1m', target: 50 },
        // { duration: '2m', target: 100 },
        // { duration: '1m', target: 150 },
        // { duration: '1m', target: 200 },
        // { duration: '1m', target: 250 },
        // { duration: '1m', target: 300 },
        // { duration: '1m', target: 400 },
        // { duration: '1m', target: 0 },
        { duration: '1m', target: 200 },
        { duration: '2m', target: 300 },
        { duration: '1m', target: 400 },
        { duration: '1m', target: 500 },
        { duration: '1m', target: 600 },
        { duration: '1m', target: 700 },
        { duration: '1m', target: 800 },
        { duration: '1m', target: 0 },
    ],
};

const USER_ID = 1;

export default function () {
    client.connect('10.40.30.233:9053', { plaintext: true });

    // --- CheckPermission ---
    const res = client.invoke('authz.AuthzService/CheckPermission', {
        userId: USER_ID,
        path: "/management/delete_user",
        method: "DELETE",
    });

    check(res, {
        'CheckPermission status OK': (r) => r.status === grpc.StatusOK,
        'CheckPermission returns allowed': (r) => r.status === grpc.StatusOK && typeof r.message.allowed !== "undefined",
        'CheckPermission message exists': (r) => r.status === grpc.StatusOK && typeof r.message.message === "string",
    });

    client.close();
    sleep(0.05); // 50ms delay
}
