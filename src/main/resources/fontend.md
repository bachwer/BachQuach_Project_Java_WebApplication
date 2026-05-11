

# SMART ACADEMIC & LAB SUPPORT PLATFORM
## Frontend Structure & Full Page Analysis

---

# 1. TỔNG QUAN KIẾN TRÚC FRONTEND

## Role trong hệ thống

| Role | Chức năng |
|---|---|
| ADMIN | Quản trị hệ thống |
| STUDENT | Sinh viên |
| LECTURER | Giảng viên / Mentor |

---

# 2. CẤU TRÚC THƯ MỤC FRONTEND

```txt
src/main/resources/

├── templates/
│
│   ├── auth/
│   │   ├── login.html
│   │   └── register.html
│   │
│   ├── admin/
│   │   ├── dashboard.html
│   │   ├── equipments/
│   │   │   ├── list.html
│   │   │   ├── create.html
│   │   │   ├── edit.html
│   │   │   └── detail.html
│   │   │
│   │   ├── borrowings/
│   │   │   ├── pending.html
│   │   │   ├── approved.html
│   │   │   └── detail.html
│   │   │
│   │   └── users/
│   │       ├── lecturers.html
│   │       └── students.html
│   │
│   ├── student/
│   │   ├── dashboard.html
│   │   ├── profile.html
│   │   ├── mentoring/
│   │   │   ├── booking.html
│   │   │   ├── my-sessions.html
│   │   │   ├── history.html
│   │   │   └── detail.html
│   │   │
│   │   ├── borrowings/
│   │   │   ├── my-borrowings.html
│   │   │   └── detail.html
│   │
│   ├── lecturer/
│   │   ├── dashboard.html
│   │   ├── mentoring/
│   │   │   ├── pending.html
│   │   │   ├── accepted.html
│   │   │   ├── completed.html
│   │   │   └── evaluation.html
│   │   │
│   │   └── students/
│   │       └── detail.html
│
│
├── static/
│
│   ├── css/
│   │   ├── global.css
│   │   ├── admin.css
│   │   ├── student.css
│   │   └── lecturer.css
│   │
│   ├── js/
│   │
│   │   ├── core/
│   │   │   ├── api.js
│   │   │   ├── auth.js
│   │   │   ├── role.js
│   │   │   └── utils.js
│   │   │
│   │   ├── auth/
│   │   │   ├── login.js
│   │   │   ├── register.js
│   │   │   └── me.js
│   │   │
│   │   ├── admin/
│   │   │   ├── dashboard.js
│   │   │   ├── equipments.js
│   │   │   ├── borrowings.js
│   │   │   └── users.js
│   │   │
│   │   ├── student/
│   │   │   ├── dashboard.js
│   │   │   ├── mentoring.js
│   │   │   ├── profile.js
│   │   │   └── borrowings.js
│   │   │
│   │   └── lecturer/
│   │       ├── dashboard.js
│   │       ├── mentoring.js
│   │       └── evaluation.js
│   │
│   └── images/
```

---

# 3. PAGE CHO STUDENT

## Authentication

| Page | Mô tả |
|---|---|
| /login | Đăng nhập |
| /register | Đăng ký |

---

## Dashboard

| Page | Mô tả |
|---|---|
| /student/dashboard | Trang tổng quan sinh viên |

### Nội dung

- Tổng số lịch đã đặt
- Lịch sắp tới
- Thiết bị đang mượn
- Thông báo hệ thống

---

## Profile

| Page | Mô tả |
|---|---|
| /student/profile | Hồ sơ cá nhân |

### Chức năng

- Update thông tin cá nhân
- Đổi avatar
- Đổi password

---

## Mentoring

| Page | Mô tả |
|---|---|
| /student/mentoring/booking | Đặt lịch cố vấn |
| /student/mentoring/my-sessions | Danh sách lịch của tôi |
| /student/mentoring/history | Lịch sử học tập |
| /student/mentoring/detail/{id} | Chi tiết buổi tư vấn |

### Booking Flow

1. Chọn Department
2. Chọn Lecturer
3. Chọn Date
4. Chọn Time Slot
5. Submit booking

### Logic

- Không cho đặt lịch quá khứ
- Không cho duplicate slot
- Có nút cancel booking

---

## Borrowings

| Page | Mô tả |
|---|---|
| /student/borrowings/my-borrowings | Thiết bị đã mượn |
| /student/borrowings/detail/{id} | Chi tiết phiếu mượn |

### Nội dung

- Tên thiết bị
- Trạng thái
- Ngày mượn
- Ngày trả
- Số lượng

---

# 4. PAGE CHO LECTURER

## Dashboard

| Page | Mô tả |
|---|---|
| /lecturer/dashboard | Dashboard giảng viên |

### Nội dung

- Số lịch chờ xác nhận
- Số lịch hoàn thành
- Sinh viên gần đây

---

## Mentoring Management

| Page | Mô tả |
|---|---|
| /lecturer/mentoring/pending | Lịch chờ xử lý |
| /lecturer/mentoring/accepted | Lịch đã nhận |
| /lecturer/mentoring/completed | Lịch hoàn thành |
| /lecturer/mentoring/evaluation/{id} | Đánh giá sinh viên |

---

## Evaluation Form

### Chức năng

- Nhập đánh giá năng lực
- Chọn thiết bị cho sinh viên
- Ghi chú
- Submit transaction

### Transaction

Khi submit:

- Update session status
- Create evaluation
- Create borrowing record
- Create borrowing details

Nếu lỗi:

- Rollback toàn bộ

---

# 5. PAGE CHO ADMIN

## Dashboard

| Page | Mô tả |
|---|---|
| /admin/dashboard | Dashboard admin |

### Nội dung

- Tổng số users
- Tổng số thiết bị
- Thiết bị đang mượn
- Top lecturers
- Biểu đồ thống kê

---

## Equipment Management

| Page | Mô tả |
|---|---|
| /admin/equipments | Danh sách thiết bị |
| /admin/equipments/create | Thêm thiết bị |
| /admin/equipments/edit/{id} | Sửa thiết bị |
| /admin/equipments/detail/{id} | Chi tiết thiết bị |

### CRUD

- Create
- Read
- Update
- Delete

### Fields

- Name
- Description
- Quantity
- Available Quantity
- Image
- Status

---

## Borrowing Approval

| Page | Mô tả |
|---|---|
| /admin/borrowings/pending | Phiếu chờ cấp phát |
| /admin/borrowings/approved | Phiếu đã xuất |
| /admin/borrowings/detail/{id} | Chi tiết phiếu |

### Logic

Khi approve:

- Check stock
- Trừ tồn kho
- Update status

Nếu thiếu stock:

- Reject toàn bộ

---

## User Management

| Page | Mô tả |
|---|---|
| /admin/users/students | Danh sách sinh viên |
| /admin/users/lecturers | Danh sách giảng viên |

---

# 6. API FRONTEND STRUCTURE

## Core API

### api.js

```js
const API_URL = "http://localhost:8080/api";
```

---

## auth.js

```js
function saveToken(token){
    localStorage.setItem("token", token);
}

function getToken(){
    return localStorage.getItem("token");
}

function logout(){
    localStorage.removeItem("token");
    window.location.href = "/login";
}
```

---

## role.js

```js
function hasRole(user, role){
    return user.roles.includes(role);
}
```

---

# 7. LOGIN FLOW

```txt
Login
   ↓
/api/auth/login
   ↓
Save JWT Token
   ↓
/api/auth/me
   ↓
Check Role
   ↓
Redirect Dashboard
```

---

# 8. ROLE REDIRECT

```js
if (roles.includes("ADMIN")) {
    window.location.href = "/admin/dashboard";
}

if (roles.includes("STUDENT")) {
    window.location.href = "/student/dashboard";
}

if (roles.includes("LECTURER")) {
    window.location.href = "/lecturer/dashboard";
}
```

---

# 9. DATABASE CORE TABLES

## Main Tables

```txt
users
user_profiles
roles
user_roles

departments
lecturers

equipments

mentoring_sessions
academic_evaluations

borrowing_records
borrowing_details
```

---

# 10. CORE BUSINESS LOGIC

## CORE-05

### Anti Duplicate Booking

```txt
1 Lecturer
+
1 Time Slot
=
UNIQUE
```

---

## CORE-06

### Transaction

```txt
Update Session
+
Create Evaluation
+
Create Borrowing
+
Create Borrowing Detail
=
1 TRANSACTION
```

---

## CORE-08

### Stock Validation

```txt
Check Quantity
→ Enough → Approve
→ Not Enough → Reject
```

---

# 11. UI COMPONENTS

## Shared Components

```txt
Navbar
Sidebar
Footer
Table
Pagination
Modal
Alert
Loader
```

---

# 12. GỢI Ý DASHBOARD UI

## Admin Dashboard

- Cards thống kê
- Bar chart
- Pie chart
- Recent borrowings
- Top lecturers

---

## Student Dashboard

- Upcoming sessions
- Borrowing status
- Learning history

---

## Lecturer Dashboard

- Pending sessions
- Completed evaluations
- Student analytics

---

# 13. SECURITY

## JWT

```txt
Login
→ Generate JWT
→ Save localStorage
→ Send Authorization Header
```

---

## Header

```txt
Authorization: Bearer TOKEN
```

---

# 14. FRONTEND BEST PRACTICE

## Không viết toàn bộ JS trong HTML

Sai:

```html
<script>
async function login(){ }
</script>
```

Đúng:

```html
<script src="/js/auth/login.js"></script>
```

---

# 15. FULL ROADMAP

## Phase 1

- Login/Register
- JWT
- Role
- Dashboard

## Phase 2

- Mentoring booking
- Lecturer evaluation
- Borrowing flow

## Phase 3

- Admin inventory
- Statistics
- Charts

## Phase 4

- Async email
- Payment
- OAuth2
- Cron jobs
```