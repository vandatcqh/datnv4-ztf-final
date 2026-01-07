import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import axios from "axios";
import Header from "./Header";
import FriendManagement from "./FriendManagement";

const Profile = () => {
  const [user, setUser] = useState(null);
  const [error, setError] = useState("");
  const [newFullname, setNewFullname] = useState("");
  const [updating, setUpdating] = useState(false);
  const [activeTab, setActiveTab] = useState("profile"); // "profile" | "friends"
  const navigate = useNavigate();

  useEffect(() => {
    const token = localStorage.getItem("token");
    if (!token) {
      navigate("/login");
      return;
    }
    fetchUser();
  }, [navigate]);

  const fetchUser = async () => {
    const token = localStorage.getItem("token");
    try {
      const res = await axios.get("http://localhost:8030/user/profile", {
        headers: { Authorization: `Bearer ${token}` },
      });

      const userData = res.data.data;
      setUser(userData);
      setNewFullname(userData.fullname); // <-- fix input hiển thị
      localStorage.setItem("user", JSON.stringify(userData));
      localStorage.setItem("userId", userData.userId);
    } catch (err) {
      if (err.response?.status === 401) {
        handleLogout();
      } else {
        setError(err.response?.data?.message || "Không thể tải thông tin người dùng");
      }
    }
  };

  const handleLogout = async () => {
    try {
      await axios.post("http://localhost:8030/auth/logout", {}, { withCredentials: true });
    } catch (err) {
      console.error("Logout API error:", err);
    } finally {
      localStorage.removeItem("token");
      localStorage.removeItem("user");
      localStorage.removeItem("userId");
      navigate("/login");
    }
  };

  const handleUpdateFullname = async () => {
    if (!newFullname.trim()) return;
    setUpdating(true);
    const token = localStorage.getItem("token");
    const userId = localStorage.getItem("userId");

    try {
      await axios.put(
        "http://localhost:8030/user/update_fullname",
        { userId: Number(userId), fullname: newFullname },
        { headers: { Authorization: `Bearer ${token}` } }
      );

      await fetchUser(); // cập nhật lại user
      alert("Cập nhật họ tên thành công!");
    } catch (err) {
      console.error(err);
      setError(err.response?.data?.message || "Cập nhật thất bại");
    } finally {
      setUpdating(false);
    }
  };

  return (
    <div className="min-h-screen bg-gray-50">
      <Header />
      <div className="max-w-4xl mx-auto px-4 sm:px-6 lg:px-8 py-6">
        <div className="bg-white rounded-2xl shadow-lg p-6">
          <h1 className="text-2xl font-bold text-gray-800 mb-6">Trang cá nhân</h1>
          {error && <p className="text-red-600 text-center mb-4">{error}</p>}

          {/* Tab Navigation */}
          <div className="flex border-b border-gray-200 mb-6">
            <button
              className={`flex-1 py-2 font-medium text-center ${
                activeTab === "profile"
                  ? "text-blue-600 border-b-2 border-blue-600"
                  : "text-gray-500 hover:text-gray-700"
              }`}
              onClick={() => setActiveTab("profile")}
            >
              Thông tin cá nhân
            </button>
            <button
              className={`flex-1 py-2 font-medium text-center ${
                activeTab === "friends"
                  ? "text-blue-600 border-b-2 border-blue-600"
                  : "text-gray-500 hover:text-gray-700"
              }`}
              onClick={() => setActiveTab("friends")}
            >
              Quản lý bạn bè
            </button>
          </div>

          {user ? (
            <div>
              {activeTab === "profile" ? (
                <div className="space-y-6">
                  <div className="flex items-center space-x-6">
                    <img
                      src="/duck.jpg"
                      alt="Avatar"
                      className="w-20 h-20 rounded-full object-cover border-2 border-gray-300"
                    />
                    <div>
                      <h2 className="text-xl font-bold text-gray-800">{user.fullname}</h2>
                      <p className="text-gray-600">@{user.username}</p>
                    </div>
                  </div>

                  <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
                    <div>
                      <label className="font-semibold text-gray-700 block mb-2">User ID:</label>
                      <p className="text-gray-600 bg-gray-50 px-4 py-2 rounded-lg">{user.userId}</p>
                    </div>

                    <div>
                      <label className="font-semibold text-gray-700 block mb-2">Họ tên:</label>
                      <input
                        type="text"
                        value={newFullname}
                        onChange={(e) => setNewFullname(e.target.value)}
                        className="border border-gray-300 rounded-lg px-4 py-2 w-full focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-transparent"
                        placeholder="Nhập họ tên mới"
                      />
                    </div>

                    <div>
                      <label className="font-semibold text-gray-700 block mb-2">Tên đăng nhập:</label>
                      <p className="text-gray-600 bg-gray-50 px-4 py-2 rounded-lg">{user.username}</p>
                    </div>

                    <div>
                      <label className="font-semibold text-gray-700 block mb-2">Email:</label>
                      <p className="text-gray-600 bg-gray-50 px-4 py-2 rounded-lg">{user.email}</p>
                    </div>

                    <div>
                      <label className="font-semibold text-gray-700 block mb-2">Ngày tạo:</label>
                      <p className="text-gray-600 bg-gray-50 px-4 py-2 rounded-lg">
                        {new Date(user.createdAt).toLocaleString()}
                      </p>
                    </div>
                  </div>

                  <div className="flex space-x-4 pt-4">
                    <button
                      onClick={handleUpdateFullname}
                      disabled={updating}
                      className="bg-blue-500 text-white px-6 py-2 rounded-lg hover:bg-blue-600 transition disabled:opacity-50 font-medium"
                    >
                      {updating ? "Đang cập nhật..." : "Cập nhật họ tên"}
                    </button>

                    <button
                      onClick={() => navigate("/")}
                      className="bg-gray-500 text-white px-6 py-2 rounded-lg hover:bg-gray-600 transition font-medium"
                    >
                      Quay lại trang chủ
                    </button>
                  </div>
                </div>
              ) : (
                <FriendManagement />
              )}
            </div>
          ) : (
            !error && (
              <div className="text-center py-8">
                <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-blue-500 mx-auto"></div>
                <p className="text-gray-600 mt-4">Đang tải thông tin...</p>
              </div>
            )
          )}
        </div>
      </div>
    </div>
  );
};

export default Profile;
