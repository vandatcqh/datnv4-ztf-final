import http from 'k6/http';
import { check, sleep } from 'k6';
import { Rate } from 'k6/metrics';

export let errorRate = new Rate('errors');

// Tăng VU theo từng giai đoạn
export let options = {
    stages: [
        { duration: '2m', target: 5 },   // 0 → 5 VU
        { duration: '2m', target: 15 },  // 5 → 10 VU
        { duration: '5m', target: 15 },  // 10 → 15 VU
        { duration: '5m', target: 20 },  // 15 → 20 VU
    ],
};

const BASE_URL = 'http://10.40.30.233:8030';

// Vì VU dynamic theo stages, không thể tạo mảng trước theo số VU cố định
let counters = {};

function pad(num) {
    return String(num).padStart(6, '0');
}

export default function registerFlow() {
    const vuId = __VU;

    // Khởi tạo counter cho VU nếu chưa có
    if (!counters[vuId]) counters[vuId] = 1;

    let counter = counters[vuId];
    if (counter > 999999) return;

    const numStr = pad(counter);
    counters[vuId]++;

    const username = `${vuId}vandat${numStr}`;
    const email = `${vuId}vandat${numStr}@gmail.com`;

    // 1️⃣ Register
    let registerPayload = JSON.stringify({
        fullname: "Ngo Van Dat",
        username: username,
        email: email,
        password: "dat123123"
    });

    let registerRes = http.post(`${BASE_URL}/auth/register`, registerPayload, {
        headers: { 'Content-Type': 'application/json' }
    });

    if (registerRes.status !== 200) {
        console.error(`Register failed for ${username}: ${registerRes.status} - ${registerRes.body}`);
        errorRate.add(1);
        return;
    }

    check(registerRes, { 'register status 200': (r) => r.status === 200 }) || errorRate.add(1);

    const transactionId = registerRes.json('data.transactionId');
    if (!transactionId) {
        console.error(`Transaction ID is null for ${username}`);
        errorRate.add(1);
        return;
    }

    // 2️⃣ Verify OTP
    let verifyPayload = JSON.stringify({
        email: email,
        transactionId: transactionId,
        otp: "111111"
    });

    let verifyRes = http.post(`${BASE_URL}/auth/register/verify`, verifyPayload, {
        headers: { 'Content-Type': 'application/json' }
    });

    if (verifyRes.status !== 200) {
        console.error(`Verify failed for ${username}: ${verifyRes.status} - ${verifyRes.body}`);
        errorRate.add(1);
        return;
    }

    check(verifyRes, { 'verify status 200': (r) => r.status === 200 }) || errorRate.add(1);

    sleep(0.1);
}
