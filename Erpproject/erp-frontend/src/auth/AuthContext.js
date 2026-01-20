import { createContext, useEffect, useState } from "react";

export const AuthContext = createContext();

export const AuthProvider = ({ children }) => {

  const [token, setToken] = useState(null);
  const [user, setUser] = useState(null);
  const [isAuthenticated, setIsAuthenticated] = useState(false);
  const [loading, setLoading] = useState(true);

  // Decode JWT payload
  const parseJwt = (token) => {
    try {
      const base64Url = token.split(".")[1];
      const base64 = base64Url.replace(/-/g, "+").replace(/_/g, "/");
      return JSON.parse(window.atob(base64));
    } catch {
      return null;
    }
  };

  useEffect(() => {

    const storedToken = localStorage.getItem("token");

    if (storedToken) {
      const decoded = parseJwt(storedToken);

      if (decoded) {
        setToken(storedToken);
        setUser({
          username: decoded.sub,
          role: decoded.role
        });
        setIsAuthenticated(true);
      }
    }

    setLoading(false);

  }, []);

  const login = (jwtToken) => {

    const decoded = parseJwt(jwtToken);

    localStorage.setItem("token", jwtToken);

    setToken(jwtToken);
    setUser({
      username: decoded.sub,
      role: decoded.role
    });

    setIsAuthenticated(true);
  };

  const logout = () => {
    localStorage.clear();
    setToken(null);
    setUser(null);
    setIsAuthenticated(false);
    window.location.href = "/login";
  };

  return (
    <AuthContext.Provider
      value={{
        token,
        user,
        isAuthenticated,
        login,
        logout,
        loading
      }}
    >
      {children}
    </AuthContext.Provider>
  );
};
