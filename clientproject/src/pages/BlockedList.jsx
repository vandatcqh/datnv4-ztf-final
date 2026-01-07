import { useState, useEffect } from "react";
import axios from "axios";

const BlockedList = () => {
  const [blockedUsers, setBlockedUsers] = useState([]);
  const [loading, setLoading] = useState(true);
  const [message, setMessage] = useState("");

  useEffect(() => {
    fetchBlockedUsers();
  }, []);

  const fetchBlockedUsers = async () => {
    const token = localStorage.getItem("token");
    
    try {
      const response = await axios.get(
        "http://localhost:8030/friends/blocked-users",
        { 
          headers: { 
            Authorization: `Bearer ${token}`,
            "Content-Type": "application/json"
          } 
        }
      );
      setBlockedUsers(response.data);
    } catch (err) {
      console.error("Fetch blocked users error:", err);
      setMessage("Không thể tải danh sách người dùng đã chặn");
    } finally {
      setLoading(false);
    }
  };

  const handleUnblock = async (targetId) => {
    const token = localStorage.getItem("token");
    
    try {
      await axios.post(
        `http://localhost:8030/friends/unblock/${targetId}`,
        {},
        { 
          headers: { 
            Authorization: `Bearer ${token}`,
            "Content-Type": "application/json"
          } 
        }
      );
      
      setMessage("Đã bỏ chặn người dùng!");
      fetchBlockedUsers(); // Refresh list
      
      // Auto hide success message after 3 seconds
      setTimeout(() => setMessage(""), 3000);
    } catch (err) {
      console.error("Unblock user error:", err);
      setMessage("Bỏ chặn thất bại, vui lòng thử lại");
    }
  };

  if (loading) return <div className="text-center py-4">Đang tải...</div>;

  return (
    <div className="space-y-4">
      <h3 className="text-lg font-semibold text-gray-700">Danh sách người dùng đã chặn</h3>
      
      {message && (
        <p className={`text-center ${
          message.includes("thành công") || message.includes("Đã") 
            ? "text-green-600" 
            : "text-red-600"
        }`}>
          {message}
        </p>
      )}

      {blockedUsers.length === 0 ? (
        <p className="text-center text-gray-500 py-4">Bạn chưa chặn người dùng nào</p>
      ) : (
        <div className="space-y-3">
          {blockedUsers.map((user) => (
            <div key={user.relationshipId} className="bg-white border border-gray-200 rounded-lg p-4 shadow-sm">
              <div className="flex justify-between items-center">
                <div className="flex items-center space-x-3">
                  {/* Avatar placeholder - có thể thay bằng avatar thực tế */}
                  <div className="w-10 h-10 bg-gray-300 rounded-full flex items-center justify-center">
                    <span className="text-gray-600 font-medium text-sm">
                      {user.fullname?.charAt(0) || user.username?.charAt(0) || 'U'}
                    </span>
                  </div>
                  <div>
                    <h4 className="font-medium text-gray-800">{user.fullname}</h4>
                    <p className="text-sm text-gray-600">@{user.username}</p>
                    <p className="text-xs text-gray-500">
                      Chặn lúc: {new Date(user.createdAt).toLocaleString()}
                    </p>
                  </div>
                </div>
                <button
                  onClick={() => handleUnblock(user.userId)}
                  className="bg-red-500 text-white px-4 py-2 rounded-lg hover:bg-red-600 transition text-sm"
                >
                  Bỏ chặn
                </button>
              </div>
            </div>
          ))}
        </div>
      )}
    </div>
  );
};

export default BlockedList;