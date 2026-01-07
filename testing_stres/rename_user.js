import http from 'k6/http';
import { sleep } from 'k6';

export let options = {
    vus: 1,               // chạy tuần tự
    iterations: 1000,     // 1000 user cần update
    rps: 2,               // khoảng 100 req/phút
};

const BASE_URL = "http://10.40.30.233:81";
const TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxIiwidXNlcl9pZCI6MSwiand0X3ZlcnNpb24iOjAsImlhdCI6MTc2MzM3MzE4MywiZXhwIjoxNzYzMzgzMTgzfQ.4mJ8F1hrkyygGmoeinC39XHY7WEGBpDakJcJIlS3HTI";

export default function () {
    let userId = __ITER + 2; // bắt đầu từ 2 vì 1 là admin

    let payload = JSON.stringify({
        userId: userId,
        fullname: `Alice ${userId - 1} bi doi ten`
    });

    let res = http.put(`${BASE_URL}/management/update_fullname`, payload, {
        headers: {
            'Content-Type': 'application/json',
            'Authorization': `Bearer ${TOKEN}`
        },
    });

    console.log(`UserId ${userId} → status: ${res.status}`);

    // throttle ~100 req/phút
    sleep(0.6);
}
