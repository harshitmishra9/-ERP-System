ERP System – Spring Boot Backend
📌 Project Overview
This project is a Spring Boot–based ERP (Enterprise Resource Planning) backend system that provides RESTful APIs for managing core business operations including products, suppliers, customers, purchase orders, sales orders, invoices, GRNs, and dashboards.
The application follows clean layered architecture and exposes APIs documented using Swagger (OpenAPI 3).
________________________________________
🚀 Key Features
•	RESTful API architecture
•	Swagger / OpenAPI documentation
•	Product Management
•	Supplier & Customer Management
•	Purchase & Sales Orders
•	Goods Receipt Notes (GRN)
•	Invoice Management
•	PDF Generation
•	Dashboard APIs
•	Modular and scalable structure
________________________________________
🛠️ Tech Stack
•	Java
•	Spring Boot
•	Spring Web
•	Spring Data JPA
•	Hibernate
•	Swagger / OpenAPI 3
•	Maven
•	MySQL / PostgreSQL
________________________________________
📂 Project Structure
src/
 └── main/
     ├── java/com/example/Erp/Project/
     │   ├── Config/
     │   ├── Controller/
     │   ├── DTO/
     │   ├── Entity/
     │   ├── Repository/
     │   ├── Service/
     │   └── ServiceImpl/
     └── resources/
         └── application.properties
 └── test/
________________________________________
⚙️ Application Configuration
Update database and application settings in:
src/main/resources/application.properties
Example configuration:
server.port=8080

spring.datasource.url=jdbc:mysql://localhost:3306/erp_db
spring.datasource.username=root
spring.datasource.password=password

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQL8Dialect
________________________________________
▶️ Running the Application
Using Maven
mvn clean install
mvn spring-boot:run
Using IDE
Run the main class:
ErpProjectApplication.java
________________________________________
🧾 Swagger API Documentation
This project uses Swagger UI to document and test all REST APIs.
🔗 Swagger UI URL
Once the application is running, open:
http://localhost:8080/swagger-ui.html
or
http://localhost:8080/swagger-ui/index.html
________________________________________
📸 Swagger UI – Overview
 
•	Displays all available ERP APIs
•	APIs grouped by controllers/modules
•	Shows HTTP methods (GET, POST, PUT, DELETE)
________________________________________
📸 Swagger UI – Endpoint Details
 
Each endpoint shows:
•	Request parameters
•	Request body schema
•	Response structure
•	HTTP status codes
________________________________________
📸 Swagger UI – Try It Out
 
•	Allows executing APIs directly from browser
•	Supports request body input
•	Displays real-time API responses
________________________________________
🗂️ Swagger Features Used
•	OpenAPI 3 specification
•	Auto-generated schemas from DTOs
•	Controller-based tagging
•	Request & response examples
📌 Place all screenshots inside a screenshots/ folder at project root.
________________________________________
📦 Main API Modules
•	Admin / Auth
•	Dashboard
•	Products
•	Suppliers
•	Customers
•	Purchase Orders
•	Sales Orders
•	GRN
•	Invoices
•	PDF Generation
________________________________________
🧪 Testing
Run unit tests using:
mvn test
________________________________________
📈 Future Improvements
•	JWT-based authentication
•	Role-based authorization
•	Frontend integration
•	Inventory analytics
•	Multi-tenant ERP support
________________________________________
🤝 Contributing
1.	Fork the repository
2.	Create a feature branch
3.	Commit your changes
4.	Submit a pull request.
________________________________________
👨‍💻 Author
ERP Backend System developed using Spring Boot and Swagger OpenAPI.


Frontend API Integration Guide
This guide explains how frontend applications (React, Angular, Vue, etc.) can integrate with the ERP Spring Boot Backend APIs using REST and Swagger documentation.
________________________________________
🌐 Base API Information
🔗 Base URL
http://localhost:8080
All API endpoints are relative to this base URL.
Example:
GET http://localhost:8080/api/products
________________________________________
📘 API Documentation Reference
Use Swagger UI to explore and test APIs:
http://localhost:8080/swagger-ui/index.html
Swagger provides:
•	Endpoint URLs
•	HTTP methods
•	Request body formats
•	Response schemas
•	Status codes
Frontend developers should always refer to Swagger as the source of truth.
________________________________________
🔐 Authentication (If Enabled)
If JWT or session-based authentication is implemented.
Login API (Example)
POST /api/auth/login
Request Body
{
  "username": "admin",
  "password": "password"
}
Response
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
}
Sending Token in Requests
Include the token in request headers:
Authorization: Bearer <JWT_TOKEN>
________________________________________
📦 Common API Modules & Usage
🧾 Products API
Get All Products
GET /api/products
Response:
[
  {
    "id": 1,
    "name": "Laptop",
    "price": 75000,
    "quantity": 10
  }
]
Create Product
POST /api/products
Request Body:
{
  "name": "Keyboard",
  "price": 1500,
  "quantity": 50
}
________________________________________
👥 Customers API
Get Customers
GET /api/customers
Create Customer
POST /api/customers
{
  "name": "John Doe",
  "email": "john@example.com",
  "phone": "9876543210"
}
________________________________________
🏭 Suppliers API
GET /api/suppliers
POST /api/suppliers
PUT /api/suppliers/{id}
DELETE /api/suppliers/{id}
________________________________________
📦 Purchase Orders
POST /api/purchase-orders
GET /api/purchase-orders/{id}
________________________________________
🧾 Sales Orders
POST /api/sales-orders
GET /api/sales-orders/{id}
________________________________________
🧾 Invoices
GET /api/invoices
GET /api/invoices/{id}
________________________________________
📊 Dashboard APIs
GET /api/dashboard/summary
Example response:
{
  "totalProducts": 120,
  "totalCustomers": 45,
  "monthlySales": 350000
}
________________________________________
⚛️ Example: React Integration (Axios)
Axios Setup
import axios from "axios";

const api = axios.create({
  baseURL: "http://localhost:8080",
});

api.interceptors.request.use((config) => {
  const token = localStorage.getItem("token");
  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
  }
  return config;
});

export default api;
________________________________________
Fetch Products
import api from "./api";

export const getProducts = async () => {
  const response = await api.get("/api/products");
  return response.data;
};
________________________________________
Create Product
export const createProduct = async (product) => {
  const response = await api.post("/api/products", product);
  return response.data;
};
________________________________________
🧪 Error Handling
Typical error response:
{
  "timestamp": "2026-01-21T10:30:00",
  "status": 400,
  "error": "Bad Request",
  "message": "Validation failed"
}
Frontend should handle:
•	400 → Validation errors
•	401 → Unauthorized
•	403 → Forbidden
•	404 → Not found
•	500 → Server error
________________________________________
📁 Recommended Frontend Folder Structure
src/
 ├── api/
 │   ├── auth.js
 │   ├── products.js
 │   ├── customers.js
 │   └── orders.js
 ├── components/
 ├── pages/
 └── services/
________________________________________
✅ Best Practices
•	Always validate data before sending
•	Handle API errors gracefully
•	Use environment variables for base URLs
•	Sync frontend models with backend DTOs
•	Refer to Swagger for latest changes
________________________________________
🚀 Next Steps
•	Add JWT authentication support
•	Enable CORS configuration
•	Create Postman collection
•	Implement frontend role-based UI
________________________________________
📞 Support
Contact on email: hm4554126@gmail.com
Admin: Harshit Mishra
________________________________________
Happy Coding 🚀

