import { useEffect, useState } from "react";
import api from "../api/axios";

export default function Users() {
  const [users, setUsers] = useState([]);
  const [loading, setLoading] = useState(false);

  useEffect(() => {
    loadUsers();
  }, []);

  const loadUsers = async () => {
    try {
      setLoading(true);
      const res = await api.get("/admin/users");
      setUsers(res.data || []);
    } catch (err) {
      console.error("Failed to load users", err);
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="bg-white rounded-xl shadow p-6">
      <h2 className="text-xl font-semibold mb-4">Users</h2>

      {loading ? (
        <p>Loading...</p>
      ) : (
        <table className="w-full border text-sm">
          <thead className="bg-slate-100">
            <tr>
              <th className="border px-3 py-2">ID</th>
              <th className="border px-3 py-2">Name</th>
              <th className="border px-3 py-2">Email</th>
              <th className="border px-3 py-2">Role</th>
            </tr>
          </thead>
          <tbody>
            {users.map((u) => (
              <tr key={u.id}>
                <td className="border px-3 py-2">{u.id}</td>
                <td className="border px-3 py-2">{u.name}</td>
                <td className="border px-3 py-2">{u.email}</td>
                <td className="border px-3 py-2">{u.role}</td>
              </tr>
            ))}
          </tbody>
        </table>
      )}
    </div>
  );
}
