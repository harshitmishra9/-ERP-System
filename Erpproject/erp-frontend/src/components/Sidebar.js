import { Link, useLocation } from "react-router-dom";
import { useContext } from "react";
import { AuthContext } from "../auth/AuthContext";

const Sidebar = () => {

  const { auth } = useContext(AuthContext);
  const location = useLocation();

  if (!auth || !auth.role) return null;

  const role = auth.role;

  const isActive = (path) =>
    location.pathname.startsWith(path)
      ? "font-semibold text-blue-600"
      : "text-gray-700";

  return (
    <nav className="flex flex-col gap-3 p-4 border-r min-h-screen">

      {/* DASHBOARD (ALL ROLES) */}
      <Link to="/dashboard" className={isActive("/dashboard")}>
        Dashboard
      </Link>

      {/* PRODUCT MANAGEMENT */}
      {["ADMIN", "INVENTORY_MANAGER"].includes(role) && (
        <Link to="/products" className={isActive("/products")}>
          Products
        </Link>
      )}

      {/* SALES MODULE */}
      {["ADMIN", "SALES_EXECUTIVE"].includes(role) && (
        <Link to="/sales-orders" className={isActive("/sales-orders")}>
          Sales Orders
        </Link>
      )}

      {/* PURCHASE + GRN */}
      {["ADMIN", "PURCHASE_MANAGER"].includes(role) && (
        <>
          <Link to="/purchase-orders" className={isActive("/purchase-orders")}>
            Purchase Orders
          </Link>

          <Link to="/grn" className={isActive("/grn")}>
            GRN
          </Link>
        </>
      )}

      {/* INVOICE + ACCOUNTS */}
      {["ADMIN", "ACCOUNTANT", "SALES_EXECUTIVE"].includes(role) && (
        <Link to="/invoices" className={isActive("/invoices")}>
          Invoices
        </Link>
      )}

    </nav>
  );
};

export default Sidebar;
