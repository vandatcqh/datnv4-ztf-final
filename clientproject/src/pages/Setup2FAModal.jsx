import { useEffect, useState } from "react";

const Setup2FAModal = ({ data, onClose }) => {
  const [qrCode, setQrCode] = useState("");
  const [otp, setOtp] = useState("");
  const [isVerifying, setIsVerifying] = useState(false);
  const [message, setMessage] = useState("");

  useEffect(() => {
    if (data) {
      setQrCode(data.qrCodeURL);
      setMessage(""); // reset thông báo khi mở lại modal
    }
  }, [data]);

  if (!data) return null;

  const handleVerify = async () => {
    if (!otp.trim()) {
      setMessage("⚠️ Vui lòng nhập mã OTP.");
      return;
    }

    setIsVerifying(true);
    setMessage("");

    try {
      // userId lấy từ localStorage (đã có sẵn sau login)
      const userId = Number(localStorage.getItem("userId"));

      const res = await fetch("http://localhost:8030/auth/2fa/verify", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ userId, otp }),
      });

      const result = await res.json();

      if (res.ok && result.status === 200) {
        setMessage("✅ Xác minh thành công! 2FA đã được bật.");
      } else {
        setMessage("❌ Mã OTP không hợp lệ.");
      }
    } catch (err) {
      setMessage("⚠️ Lỗi khi kết nối tới server.");
    } finally {
      setIsVerifying(false);
    }
  };


  return (
    <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50">
      <div className="bg-white rounded-xl shadow-lg p-6 w-80 relative">
        {/* Nút đóng */}
        <button
          className="absolute top-2 right-2 text-gray-500 hover:text-gray-800"
          onClick={onClose}
        >
          ✕
        </button>

        <h2 className="text-lg font-bold mb-4 text-center">Thiết lập 2FA</h2>

        {/* QR Code */}
        {qrCode ? (
          <img
            src={qrCode}
            alt="QR Code 2FA"
            className="mx-auto mb-4"
            style={{ width: "200px", height: "200px" }}
          />
        ) : (
          <p className="text-center">Đang tải QR code...</p>
        )}

        {/* Secret Key */}
        <p className="text-center text-sm break-all mb-4">
          Secret: {data.secret}
        </p>

        {/* Nếu chưa bật 2FA → hiện ô nhập OTP */}
        {!data.enabled ? (
          <>
            <input
              type="text"
              value={otp}
              onChange={(e) => setOtp(e.target.value)}
              placeholder="Nhập mã OTP từ Google Authenticator"
              className="w-full border border-gray-300 rounded-md p-2 mb-3 text-center"
            />
            <button
              onClick={handleVerify}
              disabled={isVerifying}
              className={`w-full py-2 rounded-md text-white ${
                isVerifying
                  ? "bg-gray-400 cursor-not-allowed"
                  : "bg-blue-600 hover:bg-blue-700"
              }`}
            >
              {isVerifying ? "Đang xác minh..." : "Xác minh & Bật 2FA"}
            </button>
          </>
        ) : (
          <p className="text-green-600 text-center font-medium">
            ✅ 2FA đã được kích hoạt!
          </p>
        )}

        {/* Thông báo */}
        {message && (
          <p className="text-center text-sm mt-3 text-gray-700">{message}</p>
        )}
      </div>
    </div>
  );
};

export default Setup2FAModal;
