import React, { useEffect, useState } from "react";
import api from "../api/axios";

export default function Invoices() {
  const [invoices, setInvoices] = useState([]);
  const [downloadingId, setDownloadingId] = useState(null);

  useEffect(() => {
    const fetchInvoices = async () => {
      try {
        const res = await api.get("/invoices");
        setInvoices(res.data);
      } catch (err) {
        console.error("Failed to fetch invoices:", err);
      }
    };
    fetchInvoices();
  }, []);

  // ✅ DOWNLOAD / OPEN PDF
  const openPdf = async (id, autoPrint = false) => {
    try {
      setDownloadingId(id);

      const res = await api.get(`/invoices/${id}/pdf`, {
        responseType: "blob",
      });

      const blob = new Blob([res.data], { type: "application/pdf" });
      const url = window.URL.createObjectURL(blob);

      const win = window.open(url, "_blank");

      if (autoPrint && win) {
        win.onload = () => win.print();
      }

      setTimeout(() => window.URL.revokeObjectURL(url), 2000);
    } catch (err) {
      console.error("PDF error:", err);
      alert("Failed to open invoice PDF");
    } finally {
      setDownloadingId(null);
    }
  };

  return (
    <div className="p-6">
      <h2 className="text-2xl font-semibold mb-4">Invoices</h2>

      {invoices.length === 0 ? (
        <p className="text-gray-500">No invoices available.</p>
      ) : (
        <div className="overflow-x-auto">
          <table className="w-full border border-gray-200 rounded-lg">
            <thead className="bg-gray-100 text-left">
              <tr>
                <th className="p-3">ID</th>
                <th className="p-3">Customer</th>
                <th className="p-3">Total</th>
                <th className="p-3">Status</th>
                <th className="p-3 text-center">Actions</th>
              </tr>
            </thead>

            <tbody>
              {invoices.map((inv) => (
                <tr key={inv.id} className="border-t">
                  <td className="p-3">{inv.id}</td>
                  <td className="p-3">
                    {inv.salesOrder?.customer?.name || "N/A"}
                  </td>
                  <td className="p-3">₹ {inv.totalPayable}</td>
                  <td className="p-3">
                    <span
                      className={`px-2 py-1 rounded text-xs ${
                        inv.status === "PAID"
                          ? "bg-green-100 text-green-700"
                          : "bg-yellow-100 text-yellow-700"
                      }`}
                    >
                      {inv.status}
                    </span>
                  </td>
                  <td className="p-3 text-center space-x-2">
                    <button
                      onClick={() => openPdf(inv.id)}
                      disabled={downloadingId === inv.id}
                      className="px-3 py-1 bg-blue-600 text-white rounded hover:bg-blue-700 text-sm"
                    >
                      {downloadingId === inv.id ? "Loading..." : "PDF"}
                    </button>

                    <button
                      onClick={() => openPdf(inv.id, true)}
                      disabled={downloadingId === inv.id}
                      className="px-3 py-1 bg-gray-700 text-white rounded hover:bg-gray-800 text-sm"
                    >
                      Print
                    </button>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      )}
    </div>
  );
}
