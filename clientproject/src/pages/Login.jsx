import { useState, useEffect } from "react";
import { useNavigate, Link } from "react-router-dom";
import axios from "axios";

const Login = () => {
  const [formData, setFormData] = useState({ username: "", password: "" });
  const [otp, setOtp] = useState(""); // OTP input
  const [transaction2faId, setTransaction2faId] = useState(null); // tạm lưu transaction2fa_id
  const [error, setError] = useState("");
  const [message, setMessage] = useState("");
  const [loading, setLoading] = useState(false);
  const navigate = useNavigate();

  useEffect(() => {
    const token = localStorage.getItem("token");
    if (token) navigate("/home");
  }, [navigate]);

  const handleChange = (e) => {
    setFormData({ ...formData, [e.target.name]: e.target.value });
  };

  const handleOtpChange = (e) => setOtp(e.target.value);

  // Login bước 1: username/password
  const handleSubmit = async (e) => {
    e.preventDefault();
    setError("");
    setMessage("");
    setLoading(true);

    try {
      const res = await axios.post(
        "http://localhost:8030/auth/login",
        formData,
        { withCredentials: true }
      );
      const { data, message: msg } = res.data;

      if (data?.transaction2fa_id) {
        // User bật 2FA → yêu cầu nhập OTP
        setTransaction2faId(data.transaction2fa_id);
        setMessage("Vui lòng nhập mã OTP từ ứng dụng 2FA");
      } else {
        // Không bật 2FA → login bình thường
        localStorage.setItem("token", data.accessToken);
        localStorage.setItem("userId", data.userId);
        localStorage.setItem("username", data.username);
        localStorage.setItem("user", JSON.stringify(data));
        setMessage(msg || "Đăng nhập thành công!");
        setTimeout(() => navigate("/home"), 800);
      }
    } catch (err) {
      setError(err.response?.data?.message || "Đăng nhập thất bại");
    } finally {
      setLoading(false);
    }
  };

  // Login bước 2: verify 2FA OTP
  const handleVerify2FA = async (e) => {
    e.preventDefault();
    setError("");
    setMessage("");
    setLoading(true);

    try {
      const res = await axios.post(
        "http://localhost:8030/auth/login_verify_2fa",
        {
          transaction2faId,
          otp: parseInt(otp, 10)
        },
        { withCredentials: true }
      );

      const { data, message: msg } = res.data;

      localStorage.setItem("token", data.accessToken);
      localStorage.setItem("userId", data.userId);
      localStorage.setItem("username", data.username);
      localStorage.setItem("user", JSON.stringify(data));

      setMessage(msg || "Đăng nhập thành công!");
      setTransaction2faId(null);
      setOtp("");
      setTimeout(() => navigate("/home"), 800);
    } catch (err) {
      setError(err.response?.data?.message || "OTP không hợp lệ");
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="min-h-screen flex items-center justify-center bg-gray-100 px-4">
      <div className="w-full max-w-md bg-white rounded-2xl shadow-xl p-8">
        <h2 className="text-2xl font-bold text-center mb-6 text-gray-800">Đăng nhập</h2>

        {error && <p className="text-red-600 text-center mb-4">{error}</p>}
        {message && <p className="text-green-600 text-center mb-4">{message}</p>}

        {!transaction2faId ? (
          <form onSubmit={handleSubmit} className="space-y-4">
            <input
              type="text"
              name="username"
              placeholder="Tên đăng nhập"
              value={formData.username}
              onChange={handleChange}
              required
              className="w-full px-3 py-2 border rounded-lg"
            />
            <input
              type="password"
              name="password"
              placeholder="Mật khẩu"
              value={formData.password}
              onChange={handleChange}
              required
              className="w-full px-3 py-2 border rounded-lg"
            />
            <button
              disabled={loading}
              className="w-full bg-blue-500 text-white py-2 rounded-lg hover:bg-blue-600 transition"
            >
              {loading ? "Đang xử lý..." : "Đăng nhập"}
            </button>
          </form>
        ) : (
          <form onSubmit={handleVerify2FA} className="space-y-4">
            <input
              type="text"
              name="otp"
              placeholder="Nhập mã OTP"
              value={otp}
              onChange={handleOtpChange}
              required
              className="w-full px-3 py-2 border rounded-lg"
            />
            <button
              disabled={loading}
              className="w-full bg-green-500 text-white py-2 rounded-lg hover:bg-green-600 transition"
            >
              {loading ? "Đang xác thực..." : "Xác thực OTP"}
            </button>
          </form>
        )}

        <p className="mt-4 text-center text-gray-600">
          Chưa có tài khoản?{" "}
          <Link to="/register" className="text-blue-500 hover:underline">
            Đăng ký
          </Link>
        </p>
        <p className="mt-2 text-center text-gray-600">
          <Link to="/forgot-password" className="text-blue-500 hover:underline">
            Quên mật khẩu?
          </Link>
        </p>
      </div>
    </div>
  );
};

export default Login;
