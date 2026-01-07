import { useState, useRef, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import Setup2FAModal from "./Setup2FAModal";

const Header = () => {
  const [user, setUser] = useState(null);
  const [showDropdown, setShowDropdown] = useState(false);
  const [show2FAModal, setShow2FAModal] = useState(false);
  const [twoFAData, setTwoFAData] = useState(null);
  const dropdownRef = useRef(null);
  const navigate = useNavigate();

  useEffect(() => {
    const userData = localStorage.getItem("user");
    if (userData) setUser(JSON.parse(userData));

    const handleClickOutside = (event) => {
      if (dropdownRef.current && !dropdownRef.current.contains(event.target)) {
        setShowDropdown(false);
      }
    };

    document.addEventListener("mousedown", handleClickOutside);
    return () => document.removeEventListener("mousedown", handleClickOutside);
  }, []);

  const handleLogout = async () => {
    try {
      await fetch("http://localhost:8030/auth/logout", {
        method: "POST",
        credentials: "include",
      });
    } catch (err) {
      console.error("Logout API error:", err);
    } finally {
      localStorage.removeItem("token");
      localStorage.removeItem("user");
      localStorage.removeItem("userId");
      navigate("/login");
    }
  };

  const handleProfile = () => {
    navigate("/profile");
    setShowDropdown(false);
  };

  // 🔹 Nút bật 2FA
  const handleSetup2FA = async () => {
    try {
      const userId = localStorage.getItem("userId");
      const response = await fetch("http://localhost:8030/auth/2fa/setup", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        credentials: "include",
        body: JSON.stringify({ userId }),
      });

      const result = await response.json();

      if (result.status === 200 || result.status === 201) {
        setTwoFAData(result.data);  // lưu dữ liệu trực tiếp
        setShow2FAModal(true);      // mở modal
      } else {
        alert("Lỗi khi tạo 2FA: " + result.message);
      }
    } catch (err) {
      console.error("Setup 2FA error:", err);
      alert("Không thể bật 2FA lúc này.");
    } finally {
      setShowDropdown(false);
    }
  };

  if (!user) return null;

  return (
    <>
      <header className="bg-white shadow-sm border-b border-gray-200">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <div className="flex justify-between items-center h-16">
            <div className="flex items-center">
              <h1
                className="text-xl font-bold text-gray-800 cursor-pointer"
                onClick={() => navigate("/")}
              >
                AMS
              </h1>
            </div>

            <div className="flex items-center space-x-4">
              <span className="text-gray-700 font-medium hidden sm:block">
                {user.fullName || user.username}
              </span>
              <div className="relative" ref={dropdownRef}>
                <img
                  src="/duck.jpg"
                  alt="Avatar"
                  className="w-10 h-10 rounded-full object-cover cursor-pointer border-2 border-gray-300 hover:border-blue-500 transition"
                  onClick={() => setShowDropdown(!showDropdown)}
                />

                {showDropdown && (
                  <div className="absolute right-0 mt-2 w-48 bg-white rounded-lg shadow-lg border border-gray-200 z-50">
                    <button
                      onClick={handleProfile}
                      className="w-full text-left px-4 py-3 text-sm text-gray-700 hover:bg-gray-100 transition flex items-center"
                    >
                      <span className="mr-2">👤</span>
                      Trang cá nhân
                    </button>

                    <button
                      onClick={handleSetup2FA}
                      className="w-full text-left px-4 py-3 text-sm text-blue-600 hover:bg-gray-100 transition flex items-center border-t border-gray-100"
                    >
                      <span className="mr-2">🔒</span>
                      Bật 2FA
                    </button>

                    <button
                      onClick={handleLogout}
                      className="w-full text-left px-4 py-3 text-sm text-red-600 hover:bg-gray-100 transition flex items-center border-t border-gray-100"
                    >
                      <span className="mr-2">🚪</span>
                      Đăng xuất
                    </button>
                  </div>
                )}
              </div>
            </div>
          </div>
        </div>
      </header>

      {/* Modal hiển thị QR 2FA */}
      {show2FAModal && (
        <Setup2FAModal
          data={twoFAData}
          onClose={() => setShow2FAModal(false)}
        />
      )}
    </>
  );
};

export default Header;
