import api from "../api/axios";

export default function GRN() {
  const submitGRN = async () => {
    await api.post("/grns", { purchaseOrderId: 1 });
    alert("Stock Updated");
  };

  return <button onClick={submitGRN}>Create GRN</button>;
}
