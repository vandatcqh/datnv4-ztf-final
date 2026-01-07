import { useState, useEffect } from "react";
import axios from "axios";

export default function Admin() {
  const [users, setUsers] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");

  const [showCreateModal, setShowCreateModal] = useState(false);
  const [newUser, setNewUser] = useState({ username: "", password: "", fullname: "", email: "" });

  const [actionMenu, setActionMenu] = useState(null);
  const [actionMenuPosition, setActionMenuPosition] = useState({ top: 0, left: 0 });
  const [showLockModal, setShowLockModal] = useState(false);
  const [lockData, setLockData] = useState({ userId: null, availableFrom: "" });

  const token = localStorage.getItem("token");

  const fetchUsers = async () => {
    setLoading(true);
    setError("");
    try {
      const res = await axios.get("http://localhost:8030/management/allusers", {
        headers: { Authorization: `Bearer ${token}` },
      });
      if (res.data.status === 200) {
        setUsers(res.data.data.users);
      } else {
        setError("Không đủ quyền truy cập hoặc lỗi server");
      }
    } catch (err) {
      if (err.response?.status === 401 || err.response?.status === 403) setError("Không đủ quyền truy cập");
      else setError("Lỗi khi gọi API");
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchUsers();
  }, []);

  const handleCreateUserChange = (e) => {
    setNewUser({ ...newUser, [e.target.name]: e.target.value });
  };

  const handleCreateUserSubmit = async (e) => {
    e.preventDefault();
    try {
      const res = await axios.post("http://localhost:8030/management/create_user", newUser, {
        headers: { Authorization: `Bearer ${token}` },
      });
      if ([200, 201].includes(res.data.status)) {
        setShowCreateModal(false);
        setNewUser({ username: "", password: "", fullname: "", email: "" });
        fetchUsers();
      } else alert("Tạo user lỗi: " + res.data.message);
    } catch (err) {
      alert("Tạo user thất bại: " + (err.response?.data?.message || err.message));
    }
  };

  const handleDeleteUser = async (userId) => {
    if (!window.confirm("Bạn có chắc muốn xóa user này không?")) return;
    try {
      const res = await axios.delete(`http://localhost:8030/management/delete_user/${userId}`, {
        headers: { Authorization: `Bearer ${token}` },
      });
      if (res.data.status === 200) fetchUsers();
      else alert("Xóa user lỗi: " + res.data.message);
    } catch (err) {
      alert("Xóa user thất bại: " + (err.response?.data?.message || err.message));
    }
  };

  const handleLockUserSubmit = async (e) => {
    e.preventDefault();
    try {
      const availableFromTimestamp = new Date(lockData.availableFrom).getTime();
      const res = await axios.post(
        "http://localhost:8030/management/lock_user",
        { userId: lockData.userId, availableFrom: availableFromTimestamp },
        { headers: { Authorization: `Bearer ${token}` } }
      );
      if (res.data.status === 200) {
        setShowLockModal(false);
        setLockData({ userId: null, availableFrom: "" });
        fetchUsers();
      } else alert("Lock user lỗi: " + res.data.message);
    } catch (err) {
      alert("Lock user thất bại: " + (err.response?.data?.message || err.message));
    }
  };

  if (loading) return <p className="text-center mt-8">Đang tải dữ liệu...</p>;
  if (error) return <p className="text-center mt-8 text-red-600">{error}</p>;

  const handleActionClick = (e, userId) => {
    const rect = e.currentTarget.getBoundingClientRect();
    setActionMenuPosition({ top: rect.bottom + window.scrollY, left: rect.left + window.scrollX });
    setActionMenu(actionMenu === userId ? null : userId);
  };

  return (
    <div className="p-6 max-w-5xl mx-auto">
      <div className="flex justify-between items-center mb-6">
        <h2 className="text-3xl font-bold">Quản trị người dùng</h2>
        <button
          onClick={() => setShowCreateModal(true)}
          className="px-4 py-2 bg-blue-600 text-white rounded-xl shadow hover:bg-blue-700"
        >
          + Tạo user
        </button>
      </div>

      <div className="overflow-hidden rounded-xl shadow bg-white">
        <table className="w-full text-left">
          <thead className="bg-gray-100 text-gray-700">
            <tr>
              <th className="py-3 px-4">User</th>
              <th className="py-3 px-4">Email</th>
              <th className="py-3 px-4">Trạng thái</th>
              <th className="py-3 px-4">Tạo lúc</th>
              <th className="py-3 px-4 text-right">Hành động</th>
            </tr>
          </thead>
          <tbody>
            {users.map((u) => {
              const isLocked = !u.available;
              return (
                <tr key={u.userId} className="border-t hover:bg-gray-50">
                  <td className="py-3 px-4 font-medium">{u.username}<br/><span className="text-sm text-gray-500">{u.fullname}</span></td>
                  <td className="py-3 px-4">{u.email}</td>
                  <td className="py-3 px-4">
                    {isLocked ? (
                      <span className="text-red-600 font-semibold">Locked</span>
                    ) : (
                      <span className="text-green-600 font-semibold">Available</span>
                    )}
                  </td>
                  <td className="py-3 px-4">{new Date(u.createdAt).toLocaleString()}</td>
                  <td className="py-3 px-4 text-right relative">
                    <button
                      onClick={(e) => handleActionClick(e, u.userId)}
                      className="px-2 py-1 border rounded hover:bg-gray-200"
                    >
                      ...
                    </button>
                  </td>
                </tr>
              );
            })}
          </tbody>
        </table>
      </div>

      {actionMenu && (
        <div style={{ position: 'absolute', top: actionMenuPosition.top, left: actionMenuPosition.left, zIndex: 9999 }} className="w-36 bg-white border rounded-xl shadow-lg">
          <button
            className="w-full px-3 py-2 hover:bg-gray-100 text-sm"
            onClick={() => {
              setShowLockModal(true);
              setLockData({ userId: actionMenu, availableFrom: "" });
              setActionMenu(null);
            }}
          >
            Khóa
          </button>
          <button
            className="w-full px-3 py-2 hover:bg-gray-100 text-sm text-red-600"
            onClick={() => {
              handleDeleteUser(actionMenu);
              setActionMenu(null);
            }}
          >
            Xóa
          </button>
        </div>
      )}

      {showCreateModal && (
        <div className="fixed inset-0 bg-black bg-opacity-40 flex items-center justify-center z-50">
          <div className="bg-white p-6 rounded-xl shadow-xl w-96">
            <h3 className="text-xl font-bold mb-4">Tạo người dùng mới</h3>
            <form onSubmit={handleCreateUserSubmit} className="space-y-3">
              <input name="username" placeholder="Username" value={newUser.username} onChange={handleCreateUserChange} className="w-full border px-3 py-2 rounded" required/>
              <input name="password" type="password" placeholder="Password" value={newUser.password} onChange={handleCreateUserChange} className="w-full border px-3 py-2 rounded" required/>
              <input name="fullname" placeholder="Fullname" value={newUser.fullname} onChange={handleCreateUserChange} className="w-full border px-3 py-2 rounded" required/>
              <input name="email" placeholder="Email" value={newUser.email} onChange={handleCreateUserChange} className="w-full border px-3 py-2 rounded" required/>

              <div className="flex justify-end gap-2 pt-2">
                <button type="button" onClick={() => setShowCreateModal(false)} className="px-3 py-1 border rounded">Hủy</button>
                <button type="submit" className="px-3 py-1 bg-blue-600 text-white rounded hover:bg-blue-700">Tạo</button>
              </div>
            </form>
          </div>
        </div>
      )}

      {showLockModal && (
        <div className="fixed inset-0 bg-black bg-opacity-40 flex items-center justify-center z-50">
          <div className="bg-white p-6 rounded-xl shadow-xl w-96">
            <h3 className="text-xl font-bold mb-4">Khóa người dùng</h3>
            <form onSubmit={handleLockUserSubmit} className="space-y-3">
              <label className="text-sm">Khóa cho tới ngày, giờ</label>
              <input
                type="datetime-local"
                value={lockData.availableFrom}
                onChange={(e) => setLockData({ ...lockData, availableFrom: e.target.value })}
                className="w-full border px-3 py-2 rounded"
                required
              />
              <div className="flex justify-end gap-2 pt-2">
                <button type="button" onClick={() => setShowLockModal(false)} className="px-3 py-1 border rounded">Hủy</button>
                <button type="submit" className="px-3 py-1 bg-yellow-500 text-white rounded hover:bg-yellow-600">Khóa</button>
              </div>
            </form>
          </div>
        </div>
      )}
    </div>
  );
}