import { useState, useEffect } from "react";
import axios from "axios";

const SentRequests = () => {
  const [requests, setRequests] = useState([]);
  const [loading, setLoading] = useState(true);
  const [message, setMessage] = useState("");

  useEffect(() => {
    fetchSentRequests();
  }, []);

  const fetchSentRequests = async () => {
    const token = localStorage.getItem("token");
    
    try {
      const response = await axios.get(
        "http://localhost:8030/friends/sent-requests",
        { 
          headers: { 
            Authorization: `Bearer ${token}`,
            "Content-Type": "application/json"
          } 
        }
      );
      setRequests(response.data);
    } catch (err) {
      console.error("Fetch sent requests error:", err);
      setMessage("Không thể tải danh sách lời mời đã gửi");
    } finally {
      setLoading(false);
    }
  };

  const handleCancel = async (targetId) => {
    const token = localStorage.getItem("token");
    
    try {
      await axios.delete(
        `http://localhost:8030/friends/cancel/${targetId}`,
        { 
          headers: { 
            Authorization: `Bearer ${token}`,
            "Content-Type": "application/json"
          } 
        }
      );
      
      setMessage("Đã hủy lời mời kết bạn!");
      fetchSentRequests(); // Refresh list
    } catch (err) {
      console.error("Cancel friend request error:", err);
      setMessage("Hủy thất bại, vui lòng thử lại");
    }
  };

  if (loading) return <div className="text-center py-4">Đang tải...</div>;

  return (
    <div className="space-y-4">
      <h3 className="text-lg font-semibold text-gray-700">Lời mời kết bạn đã gửi</h3>
      
      {message && (
        <p className={`text-center ${
          message.includes("thành công") || message.includes("Đã") 
            ? "text-green-600" 
            : "text-red-600"
        }`}>
          {message}
        </p>
      )}

      {requests.length === 0 ? (
        <p className="text-center text-gray-500 py-4">Bạn chưa gửi lời mời kết bạn nào</p>
      ) : (
        <div className="space-y-3">
          {requests.map((request) => (
            <div key={request.relationshipId} className="bg-white border border-gray-200 rounded-lg p-4 shadow-sm">
              <div className="flex justify-between items-center">
                <div>
                  <h4 className="font-medium text-gray-800">{request.fullname}</h4>
                  <p className="text-sm text-gray-600">@{request.username} (ID: {request.userId})</p>
                  <p className="text-xs text-gray-500">
                    Gửi lúc: {new Date(request.createdAt).toLocaleString()}
                  </p>
                </div>
                <button
                  onClick={() => handleCancel(request.userId)}
                  className="bg-gray-500 text-white px-4 py-2 rounded-lg hover:bg-gray-600 transition text-sm"
                >
                  Hủy lời mời
                </button>
              </div>
            </div>
          ))}
        </div>
      )}
    </div>
  );
};

export default SentRequests;