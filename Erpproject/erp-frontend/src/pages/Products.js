import api from "../api/axios";
import { useEffect, useState } from "react";
import { toast } from "react-toastify";

export default function Products() {
  const [products, setProducts] = useState([]);
  const [loading, setLoading] = useState(false);

  const loadProducts = async () => {
    try {
      setLoading(true);
      const res = await api.get("/products");
      setProducts(res.data.content || res.data);
    } catch {
      toast.error("Failed to load products");
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    loadProducts();
  }, []);

  if (loading) {
    return <p className="text-gray-500">Loading products...</p>;
  }

  return (
    <div className="p-6">
      <h2 className="text-2xl font-semibold mb-4">Products</h2>

      {products.length === 0 ? (
        <p className="text-gray-500">No products found</p>
      ) : (
        <div className="overflow-x-auto">
          <table className="w-full border rounded-lg overflow-hidden">
            <thead className="bg-gray-100">
              <tr>
                <th className="p-3 text-left">Name</th>
                <th className="p-3 text-left">Stock</th>
                <th className="p-3 text-left">Price</th>
              </tr>
            </thead>

            <tbody>
              {products.map((p) => (
                <tr key={p.id} className="border-t hover:bg-gray-50">
                  <td className="p-3">{p.name}</td>
                  <td className="p-3">{p.currentStock}</td>
                  <td className="p-3">₹ {p.unitPrice}</td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      )}
    </div>
  );
}
