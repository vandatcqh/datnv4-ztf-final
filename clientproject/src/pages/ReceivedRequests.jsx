import { useState, useEffect } from "react";
import axios from "axios";

const ReceivedRequests = () => {
  const [requests, setRequests] = useState([]);
  const [loading, setLoading] = useState(true);
  const [message, setMessage] = useState("");

  useEffect(() => {
    fetchReceivedRequests();
  }, []);

  const fetchReceivedRequests = async () => {
    const token = localStorage.getItem("token");
    
    try {
      const response = await axios.get(
        "http://localhost:8030/friends/received-requests",
        { 
          headers: { 
            Authorization: `Bearer ${token}`,
            "Content-Type": "application/json"
          } 
        }
      );
      setRequests(response.data);
    } catch (err) {
      console.error("Fetch received requests error:", err);
      setMessage("Không thể tải danh sách lời mời");
    } finally {
      setLoading(false);
    }
  };

  const handleAccept = async (requesterId) => {
    const token = localStorage.getItem("token");
    
    try {
      await axios.post(
        `http://localhost:8030/friends/accept/${requesterId}`,
        {},
        { 
          headers: { 
            Authorization: `Bearer ${token}`,
            "Content-Type": "application/json"
          } 
        }
      );
      
      setMessage("Đã chấp nhận lời mời kết bạn!");
      fetchReceivedRequests(); // Refresh list
    } catch (err) {
      console.error("Accept friend request error:", err);
      setMessage("Chấp nhận thất bại, vui lòng thử lại");
    }
  };

  const handleDecline = async (requesterId) => {
    const token = localStorage.getItem("token");
    
    try {
      await axios.post(
        `http://localhost:8030/friends/decline/${requesterId}`,
        {},
        { 
          headers: { 
            Authorization: `Bearer ${token}`,
            "Content-Type": "application/json"
          } 
        }
      );
      
      setMessage("Đã từ chối lời mời kết bạn!");
      fetchReceivedRequests(); // Refresh list
    } catch (err) {
      console.error("Decline friend request error:", err);
      setMessage("Từ chối thất bại, vui lòng thử lại");
    }
  };

  if (loading) return <div className="text-center py-4">Đang tải...</div>;

  return (
    <div className="space-y-4">
      <h3 className="text-lg font-semibold text-gray-700">Lời mời kết bạn đã nhận</h3>
      
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
        <p className="text-center text-gray-500 py-4">Không có lời mời kết bạn nào</p>
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
                <div className="flex space-x-2">
                  <button
                    onClick={() => handleAccept(request.userId)}
                    className="bg-green-500 text-white px-4 py-2 rounded-lg hover:bg-green-600 transition text-sm"
                  >
                    Chấp nhận
                  </button>
                  <button
                    onClick={() => handleDecline(request.userId)}
                    className="bg-red-500 text-white px-4 py-2 rounded-lg hover:bg-red-600 transition text-sm"
                  >
                    Từ chối
                  </button>
                </div>
              </div>
            </div>
          ))}
        </div>
      )}
    </div>
  );
};

export default ReceivedRequests;