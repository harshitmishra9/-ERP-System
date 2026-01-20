import api from "../api/axios";

export default function PurchaseOrders() {
  return <button onClick={() => api.post("/purchase-orders")}>Create PO</button>;
}
