# Complete Postman Testing Guide for AITools Application

## Overview
This guide will help you test all endpoints of the AITools application using Postman.

---

## 1. Admin Registration and Login

### 1.1 Register a New Admin
**POST** `http://localhost:8080/auth/admin/register`

**Headers:**
```
Content-Type: application/json
```

**Body (raw JSON):**
```json
{
    "name": "admin1",
    "password": "admin123"
}
```

**Expected Response (200 OK):**
```json
{
    "message": "Admin registered successfully",
    "username": "admin1",
    "id": "1"
}
```

---

### 1.2 Login as Admin
**POST** `http://localhost:8080/auth/admin/login`

**Headers:**
```
Content-Type: application/json
```

**Body (raw JSON):**
```json
{
    "name": "admin1",
    "password": "admin123"
}
```

**Expected Response (200 OK):**
```json
{
    "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
    "message": "Login successful",
    "username": "admin1",
    "adminId": "1"
}
```

**⚠️ IMPORTANT:** Copy the `token` value from the response. You'll need it for authenticated requests!

---

## 2. AI Tools Management (Admin Only - Requires JWT Token)

### 2.1 Setup Authorization for Protected Routes

For all endpoints marked as "Admin Only", you need to add the JWT token:

1. In Postman, go to the **Authorization** tab
2. Select **Type**: Bearer Token
3. Paste the token you received from login in the **Token** field

---

### 2.2 Create an AI Tool (Admin Only)
**POST** `http://localhost:8080/api/aitools`

**Authorization:** Bearer Token (paste your JWT token)

**Headers:**
```
Content-Type: application/json
```

**Body (raw JSON):**
```json
{
    "name": "ChatGPT",
    "description": "Advanced AI chatbot by OpenAI",
    "url": "https://chat.openai.com",
    "category": "Chatbot",
    "pricing": "Free/Premium"
}
```

**Expected Response (201 Created):**
```json
{
    "message": "AI Tool created successfully",
    "tool": {
        "id": 1,
        "name": "ChatGPT",
        "description": "Advanced AI chatbot by OpenAI",
        "url": "https://chat.openai.com",
        "category": "Chatbot",
        "pricing": "Free/Premium"
    }
}
```

---

### 2.3 Get All AI Tools (Public - No Auth Required)
**GET** `http://localhost:8080/api/aitools`

**Authorization:** No Auth

**Expected Response (200 OK):**
```json
[
    {
        "id": 1,
        "name": "ChatGPT",
        "description": "Advanced AI chatbot by OpenAI",
        "url": "https://chat.openai.com",
        "category": "Chatbot",
        "pricing": "Free/Premium"
    }
]
```

---

### 2.4 Get AI Tool by ID (Public - No Auth Required)
**GET** `http://localhost:8080/api/aitools/1`

**Authorization:** No Auth

**Expected Response (200 OK):**
```json
{
    "id": 1,
    "name": "ChatGPT",
    "description": "Advanced AI chatbot by OpenAI",
    "url": "https://chat.openai.com",
    "category": "Chatbot",
    "pricing": "Free/Premium"
}
```

---

### 2.5 Get My AI Tools (Admin Only)
**GET** `http://localhost:8080/api/aitools/my-tools`

**Authorization:** Bearer Token (paste your JWT token)

**Expected Response (200 OK):**
```json
[
    {
        "id": 1,
        "name": "ChatGPT",
        "description": "Advanced AI chatbot by OpenAI",
        "url": "https://chat.openai.com",
        "category": "Chatbot",
        "pricing": "Free/Premium"
    }
]
```

---

### 2.6 Update an AI Tool (Admin Only)
**PUT** `http://localhost:8080/api/aitools/1`

**Authorization:** Bearer Token (paste your JWT token)

**Headers:**
```
Content-Type: application/json
```

**Body (raw JSON):**
```json
{
    "name": "ChatGPT Plus",
    "description": "Advanced AI chatbot with GPT-4",
    "url": "https://chat.openai.com",
    "category": "Chatbot",
    "pricing": "Premium"
}
```

**Expected Response (200 OK):**
```json
{
    "message": "AI Tool updated successfully",
    "tool": {
        "id": 1,
        "name": "ChatGPT Plus",
        "description": "Advanced AI chatbot with GPT-4",
        "url": "https://chat.openai.com",
        "category": "Chatbot",
        "pricing": "Premium"
    }
}
```

---

### 2.7 Delete an AI Tool (Admin Only)
**DELETE** `http://localhost:8080/api/aitools/1`

**Authorization:** Bearer Token (paste your JWT token)

**Expected Response (200 OK):**
```json
{
    "message": "AI Tool deleted successfully",
    "deletedId": "1"
}
```

---

## 3. Reviews Management

### 3.1 Submit a Review (Public - No Auth Required)
**POST** `http://localhost:8080/api/reviews`

**Authorization:** No Auth

**Headers:**
```
Content-Type: application/json
```

**Body (raw JSON):**
```json
{
    "aiToolId": 1,
    "content": "This is an amazing AI tool! Very helpful for my work.",
    "rating": 4.5,
    "username": "john_doe"
}
```

**Expected Response (201 Created):**
```json
{
    "message": "Review submitted successfully. It will be visible after admin approval.",
    "reviewId": 1,
    "status": "PENDING"
}
```

---

### 3.2 Get Approved Reviews for an AI Tool (Public - No Auth Required)
**GET** `http://localhost:8080/api/reviews/aitool/1`

**Authorization:** No Auth

**Expected Response (200 OK):**
```json
[
    {
        "id": 1,
        "content": "This is an amazing AI tool! Very helpful for my work.",
        "rating": 4.5,
        "username": "john_doe",
        "status": "APPROVED",
        "aiToolId": 1,
        "aiToolName": "ChatGPT"
    }
]
```

---

### 3.3 Get All Approved Reviews (Public - No Auth Required)
**GET** `http://localhost:8080/api/reviews/approved`

**Authorization:** No Auth

**Expected Response (200 OK):**
```json
[
    {
        "id": 1,
        "content": "This is an amazing AI tool! Very helpful for my work.",
        "rating": 4.5,
        "username": "john_doe",
        "status": "APPROVED",
        "aiToolId": 1,
        "aiToolName": "ChatGPT"
    }
]
```

---

### 3.4 Get All Pending Reviews (Admin Only)
**GET** `http://localhost:8080/api/reviews/pending`

**Authorization:** Bearer Token (paste your JWT token)

**Expected Response (200 OK):**
```json
[
    {
        "id": 2,
        "content": "Great tool for productivity!",
        "rating": 5.0,
        "username": "jane_smith",
        "status": "PENDING",
        "aiToolId": 1,
        "aiToolName": "ChatGPT"
    }
]
```

---

### 3.5 Approve a Review (Admin Only)
**PUT** `http://localhost:8080/api/reviews/2/approve`

**Authorization:** Bearer Token (paste your JWT token)

**Expected Response (200 OK):**
```json
{
    "message": "Review approved successfully",
    "reviewId": 2,
    "status": "APPROVED"
}
```

---

### 3.6 Reject a Review (Admin Only)
**PUT** `http://localhost:8080/api/reviews/3/reject`

**Authorization:** Bearer Token (paste your JWT token)

**Expected Response (200 OK):**
```json
{
    "message": "Review rejected successfully",
    "reviewId": 3,
    "status": "REJECTED"
}
```

---

### 3.7 Delete a Review (Admin Only)
**DELETE** `http://localhost:8080/api/reviews/3`

**Authorization:** Bearer Token (paste your JWT token)

**Expected Response (200 OK):**
```json
{
    "message": "Review deleted successfully",
    "deletedId": "3"
}
```

---

### 3.8 Get Review by ID (Admin Only)
**GET** `http://localhost:8080/api/reviews/1`

**Authorization:** Bearer Token (paste your JWT token)

**Expected Response (200 OK):**
```json
{
    "id": 1,
    "content": "This is an amazing AI tool! Very helpful for my work.",
    "rating": 4.5,
    "username": "john_doe",
    "status": "APPROVED",
    "aiToolId": 1,
    "aiToolName": "ChatGPT"
}
```

---

## 4. Complete Testing Workflow

### Step-by-Step Testing Sequence:

1. **Register an Admin**
   - POST `/auth/admin/register`
   - Use: `admin1` / `admin123`

2. **Login as Admin**
   - POST `/auth/admin/login`
   - Copy the JWT token from response

3. **Create AI Tools** (with token)
   - POST `/api/aitools`
   - Create 2-3 different tools

4. **View All AI Tools** (no token needed)
   - GET `/api/aitools`
   - Verify tools are listed

5. **Submit Reviews** (no token needed)
   - POST `/api/reviews`
   - Submit 3-4 reviews for different tools

6. **View Pending Reviews** (with token)
   - GET `/api/reviews/pending`
   - You should see all submitted reviews

7. **Approve Some Reviews** (with token)
   - PUT `/api/reviews/{id}/approve`
   - Approve 2-3 reviews

8. **View Approved Reviews** (no token needed)
   - GET `/api/reviews/approved`
   - GET `/api/reviews/aitool/{id}`

9. **Update an AI Tool** (with token)
   - PUT `/api/aitools/{id}`

10. **Get My Tools** (with token)
    - GET `/api/aitools/my-tools`

---

## 5. Common Issues and Solutions

### Issue 1: 403 Forbidden on Login
**Solution:** The endpoints are now fixed. Make sure you're using the correct URL and the server is running.

### Issue 2: Token Not Working
**Solution:** 
- Make sure you copied the entire token
- Check that Authorization type is set to "Bearer Token"
- The token expires after some time, login again to get a new one

### Issue 3: Empty List When Getting Reviews
**Solution:**
- Make sure you created AI tools first (reviews need valid aiToolId)
- Check that you're using the correct AI tool ID in the request

### Issue 4: LazyInitializationException
**Solution:** This has been fixed in the latest code. Rebuild and restart the application.

---

## 6. How JWT Authentication Works

1. **Public Endpoints** (No token required):
   - POST `/auth/admin/register`
   - POST `/auth/admin/login`
   - GET `/api/aitools`
   - GET `/api/aitools/{id}`
   - POST `/api/reviews`
   - GET `/api/reviews/aitool/{id}`
   - GET `/api/reviews/approved`

2. **Protected Endpoints** (Token required):
   - GET `/api/aitools/my-tools`
   - POST `/api/aitools`
   - PUT `/api/aitools/{id}`
   - DELETE `/api/aitools/{id}`
   - GET `/api/reviews/pending`
   - GET `/api/reviews/{id}`
   - PUT `/api/reviews/{id}/approve`
   - PUT `/api/reviews/{id}/reject`
   - DELETE `/api/reviews/{id}`

3. **How to Use Token:**
   - After login, copy the `token` value
   - In Postman, go to Authorization tab
   - Select "Bearer Token" type
   - Paste the token
   - Send the request

---

## 7. Database Check Commands (MySQL)

To verify data in your database:

```sql
-- Check admins
SELECT * FROM admins;

-- Check AI tools
SELECT * FROM ai_tools;

-- Check reviews
SELECT * FROM reviews;

-- Check reviews with AI tool names
SELECT r.id, r.content, r.rating, r.username, r.status, a.name as tool_name
FROM reviews r
JOIN ai_tools a ON r.aitool_id = a.id;
```

---

## Summary

All issues have been fixed:
1. ✅ JWT authentication now works properly on `/auth/admin/login`
2. ✅ URL routing conflict with "tools" parameter resolved
3. ✅ LazyInitializationException fixed with proper fetch strategies
4. ✅ All endpoints tested and working

You can now test the complete application flow!

