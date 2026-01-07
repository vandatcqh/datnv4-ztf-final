import http from 'k6/http';
import { check, sleep } from 'k6';
import { Rate } from 'k6/metrics';

export let errorRate = new Rate('errors');

export let options = {
    vus: 3,
    iterations: 99999,
};

const BASE_URL = 'http://10.40.30.233:8030';

// Mỗi VU giữ counter riêng
let counters = Array(options.vus).fill(1);

// padding 6 chữ số
function pad(num) {
    return String(num).padStart(6, '0');
}

// Random testcase:
function pickCase() {
    const r = Math.random();
    if (r < 0.5) return 'success';            // 50%
    if (r < 0.8) return 'duplicate';          // 30%
    if (r < 0.9) return 'validation_error';   // 10%
    return 'wrong_transaction_id';            // 10%
}

export default function registerFlow() {
    const vuIndex = __VU - 1;
    let counter = counters[vuIndex];

    if (counter > 999999) return;

    const numStr = pad(counter);
    let username = `${__VU}vandat${numStr}`;
    let email = `${__VU}vandat${numStr}@gmail.com`;
    let password = "dat123123";

    counters[vuIndex]++;

    const testcase = pickCase();

    // ---------- CASE: duplicate ----------
    if (testcase === 'duplicate') {
        const reuseNum = Math.max(1, Math.floor(Math.random() * counter));
        const reuseStr = pad(reuseNum);
        username = `${__VU}vandat${reuseStr}`;
        email = `${__VU}vandat${reuseStr}@gmail.com`;
    }

    // ---------- CASE: validation error ----------
    if (testcase === 'validation_error') {
        password = "123"; // quá ngắn, hoặc sai format
    }

    // --- send REGISTER ---
    const registerPayload = JSON.stringify({
        fullname: "Ngo Van Dat",
        username: username,
        email: email,
        password: password
    });

    const registerRes = http.post(`${BASE_URL}/auth/register`, registerPayload, {
        headers: { 'Content-Type': 'application/json' }
    });

    const isRegisterOK = registerRes.status === 200;

    // --------- SUCCESS CASE ---------
    if (testcase === 'success') {
        if (!isRegisterOK) {
            console.error(`❌ Register FAILED (success case) → ${username}: ${registerRes.status} | ${registerRes.body}`);
            errorRate.add(1);
            return;
        }

        console.log(`✅ Register success → ${username}`);

        const transactionId = registerRes.json('data.transactionId');
        if (!transactionId) {
            console.error(`❌ Missing transactionId (success case) → ${username}`);
            errorRate.add(1);
            return;
        }

        // verify đúng
        const verifyPayload = JSON.stringify({
            email: email,
            transactionId: transactionId,
            otp: "111111"
        });

        const verifyRes = http.post(`${BASE_URL}/auth/register/verify`, verifyPayload, {
            headers: { 'Content-Type': 'application/json' }
        });

        if (verifyRes.status !== 200) {
            console.error(`❌ Verify FAILED (success case) → ${username}: ${verifyRes.status} | ${verifyRes.body}`);
            errorRate.add(1);
        } else {
            console.log(`✅ Verify success → ${username}`);
        }

        sleep(0.1);
        return;
    }

    // --------- DUPLICATE / VALIDATION CASE ---------
    if (testcase === 'duplicate' || testcase === 'validation_error') {
        if (isRegisterOK) {
            console.error(`❌ Expect FAIL but got SUCCESS → ${testcase} | ${username}`);
            errorRate.add(1);
        } else {
            console.log(`⚠️ Expected fail (${testcase}) → ${username}`);
        }
        return;
    }

    // --------- WRONG TRANSACTION ID CASE (register OK nhưng verify fail) ---------
    if (testcase === 'wrong_transaction_id') {

        if (!isRegisterOK) {
            console.error(`❌ WRONG-TXID case: register unexpectedly failed → ${username}`);
            errorRate.add(1);
            return;
        }

        console.log(`🔶 Register OK (wrong txid case) → ${username}`);

        const fakeTxId = "invalid-transaction-id";

        const verifyPayload = JSON.stringify({
            email: email,
            transactionId: fakeTxId,
            otp: "111111"
        });

        const verifyRes = http.post(`${BASE_URL}/auth/register/verify`, verifyPayload, {
            headers: { 'Content-Type': 'application/json' }
        });

        if (verifyRes.status === 200) {
            console.error(`❌ WRONG TXID expected fail but got SUCCESS → ${username}`);
            errorRate.add(1);
        } else {
            console.log(`⚠️ Verify failed as expected (wrong_transaction_id) → ${username}`);
        }

        return;
    }
}
