import { useState, useContext } from "react";
import { AuthContext } from "../auth/AuthContext";
import { useNavigate, Link } from "react-router-dom";
import api from "../api/axios";
import { jwtDecode } from "jwt-decode";


const Login = () => {

  const { login } = useContext(AuthContext);
  const navigate = useNavigate();

  const [data, setData] = useState({
    username: "",
    password: ""
  });

  const [error, setError] = useState("");

  const handleLogin = async (e) => {

    e.preventDefault();

    try {

      setError("");

      // ✅ SEND CORRECT DATA
      const res = await api.post("/auth/login", data);

      const token = res.data.token;

      // ✅ SAVE TOKEN
      localStorage.setItem("token", token);

      // ✅ UPDATE CONTEXT STATE
      login(token);

      // ✅ DECODE ROLE FROM JWT
      const decoded = jwtDecode(token);

      const role = decoded.role;

      // ✅ ROLE BASED REDIRECT
      if (role === "ADMIN") {
        navigate("/admin/dashboard");
      } else {
        navigate("/dashboard");
      }

    } catch (err) {

      console.error(err);

      setError(
        err.response?.data?.message || "Login failed"
      );
    }
  };

  return (
    <div className="auth-container">

      <h2>Login</h2>

      <form onSubmit={handleLogin}>

        <input
          type="text"
          placeholder="Username"
          value={data.username}
          onChange={(e) =>
            setData({ ...data, username: e.target.value })
          }
          required
        />

        <input
          type="password"
          placeholder="Password"
          value={data.password}
          onChange={(e) =>
            setData({ ...data, password: e.target.value })
          }
          required
        />

        <button type="submit">Login</button>

      </form>

      {error && <p className="error">{error}</p>}

      <p>
        Don’t have an account? <Link to="/register">Register</Link>
      </p>

    </div>
  );
};

export default Login;
