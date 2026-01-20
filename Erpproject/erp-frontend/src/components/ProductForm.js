import { useForm } from "react-hook-form";
import { yupResolver } from "@hookform/resolvers/yup";
import * as yup from "yup";

const schema = yup.object({
  name: yup.string().required("Name is required"),
  sku: yup.string().required("SKU is required"),
  unitPrice: yup
    .number()
    .typeError("Price must be number")
    .positive("Must be positive")
    .required("Price required"),
  currentStock: yup
    .number()
    .typeError("Stock must be number")
    .min(0, "Cannot be negative")
    .required("Stock required"),
});

export default function ProductForm({ onSubmit, loading }) {

  const {
    register,
    handleSubmit,
    reset,
    formState: { errors },
  } = useForm({
    resolver: yupResolver(schema),
  });

  const submitHandler = async (data) => {
    await onSubmit(data);
    reset();
  };

  return (
    <form onSubmit={handleSubmit(submitHandler)}>

      <input {...register("name")} placeholder="Name" />
      <p style={{ color: "red" }}>{errors.name?.message}</p>

      <input {...register("sku")} placeholder="SKU" />
      <p style={{ color: "red" }}>{errors.sku?.message}</p>

      <input type="number" {...register("unitPrice")} placeholder="Price" />
      <p style={{ color: "red" }}>{errors.unitPrice?.message}</p>

      <input type="number" {...register("currentStock")} placeholder="Stock" />
      <p style={{ color: "red" }}>{errors.currentStock?.message}</p>

      <button disabled={loading}>
        {loading ? "Saving..." : "Save Product"}
      </button>

    </form>
  );
}
