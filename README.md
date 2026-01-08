# 🛒 Cartify – Full-Stack E-Commerce Application

A **production-grade full-stack e-commerce platform** built using **Spring Boot** and **Angular**, designed with real-world architecture, security, payments, and real-time features.

This project goes beyond basic CRUD and demonstrates **end-to-end system design**, **secure authentication**, **payment processing**, and **real-time updates**.

---

## 🚀 Tech Stack

### 🔧 Backend
- Java 17  
- Spring Boot  
- Spring Security + JWT  
- REST APIs  
- WebSockets (STOMP)  
- Stripe Payments  
- MySQL  
- Hibernate / JPA  

### 🎨 Frontend
- Angular  
- TypeScript  
- SCSS  
- RxJS  
- Reactive Forms  

### 🔗 Integrations
- Stripe (Payments & Webhooks)  
- Cloudinary (Image Storage)  
- Email Notifications  
- WhatsApp Notifications  
- WebSocket-based Live Updates  

---

## ⭐ Features

### 🔐 Secure Authentication & Authorization
- JWT-based authentication  
- Role-based access control:
  - Buyer  
  - Seller  
  - Admin  
- Protected routes on both frontend and backend  
- Stateless, production-ready security design  

---

### 🛍️ Real-Time Product & Stock Updates
- WebSocket (STOMP) integration  
- Live stock updates across all active clients  
- Prevents overselling  
- Mimics real e-commerce platforms like Amazon / Flipkart  

---

### 🛒 Intelligent Cart System
- Add / update / remove cart items  
- Automatic total recalculation  
- Cart persistence per user  
- Server-side validation to prevent price tampering  

---

### 💳 Secure Stripe Checkout Integration
- Stripe Checkout Sessions  
- Server-side payment intent creation  
- Secure webhook handling for payment confirmation  
- Order completion triggered **only after webhook verification**  
- Prevents fake or client-side payment confirmations  

---

### 📦 Order Management System
- Orders created in **PENDING** state  
- Status updated to **COMPLETE** only after Stripe webhook success  
- Order history per user  
- Detailed order view with items and totals  

---

### 🔔 Post-Payment Notifications
- Automatic email confirmation on successful payment  
- WhatsApp notification sent after order completion  
- Simulates real-world customer experience  

---

### ⚡ WebSocket-Driven Architecture
- Live stock broadcasting:
/topic/products/{productId}/stock

- Extensible order status updates  
- Scalable pub-sub design  

---

### 🛡️ Centralized Global Exception Handling
- `@RestControllerAdvice`-based error handling  
- Meaningful HTTP status codes  
- Clean error responses for frontend consumption  
- Graceful handling of:
- Validation errors  
- Authentication errors  
- Business logic violations  

---

### ☁️ Cloud-Ready Configuration
- All secrets externalized using environment variables  
- No credentials hardcoded  
- Ready for deployment on:
- Railway  
- Render  
- Docker-based platforms  

---

## 🧠 Architecture Highlights
- Layered backend architecture (Controller → Service → Repository)  
- Stateless JWT authentication  
- Event-driven payment confirmation via Stripe Webhooks  
- Real-time communication using WebSockets  
- Clean separation of concerns (frontend / backend)  

---

## 🧪 Running Locally

### Backend
```bash
cd backend
mvn spring-boot:run
```

### Frontend
```bash
cd frontend
npm install
ng serve
```

**📸 Screenshots & Demo**
**Login**

<img width="1200" alt="login" src="https://github.com/user-attachments/assets/ba5c5601-65fc-4833-8968-ba41b41ed586" />
**Buyer Dashboard**

<img width="1200" alt="buyer_dashboard" src="https://github.com/user-attachments/assets/37453519-0ef3-48fd-bc78-af699ff02a65" />
**Admin Dashboard**

<img width="1200" alt="admin_dashboard" src="https://github.com/user-attachments/assets/401a2139-eb22-4d27-88c4-6174203d8593" />
**Seller Dashboard**

<img width="1200" alt="seller_dashboard" src="https://github.com/user-attachments/assets/cf4a4985-a49c-4dd8-936e-447a303cb24a" />
**Payment Success**

<img width="1200" alt="buyer_payment_success" src="https://github.com/user-attachments/assets/ea0d0460-d302-4f20-8bc8-5f1d8e4f5385" />

👩‍💻 Author

Shreya Galurgi

Full-Stack Software Engineer

Java • Spring Boot • Angular • System Design
