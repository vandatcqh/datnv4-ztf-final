import http from 'k6/http';
import { check, sleep } from 'k6';
import { Rate } from 'k6/metrics';

export let errorRate = new Rate('errors');

export let options = {
    vus: 3,            // tăng lên 3 VU
    duration: '50m',   // chạy liên tục
};

const BASE_URL = 'http://10.40.30.233:8030';
const TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxIiwidXNlcl9pZCI6MSwiand0X3ZlcnNpb24iOjAsImlhdCI6MTc2NDAyMjg4MSwiZXhwIjoxNzY0MDMyODgxfQ.zCPO00daHTHTylIFZNfO-o4Eq6LkrcKOhqVbwg_tMD4"; 

// hàm tiện ích padding số
function pad(num) {
    return String(num).padStart(6, '0');
}

// gán range cho từng VU
function getStartCounter(vu) {
    switch (vu) {
        case 1: return 100001;
        case 2: return 200001;
        case 3: return 300001;
        default: return 1;
    }
}

// mỗi VU có counter riêng
let vuCounter = {};

export default function () {
    const vu = __VU; // VU hiện tại
    if (!vuCounter[vu]) {
        vuCounter[vu] = getStartCounter(vu);
    }

    const counter = vuCounter[vu];
    if (counter > getStartCounter(vu) + 99998) { // mỗi VU tạo 99,999 user
        console.log(`VU ${vu} DONE`);
        return;
    }

    const suffix = pad(counter);
    const username = `vandat${suffix}`;
    const email    = `vandat${suffix}@gmail.com`;

    let payload = JSON.stringify({
        fullname: "Load Test User",
        username: username,
        email: email,
        password: "abc123123"
    });

    let res = http.post(`${BASE_URL}/management/create_user`, payload, {
        headers: {
            'Authorization': `Bearer ${TOKEN}`,
            'Content-Type': 'application/json'
        }
    });

    if (res.status !== 200) {
        console.error(`FAILED ${username} => ${res.status} - ${res.body}`);
        errorRate.add(1);
    }

    check(res, {
        'status 200': (r) => r.status === 200
    }) || errorRate.add(1);

    vuCounter[vu]++; // tăng counter riêng của VU
    sleep(0.01);
}
