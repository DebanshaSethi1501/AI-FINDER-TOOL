# 🎯 COMPLETE REVIEW SYSTEM TESTING GUIDE

## 📋 What You'll Test

1. ✅ Submit a review (public - no auth)
2. ✅ View pending reviews (admin - requires auth)
3. ✅ Approve a review (admin - requires auth)
4. ✅ View approved reviews (public - no auth)
5. ✅ Reject/Delete reviews (admin - requires auth)

---

## 🚀 Step-by-Step Testing Instructions

### STEP 1: Make Sure Server is Running
```bash
# Check if server is running on port 8080
# You should see logs like: "Tomcat started on port 8080"
```

---

### STEP 2: Create an AI Tool First (Required!)

**Why?** Reviews need an AI tool to be linked to. You can't review something that doesn't exist!

#### 2a. Login as Admin
```
Method: POST
URL: http://localhost:8080/auth/admin/login

Headers:
Content-Type: application/json

Body (raw → JSON):
{
  "name": "testadmin",
  "password": "test123"
}

Expected Response:
{
  "token": "eyJhbGciOiJIUzI1NiJ9...",
  "message": "Login successful",
  "username": "testadmin",
  "adminId": "1"
}
```

**📋 COPY THE TOKEN!** You'll need it for next steps.

#### 2b. Create an AI Tool (With Token)
```
Method: POST
URL: http://localhost:8080/api/aitools

Authorization Tab:
- Type: Bearer Token
- Token: [paste your token here]

Headers:
Content-Type: application/json

Body (raw → JSON):
{
  "name": "ChatGPT",
  "decription": "AI conversational assistant by OpenAI",
  "usecases": "Content writing, coding help, customer support",
  "category": "Natural Language Processing",
  "pricingtype": "Freemium",
  "rating": 4.8
}

Expected Response:
{
  "message": "AI Tool created successfully",
  "tool": {
    "id": 1,  ← REMEMBER THIS ID!
    "name": "ChatGPT",
    ...
  }
}
```

**📌 Note the AI Tool ID (probably 1 if this is your first tool)**

---

### STEP 3: Submit a Review (No Auth Required!)

**Important:** Anyone can submit a review - no login needed!

```
Method: POST
URL: http://localhost:8080/api/reviews

Authorization: No Auth (select this!)

Headers:
Content-Type: application/json

Body (raw → JSON):
{
  "aiToolId": 1,
  "content": "This is an amazing AI tool! Very helpful for my work.",
  "rating": 4.5,
  "username": "john_doe"
}

Expected Response (201 Created):
{
  "message": "Review submitted successfully. It will be visible after admin approval.",
  "reviewId": 1,
  "status": "PENDING"
}
```

**✅ Success!** Review is now in database with status PENDING.

---

### STEP 4: Check Pending Reviews (Admin Only - Requires Token)

Now as admin, check what reviews are waiting for approval.

```
Method: GET
URL: http://localhost:8080/api/reviews/pending

Authorization Tab:
- Type: Bearer Token
- Token: [paste your admin token from Step 2a]

NO Body needed (GET request)

Expected Response (200 OK):
[
  {
    "id": 1,
    "content": "This is an amazing AI tool! Very helpful for my work.",
    "rating": 4.5,
    "username": "john_doe",
    "status": "PENDING",
    "aiToolId": 1,
    "aiToolName": "ChatGPT"
  }
]
```

**✅ You should see the review you just submitted!**

---

### STEP 5: Approve the Review (Admin Only - Requires Token)

```
Method: PUT
URL: http://localhost:8080/api/reviews/1/approve
     (Use the review ID from Step 4)

Authorization Tab:
- Type: Bearer Token
- Token: [paste your admin token]

NO Body needed

Expected Response (200 OK):
{
  "message": "Review approved successfully",
  "reviewId": 1,
  "status": "APPROVED"
}
```

**✅ Review is now approved and visible to public!**

---

### STEP 6: View Approved Reviews (Public - No Auth)

Now anyone can see the approved review!

```
Method: GET
URL: http://localhost:8080/api/reviews/aitool/1

Authorization: No Auth

NO Body needed

Expected Response (200 OK):
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

**✅ Public can now see this review!**

---

## 🎨 Visual Postman Setup

### For Endpoints WITHOUT Authentication:
```
┌────────────────────────────────────────────────┐
│ POST http://localhost:8080/api/reviews [Send] │
├────────────────────────────────────────────────┤
│ Authorization Tab:                             │
│ Type: No Auth                                  │
│         ↑ Select this!                         │
├────────────────────────────────────────────────┤
│ Body Tab:                                      │
│ • Select "raw"                                 │
│ • Select "JSON" from dropdown                  │
│ • Paste JSON body                              │
└────────────────────────────────────────────────┘
```

### For Endpoints WITH Authentication (Admin):
```
┌────────────────────────────────────────────────┐
│ GET http://localhost:8080/api/reviews/pending │
├────────────────────────────────────────────────┤
│ Authorization Tab:                             │
│ Type: Bearer Token              [▼]            │
│ Token: eyJhbGciOiJIUzI1NiJ9...                 │
│        ↑ Paste your token here                 │
└────────────────────────────────────────────────┘
```

---

## 📊 All Endpoints Summary

### PUBLIC Endpoints (No Token Required):

| Method | URL | Purpose |
|--------|-----|---------|
| POST | `/api/reviews` | Submit a review |
| GET | `/api/reviews/aitool/1` | View approved reviews for AI tool 1 |
| GET | `/api/reviews/approved` | View all approved reviews |

### ADMIN Endpoints (Token Required):

| Method | URL | Purpose |
|--------|-----|---------|
| GET | `/api/reviews/pending` | View pending reviews |
| PUT | `/api/reviews/1/approve` | Approve review #1 |
| PUT | `/api/reviews/1/reject` | Reject review #1 |
| DELETE | `/api/reviews/1` | Delete review #1 |

---

## 🧪 Quick Test Sequence

**Copy-paste this into Postman to test everything:**

### Test 1: Login
```
POST http://localhost:8080/auth/admin/login
Body: {"name":"testadmin","password":"test123"}
→ Copy token
```

### Test 2: Create AI Tool
```
POST http://localhost:8080/api/aitools
Auth: Bearer Token
Body: {"name":"ChatGPT","decription":"AI assistant","usecases":"Coding","category":"NLP","pricingtype":"Freemium","rating":4.8}
→ Note the tool ID
```

### Test 3: Submit Review (No Auth)
```
POST http://localhost:8080/api/reviews
Auth: No Auth
Body: {"aiToolId":1,"content":"Great tool!","rating":5.0,"username":"test_user"}
```

### Test 4: Check Pending (With Auth)
```
GET http://localhost:8080/api/reviews/pending
Auth: Bearer Token
```

### Test 5: Approve Review (With Auth)
```
PUT http://localhost:8080/api/reviews/1/approve
Auth: Bearer Token
```

### Test 6: View Approved (No Auth)
```
GET http://localhost:8080/api/reviews/aitool/1
Auth: No Auth
```

**✅ All working? Your review system is complete!**

---

## ⚠️ Common Issues & Solutions

### Issue 1: "AI Tool not found with id: X"
**Solution:** Create the AI tool first (Step 2)

### Issue 2: 403 Access Denied on /api/reviews/pending
**Solutions:**
- Make sure URL is `/api/reviews/pending` (NOT `/auth/api/reviews/pending`)
- Use Authorization tab, not Headers
- Select "Bearer Token" type
- Paste token without "Bearer " prefix
- Get fresh token (login again)

### Issue 3: Empty array when viewing reviews
**Solution:** 
- Check if reviews exist: `GET /api/reviews/pending` (with auth)
- Approve reviews first: `PUT /api/reviews/1/approve` (with auth)
- Public endpoint only shows APPROVED reviews

### Issue 4: 401 Unauthorized
**Solution:**
- Token expired (24 hours) - login again
- Token missing - add in Authorization tab
- Wrong credentials - check username/password

---

## 🎯 Testing Checklist

- [ ] Server is running on port 8080
- [ ] Logged in as admin and got token
- [ ] Created at least one AI tool
- [ ] Submitted a review (no auth)
- [ ] Viewed pending reviews (with auth)
- [ ] Approved a review (with auth)
- [ ] Viewed approved reviews (no auth)
- [ ] Tested reject/delete (with auth)

---

## 💾 Database Verification

You can also check directly in MySQL:

```sql
-- Check AI tools
SELECT * FROM ai_tools;

-- Check reviews
SELECT * FROM reviews;

-- Check reviews by status
SELECT * FROM reviews WHERE status = 'PENDING';
SELECT * FROM reviews WHERE status = 'APPROVED';

-- Check reviews for specific AI tool
SELECT * FROM reviews WHERE aitool_id = 1;
```

---

## 🎉 Complete Workflow Example

**User Journey:**
1. User visits your site and sees AI Tools
2. User submits a review (no login needed)
3. Review goes to "PENDING" status (not visible to public)
4. Admin logs in and checks pending reviews
5. Admin approves the review
6. Review status changes to "APPROVED"
7. Now everyone can see the review!

**This is exactly how platforms like Amazon, Yelp, Google Reviews work!**

---

## ✅ You're All Set!

Your review system is fully functional with:
- ✅ Public review submission
- ✅ Admin approval workflow
- ✅ Three status states (PENDING/APPROVED/REJECTED)
- ✅ Proper JWT authentication for admin operations
- ✅ Public viewing of approved reviews

**Start testing now!** 🚀

---

## 🆘 Need Help?

If something doesn't work:
1. Check server logs for errors
2. Verify admin exists in database
3. Make sure AI tool exists before submitting reviews
4. Use correct URLs (no `/auth` prefix for review endpoints)
5. Token in Authorization tab, not Headers tab

**Happy Testing!** 🎊

