# 📨 Notification System – Java Backend Developer Assignment

This project implements an Event Notification System capable of handling different types of events (EMAIL, SMS, PUSH) using multithreading, REST APIs, and Docker. The system simulates asynchronous event processing and calls a callback URL upon completion.

---

## 🚀 Technologies Used

- Java 17  
- Spring Boot 3.x  
- Spring Web & WebFlux (for WebClient)  
- Docker  
- Maven  

---

## 🧪 Features

- REST API to accept and queue event requests  
- Event processing via separate threads per event type  
- Simulated random processing delay and failure  
- Asynchronous callback posting with status (`COMPLETED` or `FAILED`)  
- Graceful shutdown of processing threads  
- Dockerized for easy deployment  

---

## 📦 API Endpoint

**POST** `/api/events`

### Request Body:
```json
{
  "eventType": "EMAIL",
  "payload": {
    "recipient": "test@example.com",
    "message": "Hello!"
  },
  "callbackUrl": "https://your-callback-url.com"
}
