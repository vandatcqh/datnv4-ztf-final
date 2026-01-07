import { useState, useEffect } from "react";
import axios from "axios";

const FriendList = ({ friends, loading, onViewAll }) => {
  return (
    <div className="bg-white rounded-2xl shadow-lg p-6 sticky top-6">
      <h3 className="text-lg font-bold text-gray-800 mb-4 flex items-center">
        <span className="mr-2">👥</span>
        Bạn bè ({friends.length})
      </h3>
      
      {loading ? (
        <div className="text-center py-4">
          <div className="animate-spin rounded-full h-8 w-8 border-b-2 border-blue-500 mx-auto"></div>
          <p className="text-gray-500 mt-2">Đang tải...</p>
        </div>
      ) : friends.length === 0 ? (
        <div className="text-center py-4">
          <p className="text-gray-500">Chưa có bạn bè</p>
          <button 
            onClick={onViewAll}
            className="text-blue-500 hover:text-blue-600 text-sm mt-2"
          >
            Kết bạn ngay
          </button>
        </div>
      ) : (
        <div className="space-y-3 max-h-96 overflow-y-auto">
          {friends.slice(0, 10).map((friend) => (
            <div key={friend.relationshipId} className="flex items-center space-x-3 p-2 rounded-lg hover:bg-gray-50 transition">
              <div className="w-8 h-8 bg-blue-100 rounded-full flex items-center justify-center flex-shrink-0">
                <span className="text-blue-600 font-medium text-sm">
                  {friend.fullname?.charAt(0) || friend.username?.charAt(0) || 'F'}
                </span>
              </div>
              <div className="flex-1 min-w-0">
                <p className="text-sm font-medium text-gray-800 truncate">
                  {friend.fullname}
                </p>
                <p className="text-xs text-gray-500 truncate">
                  @{friend.username}
                </p>
              </div>
            </div>
          ))}
          
          {friends.length > 10 && (
            <button 
              onClick={onViewAll}
              className="w-full text-center text-blue-500 hover:text-blue-600 text-sm py-2 border-t border-gray-100"
            >
              Xem tất cả ({friends.length})
            </button>
          )}
        </div>
      )}
    </div>
  );
};

export default FriendList;