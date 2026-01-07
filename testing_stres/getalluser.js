import http from 'k6/http';
import { check, sleep } from 'k6';

export let options = {
    stages: [
        { duration: '3m', target: 40 },  // 10 VUs
        { duration: '7m', target: 50 },  // 20 VUs
    ],
};

const BASE_URL = 'http://10.40.30.233:8030/management/allusers';

const TOKEN = 'eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiI0MDM2MCIsInVzZXJfaWQiOjQwMzYwLCJqd3RfdmVyc2lvbiI6MCwiaWF0IjoxNzYzODEzOTYzLCJleHAiOjE3NjM4MjM5NjN9.5dGnJMQZpooFzIr175t0zBW-Q4UrGfIphf6Hcqfulkg';

export default function () {
    const res = http.get(BASE_URL, {
        headers: {
            'Authorization': `Bearer ${TOKEN}`,
        },
    });

    check(res, {
        'status 200': (r) => r.status === 200,
    });

    sleep(0.01);
}
