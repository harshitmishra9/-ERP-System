import api from "../api/axios";
import { useEffect, useState } from "react";

export default function Suppliers() {
  const [data, setData] = useState([]);
  useEffect(() => {
    api.get("/suppliers").then(r => setData(r.data));
  }, []);

  return data.map(c => <div key={c.id}>{c.name}</div>);
}
