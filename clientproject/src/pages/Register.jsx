import { useState, useEffect } from "react";
import { useNavigate, Link } from "react-router-dom";
import axios from "axios";

const Register = () => {
  const [formData, setFormData] = useState({
    fullname: "",
    username: "",
    email: "",
    password: "",
    confirmPassword: "",
  });
  const [otp, setOtp] = useState("");
  const [transactionId, setTransactionId] = useState("");
  const [step, setStep] = useState(1);
  const [message, setMessage] = useState("");
  const [error, setError] = useState("");
  const [resendTimer, setResendTimer] = useState(0);
  const navigate = useNavigate();

  useEffect(() => {
    const token = localStorage.getItem("token");
    if (token) navigate("/home");
  }, [navigate]);

  useEffect(() => {
    if (resendTimer > 0) {
      const timer = setTimeout(() => setResendTimer(resendTimer - 1), 1000);
      return () => clearTimeout(timer);
    }
  }, [resendTimer]);

  const handleChange = (e) =>
    setFormData({ ...formData, [e.target.name]: e.target.value });

  const handleRegister = async (e) => {
    e.preventDefault();
    setError("");
    setMessage("");

    if (formData.password !== formData.confirmPassword) {
      setError("Mật khẩu không trùng khớp");
      return;
    }

    try {
      const res = await axios.post("http://localhost:8030/auth/register", {
        fullname: formData.fullname,
        username: formData.username,
        email: formData.email,
        password: formData.password,
      });

      const { data, message } = res.data;
      setTransactionId(data.transactionId);
      setMessage(message || "OTP đã được gửi đến email của bạn");
      setStep(2);
      setResendTimer(60); // countdown 60s cho resend OTP
    } catch (err) {
      setError(err.response?.data?.message || "Đăng ký thất bại.");
    }
  };

  const handleVerify = async (e) => {
    e.preventDefault();
    setError("");
    setMessage("");

    try {
      const res = await axios.post("http://localhost:8030/auth/register/verify", {
        email: formData.email,
        transactionId,
        otp,
      });

      const { data, message } = res.data;
      setMessage(message || "Đăng ký thành công!");
      localStorage.setItem("fullname", data.fullname);

      setTimeout(() => navigate("/login"), 1200);
    } catch (err) {
      setError(err.response?.data?.message || "Xác thực OTP thất bại.");
    }
  };

  const handleResendOTP = async () => {
    setError("");
    setMessage("");

    try {
      const res = await axios.post("http://localhost:8030/auth/register/resend-otp", {
        email: formData.email,
        transactionId,
      });

      const { data, message } = res.data;
      setTransactionId(data.transactionId);
      setMessage(message || "OTP mới đã được gửi đến email của bạn");
      setResendTimer(60);
    } catch (err) {
      setError(err.response?.data?.message || "Không thể gửi lại OTP.");
    }
  };

  return (
    <div className="min-h-screen flex items-center justify-center bg-gray-100 px-4">
      <div className="w-full max-w-md bg-white rounded-2xl shadow-xl p-8">
        <h2 className="text-2xl font-bold text-center mb-6 text-gray-800">
          {step === 1 ? "Đăng ký" : "Xác thực OTP"}
        </h2>

        {message && <p className="text-green-600 text-center mb-4">{message}</p>}
        {error && <p className="text-red-600 text-center mb-4">{error}</p>}

        {step === 1 ? (
          <form onSubmit={handleRegister} className="space-y-4">
            <input
              type="text"
              name="fullname"
              placeholder="Họ và tên"
              value={formData.fullname}
              onChange={handleChange}
              required
              className="w-full px-3 py-2 border rounded-lg"
            />
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
              type="email"
              name="email"
              placeholder="Email"
              value={formData.email}
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
            <input
              type="password"
              name="confirmPassword"
              placeholder="Xác nhận mật khẩu"
              value={formData.confirmPassword}
              onChange={handleChange}
              required
              className="w-full px-3 py-2 border rounded-lg"
            />
            <button className="w-full bg-blue-500 text-white py-2 rounded-lg hover:bg-blue-600 transition">
              Gửi OTP
            </button>
          </form>
        ) : (
          <form onSubmit={handleVerify} className="space-y-4">
            <input
              type="text"
              placeholder="Nhập mã OTP"
              value={otp}
              onChange={(e) => setOtp(e.target.value)}
              required
              className="w-full px-3 py-2 border rounded-lg"
            />
            <button className="w-full bg-green-500 text-white py-2 rounded-lg hover:bg-green-600 transition">
              Xác thực OTP
            </button>

            <div className="text-center mt-4">
              {resendTimer > 0 ? (
                <p className="text-gray-500 text-sm">
                  Gửi lại OTP sau {resendTimer}s
                </p>
              ) : (
                <button
                  type="button"
                  onClick={handleResendOTP}
                  className="text-blue-500 hover:underline text-sm"
                >
                  Gửi lại OTP
                </button>
              )}
            </div>
          </form>
        )}

        {step === 1 && (
          <p className="mt-4 text-center text-gray-600">
            Đã có tài khoản?{" "}
            <Link to="/login" className="text-blue-500 hover:underline">
              Đăng nhập
            </Link>
          </p>
        )}
      </div>
    </div>
  );
};

export default Register;
