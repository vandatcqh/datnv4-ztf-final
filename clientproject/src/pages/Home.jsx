import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import axios from "axios";
import Header from "./Header";
import FriendList from "./FriendList";

const Home = () => {
  const [user, setUser] = useState(null);
  const [friends, setFriends] = useState([]);
  const [friendsLoading, setFriendsLoading] = useState(false);
  const navigate = useNavigate();

  useEffect(() => {
    const token = localStorage.getItem("token");
    if (!token) {
      navigate("/login");
      return;
    }

    fetchUser();
    fetchFriends();
  }, [navigate]);

  const fetchUser = async () => {
    const token = localStorage.getItem("token");
    const userId = localStorage.getItem("userId");
    
    try {
      const res = await axios.post(
        "http://localhost:8030/users/profile",
        { userId },
        { headers: { Authorization: `Bearer ${token}` } }
      );
      setUser(res.data);
      localStorage.setItem("user", JSON.stringify(res.data));
    } catch (err) {
      console.error("Fetch user error:", err);
      // Xử lý lỗi token nếu cần
    }
  };

  const fetchFriends = async () => {
    const token = localStorage.getItem("token");
    setFriendsLoading(true);
    
    try {
      const response = await axios.get(
        "http://localhost:8030/friends/friends",
        { 
          headers: { 
            Authorization: `Bearer ${token}`,
            "Content-Type": "application/json"
          } 
        }
      );
      setFriends(response.data);
    } catch (err) {
      console.error("Fetch friends error:", err);
    } finally {
      setFriendsLoading(false);
    }
  };

  return (
    <div className="min-h-screen bg-gray-50">
      <Header />
      
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-6">
        <div className="grid grid-cols-1 lg:grid-cols-4 gap-6">
          {/* Main Content - 3/4 width */}
          <div className="lg:col-span-3">
            <div className="bg-white rounded-2xl shadow-lg p-6">
              <h2 className="text-2xl font-bold text-gray-800 mb-4">Xin chào tất cả mọi người! 👋</h2>
              <p className="text-gray-600 mb-6">
                {user ? `Xin chào ${user.fullName || user.username}!` : "...."}
              </p>
              
              <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
                <div 
                  className="bg-blue-50 border border-blue-200 rounded-lg p-6 cursor-pointer hover:bg-blue-100 transition"
                  onClick={() => navigate("/profile")}
                >
                  <h3 className="text-lg font-semibold text-blue-800 mb-2">👤 Trang cá nhân</h3>
                  <p className="text-blue-600 text-sm">Quản lý thông tin cá nhân và bạn bè</p>
                </div>
                
                <div className="bg-green-50 border border-green-200 rounded-lg p-6">
                  <h3 className="text-lg font-semibold text-green-800 mb-2">📊 Thống kê</h3>
                  <p className="text-green-600 text-sm">Bạn có {friends.length} người bạn</p>
                </div>
              </div>
            </div>
          </div>

          {/* Sidebar - Danh sách bạn bè */}
          <div className="lg:col-span-1">
            <FriendList 
              friends={friends} 
              loading={friendsLoading} 
              onViewAll={() => navigate("/profile?tab=friends")}
            />
          </div>
        </div>
      </div>
    </div>
  );
};

export default Home;