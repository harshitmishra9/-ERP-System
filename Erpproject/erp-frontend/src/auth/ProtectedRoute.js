import { Navigate } from "react-router-dom";
import { jwtDecode } from "jwt-decode";

const ProtectedRoute = ({ children, roles }) => {
  const token = localStorage.getItem("token");

  // Not logged in
  if (!token) {
    return <Navigate to="/login" replace />;
  }

  try {
    const decoded = jwtDecode(token);

    const userRole =
      decoded.role ||
      decoded.roles ||
      decoded.authorities?.[0]?.replace("ROLE_", "");

    // Role-based protection
    if (roles && !roles.includes(userRole)) {
      // 🔥 FIX: redirect based on role
      if (userRole === "ADMIN") {
        return <Navigate to="/admin/dashboard" replace />;
      }
      return <Navigate to="/dashboard" replace />;
    }

    return children;
  } catch (err) {
    localStorage.removeItem("token");
    return <Navigate to="/login" replace />;
  }
};

export default ProtectedRoute;
