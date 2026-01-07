import http from 'k6/http';
import { check, sleep } from 'k6';
import { Rate } from 'k6/metrics';

export let errorRate = new Rate('errors');

export let options = {
    vus: 3,
    iterations: 15000, // tổng login
};

const BASE_URL = 'http://10.40.30.233:8030';

// Mỗi VU giữ counter riêng
let counters = Array(options.vus).fill(1);

// Spam user cố định
const SPAM_USERNAME = '1vandat000001';
const SPAM_PASSWORD = 'dat123123';
let spamCount = 0;
const SPAM_TIMES = 50; // số lần spam

// padding số thành 6 chữ số
function pad(num) {
    return String(num).padStart(6, '0');
}

// random chọn testcase
function pickCase() {
    const rnd = Math.random();
    if (rnd < 0.33) return 'success';
    if (rnd < 0.66) return 'wrong_username';
    return 'wrong_password';
}

export default function loginFlow() {
    // Nếu còn spam chưa xong, spam user này
    if (spamCount < SPAM_TIMES) {
        spamCount++;
        const payload = JSON.stringify({
            username: SPAM_USERNAME,
            password: SPAM_PASSWORD
        });

        const res = http.post(`${BASE_URL}/auth/login`, payload, {
            headers: { 'Content-Type': 'application/json' }
        });

        const ok = check(res, {
            'spam login success': (r) => r.status === 200 && r.json('data.accessToken') !== undefined
        });
        if (!ok) {
            console.error(`Spam login failed for ${SPAM_USERNAME}: ${res.status} | ${res.body}`);
            errorRate.add(1);
        }

        sleep(0.05);
        return; // kết thúc VU vòng này, không chạy user khác
    }

    // --- Phần login bình thường ---
    const vuIndex = __VU - 1;
    let counter = counters[vuIndex];

    if (counter > 500) return;

    const numStr = pad(counter);
    const username = `${__VU}vandat${numStr}`;
    counters[vuIndex]++;

    const testcase = pickCase();

    let payload;
    if (testcase === 'success') {
        payload = JSON.stringify({
            username: username,
            password: "dat123123"
        });
    } else if (testcase === 'wrong_username') {
        payload = JSON.stringify({
            username: `wrong${username}`,
            password: "dat123123"
        });
    } else { // wrong_password
        payload = JSON.stringify({
            username: username,
            password: "wrongpassword"
        });
    }

    const res = http.post(`${BASE_URL}/auth/login`, payload, {
        headers: { 'Content-Type': 'application/json' }
    });

    if (testcase === 'success') {
        const ok = check(res, {
            'login success status 200': (r) => r.status === 200,
            'has accessToken': (r) => r.json('data.accessToken') !== undefined
        });
        if (!ok) {
            console.error(`Login success failed for ${username}: ${res.status} | ${res.body}`);
            errorRate.add(1);
        }
    } else {
        const ok = check(res, {
            'login fail status 400 or 401': (r) => r.status === 400 || r.status === 401
        });
        if (!ok) {
            console.error(`Login fail testcase "${testcase}" failed for ${username}: ${res.status} | ${res.body}`);
            errorRate.add(1);
        }
    }

    sleep(0.05);
}
