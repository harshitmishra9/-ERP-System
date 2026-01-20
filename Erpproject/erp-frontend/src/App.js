import { BrowserRouter, Routes, Route, Navigate } from "react-router-dom";
import { AuthProvider } from "./auth/AuthContext";
import ProtectedRoute from "./auth/ProtectedRoute";

import AuthLayout from "./layouts/AuthLayout";

import Register from "./pages/Register";
import Login from "./pages/Login";
import Dashboard from "./pages/Dashboard";
import AdminDashboard from "./pages/Admindashboard";
import Products from "./pages/Products";
import SalesOrders from "./pages/SalesOrders";
import Invoices from "./pages/Invoices";

function App() {
  return (
    <AuthProvider>
      <BrowserRouter>
        <Routes>

          {/* ================= AUTH ================= */}
          <Route element={<AuthLayout />}>
            <Route path="/login" element={<Login />} />
            <Route path="/register" element={<Register />} />
          </Route>

          <Route path="/" element={<Navigate to="/login" replace />} />

          {/* ================= USER DASHBOARD ================= */}
          <Route
            path="/dashboard"
            element={
              <ProtectedRoute roles={["USER", "INVENTORY_MANAGER"]}>
                <Dashboard />
              </ProtectedRoute>
            }
          >
            <Route
              path="orders"
              element={
                <ProtectedRoute roles={["USER", "INVENTORY_MANAGER"]}>
                  <SalesOrders />
                </ProtectedRoute>
              }
            />

            <Route
              path="invoices"
              element={
                <ProtectedRoute roles={["USER", "ACCOUNTANT"]}>
                  <Invoices />
                </ProtectedRoute>
              }
            />

            <Route
              path="products"
              element={
                <ProtectedRoute roles={["INVENTORY_MANAGER"]}>
                  <Products />
                </ProtectedRoute>
              }
            />
          </Route>

          {/* ================= ADMIN DASHBOARD ================= */}
          <Route
            path="/admin/dashboard"
            element={
              <ProtectedRoute roles={["ADMIN"]}>
                <AdminDashboard />
              </ProtectedRoute>
            }
          >
            <Route
              path="orders"
              element={
                <ProtectedRoute roles={["ADMIN"]}>
                  <SalesOrders />
                </ProtectedRoute>
              }
            />

            <Route
              path="invoices"
              element={
                <ProtectedRoute roles={["ADMIN"]}>
                  <Invoices />
                </ProtectedRoute>
              }
            />

            <Route
              path="products"
              element={
                <ProtectedRoute roles={["ADMIN"]}>
                  <Products />
                </ProtectedRoute>
              }
            />
          </Route>

          {/* ================= FALLBACK ================= */}
          <Route path="*" element={<Navigate to="/login" replace />} />

        </Routes>
      </BrowserRouter>
    </AuthProvider>
  );
}

export default App;
