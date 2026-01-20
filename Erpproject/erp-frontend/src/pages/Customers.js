import api from "../api/axios";
import { useEffect, useState } from "react";
import { toast } from "react-toastify";

export default function Customers() {

  const [customers, setCustomers] = useState([]);
  const [loading, setLoading] = useState(false);

  // ==========================
  // Load Customers
  // ==========================
  const loadCustomers = async () => {
    try {
      setLoading(true);

      const res = await api.get("/customers");

      // Spring pagination safe
      const data = res.data.content || res.data;

      setCustomers(data);

    } catch (err) {
      console.error(err);
      toast.error("Failed to load customers");
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    loadCustomers();
  }, []);

  // ==========================
  // UI
  // ==========================
  if (loading) {
    return <p>Loading customers...</p>;
  }

  return (
    <div>

      <h2>Customers</h2>

      {customers.length === 0 ? (
        <p>No customers found</p>
      ) : (
        customers.map((c) => (
          <div key={c.id}>
            {c.name}
          </div>
        ))
      )}

    </div>
  );
}
