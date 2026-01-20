import { useState } from "react";
import { useNavigate, Link } from "react-router-dom";
import api from "../api/axios"; // Axios instance

const Register = () => {
  const navigate = useNavigate();
  const [data, setData] = useState({
    username: "",
    email: "",
    password: "",
    role: ""
  });
  const [error, setError] = useState("");
  const [success, setSuccess] = useState("");

  const handleRegister = async (e) => {
    e.preventDefault();

    try {
      setError("");
      setSuccess("");

      const payload = {
        username: data.username,
        email: data.email,
        password: data.password,
        role: data.role
      };

      const res = await api.post("/auth/register", payload);

      setSuccess(res.data.message || "User registered successfully");

      setTimeout(() => navigate("/login"), 1500);
    } catch (err) {
      console.error(err);
      setError(err.response?.data?.message || err.message || "Registration failed");
    }
  };

  return (
    <div className="auth-container">
      <h2>Register</h2>
      <form onSubmit={handleRegister}>
        <input
          type="text"
          placeholder="Username"
          value={data.username}
          onChange={(e) => setData({ ...data, username: e.target.value })}
          required
        />
        <input
          type="email"
          placeholder="Email"
          value={data.email}
          onChange={(e) => setData({ ...data, email: e.target.value })}
          required
        />
        <input
          type="password"
          placeholder="Password"
          value={data.password}
          onChange={(e) => setData({ ...data, password: e.target.value })}
          required
        />
        <select
          value={data.role}
          onChange={(e) => setData({ ...data, role: e.target.value })}
          required
        >
          <option value="">Select role</option>
          <option value="SALES_EXECUTIVE">Sales Executive</option>
          <option value="INVENTORY_MANAGER">Inventory Manager</option>
          <option value="PURCHASE_MANAGER">Purchase Manager</option>
          <option value="ACCOUNTANT">Accountant</option>
        </select>
        <button type="submit">Register</button>
      </form>

      {error && <p className="error">{error}</p>}
      {success && <p className="success">{success}</p>}

      <p className="mt-20">
        Already have an account? <Link to="/login">Login</Link>
      </p>
    </div>
  );
};

export default Register;
