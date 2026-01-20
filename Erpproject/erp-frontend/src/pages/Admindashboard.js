import { useEffect, useState, useCallback } from "react";
import { Link, useLocation, Outlet } from "react-router-dom";
import { BarChart, Bar, XAxis, Tooltip, ResponsiveContainer } from "recharts";
import {
  TrendingUp,
  ShoppingCart,
  Users,
  RefreshCcw,
  Moon,
  Sun,
  Download,
  FileCheck,
  ChevronLeft,
  ChevronRight
} from "lucide-react";
import { motion } from "framer-motion";
import api from "../api/axios";

export default function AdminDashboard() {
  const [summary, setSummary] = useState({});
  const [chartData, setChartData] = useState([]);
  const [loading, setLoading] = useState(false);
  const [darkMode, setDarkMode] = useState(false);
  const [sidebarOpen, setSidebarOpen] = useState(true);
  const [filter, setFilter] = useState("THIS_MONTH");
  const location = useLocation();

  const loadDashboard = useCallback(async () => {
    try {
      setLoading(true);

      const summaryRes = await api.get(
        `/admin/dashboard?filter=${filter}`
      );

      const chartRes = await api.get(
        `/admin/dashboard/sales-chart?filter=${filter}`
      );

      setSummary(summaryRes.data || {});
      setChartData(Array.isArray(chartRes.data) ? chartRes.data : []);

    } catch (err) {
      console.error("Admin dashboard load failed", err);
    } finally {
      setLoading(false);
    }
  }, [filter]);

  useEffect(() => {
    loadDashboard();
  }, [loadDashboard]);

  const downloadReport = async () => {
    try {
      const res = await api.get(
        `/admin/dashboard/report?filter=${filter}`,
        { responseType: "blob" }
      );

      const blob = new Blob([res.data], { type: "application/pdf" });
      const url = window.URL.createObjectURL(blob);
      const link = document.createElement("a");
      link.href = url;
      link.download = "admin-dashboard-report.pdf";
      link.click();

    } catch (err) {
      console.error("PDF download failed", err);
    }
  };

  // ✅ USERS REMOVED FROM SIDEBAR
  const sidebarItems = [
    { label: "Dashboard", path: "/admin/dashboard" },
    { label: "Orders", path: "/admin/dashboard/orders" },
    { label: "Invoices", path: "/admin/dashboard/invoices" },
    { label: "Products", path: "/admin/dashboard/products" }
  ];

  return (
    <div className={`${darkMode ? "bg-slate-900 text-white" : "bg-slate-100 text-black"} min-h-screen flex`}>

      {/* SIDEBAR */}
      <motion.div
        animate={{ width: sidebarOpen ? 240 : 70 }}
        className="bg-slate-800 text-white h-screen p-4 flex flex-col"
      >
        <div className="flex justify-between items-center mb-6">
          {sidebarOpen && <h2 className="font-bold">Admin ERP</h2>}
          <button onClick={() => setSidebarOpen(!sidebarOpen)}>
            {sidebarOpen ? <ChevronLeft /> : <ChevronRight />}
          </button>
        </div>

        <nav className="flex flex-col gap-4 text-sm">
          {sidebarItems.map((item) => (
            <Link
              key={item.label}
              to={item.path}
              className={`px-3 py-2 rounded transition-colors ${
                location.pathname === item.path
                  ? "bg-slate-600 font-semibold"
                  : "hover:bg-slate-700"
              }`}
            >
              {sidebarOpen ? item.label : item.label[0]}
            </Link>
          ))}
        </nav>
      </motion.div>

      {/* MAIN CONTENT */}
      <div className="flex-1 p-6">

        {/* HEADER */}
        <div className="flex justify-between items-center mb-6">
          <div>
            <h1 className="text-2xl font-semibold">Admin Dashboard</h1>
            <p className="text-sm opacity-70">Full system overview</p>
          </div>

          <div className="flex gap-3">
            <select
              className={`border rounded px-3 py-1 ${darkMode ? "bg-slate-700" : "bg-white"}`}
              value={filter}
              onChange={(e) => setFilter(e.target.value)}
            >
              <option value="THIS_MONTH">This Month</option>
              <option value="LAST_MONTH">Last Month</option>
            </select>

            <button
              onClick={() => setDarkMode(!darkMode)}
              className="px-3 py-1 border rounded"
            >
              {darkMode ? <Sun size={16} /> : <Moon size={16} />}
            </button>

            <button
              onClick={downloadReport}
              className="flex items-center gap-2 px-3 py-1 border rounded"
            >
              <Download size={16} /> Export
            </button>

            <button
              onClick={loadDashboard}
              className="flex items-center gap-2 px-3 py-1 border rounded"
            >
              <RefreshCcw size={16} /> Refresh
            </button>
          </div>
        </div>

        {/* KPI CARDS */}
        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-5 mb-8">
          {loading ? (
            <SkeletonCards />
          ) : (
            <>
              <StatCard title="Total Sales" value={`₹ ${summary.totalSales || 0}`} icon={<TrendingUp />} />
              <StatCard title="Total Purchases" value={`₹ ${summary.totalPurchases || 0}`} icon={<ShoppingCart />} />
              <StatCard title="Pending Invoices" value={summary.pendingInvoices || 0} icon={<FileCheck />} />
              <StatCard title="Total Users" value={summary.totalUsers || 0} icon={<Users />} />
            </>
          )}
        </div>

        {/* SALES CHART */}
        <div className="bg-white rounded-2xl shadow-md p-6 mb-10">
          <h2 className="text-lg font-semibold mb-3">Sales Summary</h2>

          {loading ? (
            <SkeletonChart />
          ) : chartData.length > 0 ? (
            <ResponsiveContainer width="100%" height={320}>
              <BarChart data={chartData}>
                <XAxis dataKey="date" />
                <Tooltip />
                <Bar dataKey="totalSales" fill="#3b82f6" />
              </BarChart>
            </ResponsiveContainer>
          ) : (
            <p className="text-center opacity-60">No sales data available</p>
          )}
        </div>

        {/* SUB ROUTES */}
        <Outlet />

      </div>
    </div>
  );
}

// ================= COMPONENTS =================

const StatCard = ({ title, value, icon }) => (
  <div className="bg-white rounded-xl shadow p-5 flex justify-between items-center">
    <div>
      <p className="text-sm opacity-70">{title}</p>
      <h3 className="text-xl font-bold mt-1">{value}</h3>
    </div>
    <div className="bg-slate-100 p-3 rounded-xl">{icon}</div>
  </div>
);

const SkeletonCards = () => (
  <>
    {[1, 2, 3, 4].map((i) => (
      <div key={i} className="h-24 bg-slate-200 animate-pulse rounded-xl"></div>
    ))}
  </>
);

const SkeletonChart = () => (
  <div className="h-72 bg-slate-200 animate-pulse rounded-xl"></div>
);
