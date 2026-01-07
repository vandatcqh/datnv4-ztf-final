import { useState } from "react";
import { useNavigate, Link } from "react-router-dom";
import axios from "axios";
import { useEffect } from "react";

const ForgotPassword = () => {
  const [email, setEmail] = useState("");
  const [otp, setOtp] = useState("");
  const [newPassword, setNewPassword] = useState("");
  const [confirmPassword, setConfirmPassword] = useState("");
  const [transactionId, setTransactionId] = useState("");
  const [step, setStep] = useState(1); // 1: Nhập email, 2: Nhập OTP & mật khẩu mới
  const [message, setMessage] = useState("");
  const [error, setError] = useState("");
  const navigate = useNavigate();

    useEffect(() => {
      const token = localStorage.getItem("token");
      if (token) {
        navigate("/home"); // có token thì đá thẳng sang home
      }
    }, [navigate]);

  const handleSendOtp = async (e) => {
    e.preventDefault();
    setError("");
    setMessage("");

    try {
      const res = await axios.post("http://localhost:8030/auth/forgot-password", {
        email: email,
      });

      setTransactionId(res.data.data.transactionId);
      setMessage(res.data.message || "OTP đã được gửi đến email của bạn");
      setStep(2);
    } catch (err) {
      setError(err.response?.data?.message || "Gửi OTP thất bại. Vui lòng thử lại.");
    }
  };

  const handleResetPassword = async (e) => {
    e.preventDefault();
    setError("");
    setMessage("");

    if (newPassword !== confirmPassword) {
      setError("Mật khẩu mới không trùng khớp");
      return;
    }

    if (newPassword.length < 6) {
      setError("Mật khẩu phải có ít nhất 6 ký tự");
      return;
    }

    try {
      const res = await axios.post("http://localhost:8030/auth/forgot-password/verify", {
        transactionId,
        otp,
        newPassword,
      });

      setMessage(res.data.message || "Đặt lại mật khẩu thành công!");

      setTimeout(() => navigate("/login"), 1500);
    } catch (err) {
      setError(err.response?.data?.message || "Xác thực OTP thất bại.");
    }
  };

  const handleBackToEmail = () => {
    setStep(1);
    setError("");
    setMessage("");
    setOtp("");
    setNewPassword("");
    setConfirmPassword("");
  };

  return (
    <div className="min-h-screen flex items-center justify-center bg-gray-100 px-4">
      <div className="w-full max-w-md bg-white rounded-2xl shadow-xl p-8">
        <h2 className="text-2xl font-bold text-center mb-6 text-gray-800">
          {step === 1 ? "Quên mật khẩu" : "Đặt lại mật khẩu"}
        </h2>

        {message && <p className="text-green-600 text-center mb-4">{message}</p>}
        {error && <p className="text-red-600 text-center mb-4">{error}</p>}

        {step === 1 ? (
          <form onSubmit={handleSendOtp} className="space-y-4">
            <div className="text-sm text-gray-600 mb-4 text-center">
              Nhập email của bạn để nhận mã OTP đặt lại mật khẩu
            </div>
            
            <input
              type="email"
              name="email"
              placeholder="Email đăng ký"
              value={email}
              onChange={(e) => setEmail(e.target.value)}
              required
              className="w-full px-3 py-2 border rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
            />
            
            <button 
              type="submit"
              className="w-full bg-blue-500 text-white py-2 rounded-lg hover:bg-blue-600 transition font-medium"
            >
              Gửi mã OTP
            </button>
          </form>
        ) : (
          <form onSubmit={handleResetPassword} className="space-y-4">
            <div className="text-sm text-gray-600 mb-4 text-center">
              Nhập mã OTP và mật khẩu mới của bạn
            </div>
            
            <input
              type="text"
              placeholder="Mã OTP"
              value={otp}
              onChange={(e) => setOtp(e.target.value)}
              required
              className="w-full px-3 py-2 border rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
            />
            
            <input
              type="password"
              placeholder="Mật khẩu mới"
              value={newPassword}
              onChange={(e) => setNewPassword(e.target.value)}
              required
              className="w-full px-3 py-2 border rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
            />
            
            <input
              type="password"
              placeholder="Xác nhận mật khẩu mới"
              value={confirmPassword}
              onChange={(e) => setConfirmPassword(e.target.value)}
              required
              className="w-full px-3 py-2 border rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
            />
            
            <div className="flex gap-2">
              <button
                type="button"
                onClick={handleBackToEmail}
                className="flex-1 bg-gray-500 text-white py-2 rounded-lg hover:bg-gray-600 transition font-medium"
              >
                Quay lại
              </button>
              
              <button 
                type="submit"
                className="flex-1 bg-green-500 text-white py-2 rounded-lg hover:bg-green-600 transition font-medium"
              >
                Đặt lại mật khẩu
              </button>
            </div>
          </form>
        )}

        <div className="mt-6 text-center space-y-2">
          <p className="text-gray-600">
            {step === 1 ? "Nhớ mật khẩu? " : "Quay lại "}
            <Link 
              to="/login" 
              className="text-blue-500 hover:underline font-medium"
            >
              {step === 1 ? "Đăng nhập" : "Đăng nhập"}
            </Link>
          </p>
          
          {step === 1 && (
            <p className="text-gray-600">
              Chưa có tài khoản?{" "}
              <Link 
                to="/register" 
                className="text-blue-500 hover:underline font-medium"
              >
                Đăng ký
              </Link>
            </p>
          )}
        </div>
      </div>
    </div>
  );
};

export default ForgotPassword;