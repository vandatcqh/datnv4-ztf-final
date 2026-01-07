import http from "k6/http";
import { check, sleep } from "k6";

export let options = {
    stages: [
        { duration: '1m', target: 10 },  // ramp-up lên 10 VU
        { duration: '1m', target: 20 },  // tăng lên 20 VU
        { duration: '1m', target: 30 },  // tăng lên 30 VU
        { duration: '1m', target: 40 },  // tăng lên 40 VU
        { duration: '1m', target: 50 },  // tăng lên 50 VU
        { duration: '1m', target: 60 }, // giữ 50 VU
        { duration: '1m', target: 0 },   // ramp-down về 0 VU
    ],
};


const BASE_URL = "http://10.40.30.233:8030";

// Pool định nghĩa start + count
const POOLS = [
    { start: 1,      count: 3000   },      // vandat000001 → 0003000
    { start: 100001, count: 10000  },      // vandat100001 → 110000
    { start: 200001, count: 10000  },      // vandat200001 → 210000
    { start: 300001, count: 10000  },      // vandat300001 → 310000
];

// padding số 6 chữ
function pad(num) {
    return String(num).padStart(6, "0");
}

// random int
function rnd(min, max) {
    return Math.floor(Math.random() * (max - min + 1)) + min;
}

// chọn random 1 username hợp lệ
function randomUsername() {
    const pool = POOLS[rnd(0, POOLS.length - 1)];

    const index = pool.start + rnd(0, pool.count - 1);
    return "vandat" + pad(index);
}

export default function () {
    const username = randomUsername();

    const payload = JSON.stringify({
        username: username,
        password: "abc123123"
    });

    const res = http.post(`${BASE_URL}/auth/login`, payload, {
        headers: { "Content-Type": "application/json" },
        timeout: "3s"
    });

    // không log, không retry — chỉ check
    check(res, {
        "login ok": (r) => r.status === 200
    });

    sleep(0.05);  // tùy chỉnh load
}
