import api from "../api/axios";
import { useEffect, useState } from "react";
import { toast } from "react-toastify";

export default function SalesOrders() {
  const [customers, setCustomers] = useState([]);
  const [products, setProducts] = useState([]);

  const [customerId, setCustomerId] = useState("");
  const [productId, setProductId] = useState("");
  const [quantity, setQuantity] = useState(1);

  const [items, setItems] = useState([]);
  const [loading, setLoading] = useState(false);

  useEffect(() => {
    loadData();
  }, []);

  const loadData = async () => {
    try {
      const c = await api.get("/customers");
      const p = await api.get("/products");
      setCustomers(c.data.content || c.data);
      setProducts(p.data.content || p.data);
    } catch {
      toast.error("Failed to load customers/products");
    }
  };

  const addItem = () => {
    if (!productId || quantity <= 0) {
      toast.error("Invalid product or quantity");
      return;
    }

    const product = products.find(p => p.id === Number(productId));
    if (!product) return;

    if (items.some(i => i.productId === product.id)) {
      toast.error("Product already added");
      return;
    }

    setItems([
      ...items,
      {
        productId: product.id,
        productName: product.name,
        quantity: Number(quantity),
        unitPrice: product.unitPrice
      }
    ]);

    setProductId("");
    setQuantity(1);
  };

  const createOrder = async () => {
    if (!customerId || items.length === 0) {
      toast.error("Customer and items required");
      return;
    }

    try {
      setLoading(true);
      await api.post("/sales-orders", {
        customer: { id: Number(customerId) },
        items: items.map(i => ({
          product: { id: i.productId },
          quantity: i.quantity
        }))
      });

      toast.success("Sales Order Created");
      setItems([]);
      setCustomerId("");
    } catch (err) {
      toast.error("Order creation failed");
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="p-6 max-w-3xl mx-auto">
      <h2 className="text-2xl font-semibold mb-6">Create Sales Order</h2>

      {/* CUSTOMER */}
      <select
        className="w-full border rounded p-2 mb-4"
        value={customerId}
        onChange={e => setCustomerId(e.target.value)}
      >
        <option value="">Select Customer</option>
        {customers.map(c => (
          <option key={c.id} value={c.id}>{c.name}</option>
        ))}
      </select>

      {/* PRODUCT */}
      <div className="flex gap-3 mb-4">
        <select
          className="flex-1 border rounded p-2"
          value={productId}
          onChange={e => setProductId(e.target.value)}
        >
          <option value="">Select Product</option>
          {products.map(p => (
            <option key={p.id} value={p.id}>
              {p.name} (Stock: {p.currentStock})
            </option>
          ))}
        </select>

        <input
          type="number"
          min="1"
          className="w-24 border rounded p-2"
          value={quantity}
          onChange={e => setQuantity(e.target.value)}
        />

        <button
          onClick={addItem}
          className="bg-blue-600 text-white px-4 rounded hover:bg-blue-700"
        >
          Add
        </button>
      </div>

      {/* ITEMS */}
      <div className="bg-white border rounded p-4 mb-4">
        {items.length === 0 ? (
          <p className="text-gray-500">No items added</p>
        ) : (
          items.map((i, idx) => (
            <div key={idx} className="flex justify-between border-b py-1">
              <span>{i.productName}</span>
              <span>Qty: {i.quantity}</span>
            </div>
          ))
        )}
      </div>

      <button
        onClick={createOrder}
        disabled={loading}
        className="w-full bg-green-600 text-white py-2 rounded hover:bg-green-700"
      >
        {loading ? "Saving..." : "Submit Order"}
      </button>
    </div>
  );
}
