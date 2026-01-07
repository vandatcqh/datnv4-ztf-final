import { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import axios from "axios";
import SentRequests from "./SentRequests";
import ReceivedRequests from "./ReceivedRequests";
import FriendList from "./FriendList";
import FriendRequest from "./FriendRequest";
import BlockedList from "./BlockedList";

const FriendManagement = () => {
  const [activeSection, setActiveSection] = useState("received");

  // State bạn bè
  const [friends, setFriends] = useState([]);
  const [friendsLoading, setFriendsLoading] = useState(false);
  const navigate = useNavigate();

  useEffect(() => {
    if (activeSection === "friends") {
      fetchFriends();
    }
  }, [activeSection]);

  const fetchFriends = async () => {
    const token = localStorage.getItem("token");
    setFriendsLoading(true);

    try {
      const response = await axios.get(
        "http://localhost:8030/friends/friends",
        {
          headers: {
            Authorization: `Bearer ${token}`,
            "Content-Type": "application/json",
          },
        }
      );
      setFriends(response.data);
    } catch (err) {
      console.error("Fetch friends error:", err);
      setFriends([]);
    } finally {
      setFriendsLoading(false);
    }
  };

  return (
    <div className="space-y-6">
      <div className="grid grid-cols-1 lg:grid-cols-3 gap-6">
        {/* Form gửi lời mời */}
        <div className="lg:col-span-1">
          <FriendRequest />
        </div>

        {/* Tabs quản lý */}
        <div className="lg:col-span-2">
          <div className="bg-white rounded-lg border border-gray-200">
            <div className="flex border-b border-gray-200">
              <button
                className={`flex-1 py-3 font-medium text-center ${
                  activeSection === "received"
                    ? "text-blue-600 border-b-2 border-blue-600"
                    : "text-gray-500 hover:text-gray-700"
                }`}
                onClick={() => setActiveSection("received")}
              >
                Lời mời đến
              </button>
              <button
                className={`flex-1 py-3 font-medium text-center ${
                  activeSection === "sent"
                    ? "text-blue-600 border-b-2 border-blue-600"
                    : "text-gray-500 hover:text-gray-700"
                }`}
                onClick={() => setActiveSection("sent")}
              >
                Đã gửi
              </button>
              <button
                className={`flex-1 py-3 font-medium text-center ${
                  activeSection === "blocked"
                    ? "text-blue-600 border-b-2 border-blue-600"
                    : "text-gray-500 hover:text-gray-700"
                }`}
                onClick={() => setActiveSection("blocked")}
              >
                Đã chặn
              </button>
            </div>

            {/* Nội dung */}
            <div className="min-h-[400px] p-4">
              {activeSection === "received" && <ReceivedRequests />}
              {activeSection === "sent" && <SentRequests />}
              {activeSection === "friends" && (
                <FriendList
                  friends={friends}
                  loading={friendsLoading}
                  onViewAll={() => navigate("/profile?tab=friends")}
                />
              )}
              {activeSection === "blocked" && <BlockedList />}
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default FriendManagement;
