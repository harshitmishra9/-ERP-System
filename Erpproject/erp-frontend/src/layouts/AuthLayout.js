import { Outlet } from "react-router-dom";

const AuthLayout = () => {
  return (
    <div className="min-h-screen flex items-center justify-center bg-slate-100">
      <div className="w-full max-w-md bg-white p-8 rounded-xl shadow-lg">
        <h1 className="text-2xl font-bold text-center mb-6">
          Welcome to ERP System
        </h1>

        {/* Login / Register will render here */}
        <Outlet />
      </div>
    </div>
  );
};

export default AuthLayout;
