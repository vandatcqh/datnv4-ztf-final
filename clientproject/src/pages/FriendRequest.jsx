import { useState } from "react";
import axios from "axios";

const FriendRequest = () => {
  const [friendId, setFriendId] = useState("");
  const [loading, setLoading] = useState(false);
  const [message, setMessage] = useState("");

  const handleSendFriendRequest = async () => {
    if (!friendId.trim()) {
      setMessage("Vui lòng nhập ID người dùng");
      return;
    }

    const targetId = parseInt(friendId);
    if (isNaN(targetId)) {
      setMessage("ID người dùng phải là số");
      return;
    }

    setLoading(true);
    setMessage("");
    
    const token = localStorage.getItem("token");

    try {
      await axios.post(
        `http://localhost:8030/friends/request/${targetId}`,
        {  },
        { 
          headers: { 
            Authorization: `Bearer ${token}`,
            "Content-Type": "application/json"
          } 
        }
      );
      
      setMessage("Đã gửi lời mời kết bạn thành công!");
      setFriendId("");
    } catch (err) {
      console.error("Send friend request error:", err);
      
      const errorMessage = err.response?.data?.message || 
                          err.response?.data?.error ||
                          "Gửi lời mời thất bại, vui lòng thử lại";
      setMessage(errorMessage);
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="space-y-4 bg-white p-4 rounded-lg border border-gray-200">
      <h2 className="text-xl font-semibold text-gray-800 mb-4">Gửi lời mời kết bạn</h2>
      
      <div className="space-y-2">
        <label className="block text-sm font-medium text-gray-700">
          ID người dùng muốn kết bạn:
        </label>
        <input
          type="text"
          value={friendId}
          onChange={(e) => setFriendId(e.target.value)}
          placeholder="Nhập ID người dùng"
          className="border border-gray-300 rounded-lg px-3 py-2 w-full focus:outline-none focus:ring-2 focus:ring-blue-500"
          disabled={loading}
        />
      </div>

      <button
        onClick={handleSendFriendRequest}
        disabled={loading || !friendId.trim()}
        className="w-full bg-green-500 text-white py-2 rounded-lg hover:bg-green-600 transition disabled:opacity-50 disabled:cursor-not-allowed"
      >
        {loading ? "Đang gửi..." : "Gửi lời mời kết bạn"}
      </button>

      {message && (
        <p className={`text-center mt-2 ${
          message.includes("thành công") ? "text-green-600" : "text-red-600"
        }`}>
          {message}
        </p>
      )}

      <div className="mt-4 p-3 bg-gray-50 rounded-lg">
        <h3 className="font-medium text-gray-700 mb-2 text-sm">Hướng dẫn:</h3>
        <ul className="text-xs text-gray-600 space-y-1">
          <li>• Nhập ID người dùng bạn muốn kết bạn</li>
          <li>• ID là số nguyên (ví dụ: 1, 2, 3...)</li>
          <li>• Không thể gửi lời mời kết bạn cho chính mình</li>
        </ul>
      </div>
    </div>
  );
};

export default FriendRequest;