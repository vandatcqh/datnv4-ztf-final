import http from 'k6/http';
import { check } from 'k6';
import { Rate } from 'k6/metrics';

export let errorRate = new Rate('errors');

// Số lượng request: thay đổi 100 / 1000 / 10000
export let options = {
    vus: 100,          // số lượng VU chạy đồng thời
    iterations: 100000,  // tổng số request muốn gửi
};

const BASE_URL = 'http://10.40.30.233:8030';
const TOKEN = 'eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiI0MDM2MCIsInVzZXJfaWQiOjQwMzYwLCJqd3RfdmVyc2lvbiI6MCwiaWF0IjoxNzYzNzg2NTQ0LCJleHAiOjE3NjM3OTY1NDR9.DXoEjprQQrEC8ZqU6-stHwycAVEI6PNbLiL38ccYYwI';

export default function () {
    const url = `${BASE_URL}/user/profile`;

    const params = {
        headers: {
            'Authorization': `Bearer ${TOKEN}`,
            'Content-Type': 'application/json',
        },
    };

    let res = http.get(url, params);

    check(res, {
        'status is 200': (r) => r.status === 200,
    }) || errorRate.add(1);
}
