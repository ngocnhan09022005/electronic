# 📊 Thông tin Database - shopquanao

## 🗂️ Cấu trúc Database

Database **`shopquanao`** chứa các bảng sau:

### 1. 📋 Bảng `users` - Quản lý tài khoản người dùng

**Vị trí:** `shopquanao` → `Tables` → `users`

**Các cột (Columns) trong bảng `users`:**

| Cột | Kiểu dữ liệu | Mô tả | Bắt buộc |
|-----|-------------|-------|----------|
| `id` | BIGINT (AUTO_INCREMENT) | ID duy nhất của người dùng (Primary Key) | ✅ |
| `username` | VARCHAR(100) | Tên đăng nhập (duy nhất) | ✅ |
| `password` | VARCHAR(255) | Mật khẩu đã được mã hóa bằng bcrypt | ✅ |
| `email` | VARCHAR(255) | Email (duy nhất) | ❌ |
| `full_name` | VARCHAR(255) | Họ và tên đầy đủ | ❌ |
| `phone` | VARCHAR(20) | Số điện thoại | ❌ |
| `address` | VARCHAR(500) | Địa chỉ chi tiết | ❌ |
| `city` | VARCHAR(100) | Thành phố/Tỉnh | ❌ |
| `district` | VARCHAR(100) | Quận/Huyện | ❌ |
| `ward` | VARCHAR(100) | Phường/Xã | ❌ |
| `postal_code` | VARCHAR(10) | Mã bưu điện | ❌ |
| `role` | VARCHAR(20) | Vai trò: "USER" hoặc "ADMIN" (mặc định: "USER") | ❌ |
| `created_at` | TIMESTAMP | Thời gian tạo tài khoản | ✅ |
| `updated_at` | TIMESTAMP | Thời gian cập nhật lần cuối | ✅ |

**Indexes:**
- `idx_username` - Index trên cột `username` để tìm kiếm nhanh
- `idx_email` - Index trên cột `email` để tìm kiếm nhanh

---

### 2. 📦 Bảng `clothing_item` - Quản lý sản phẩm

**Vị trí:** `shopquanao` → `Tables` → `clothing_item`

Lưu trữ thông tin về các sản phẩm quần áo.

---

### 3. 🛒 Bảng `orders` - Quản lý đơn hàng

**Vị trí:** `shopquanao` → `Tables` → `orders`

Lưu trữ thông tin về các đơn hàng của khách hàng.

**Cột quan trọng:**
- `id` - ID đơn hàng
- `order_code` - Mã đơn hàng (ví dụ: ORD-123456)
- `user_id` - ID người dùng (Foreign Key đến bảng `users`)
- `total_amount` - Tổng tiền
- `payment_status` - Trạng thái thanh toán (PENDING, COMPLETED, FAILED)
- `order_status` - Trạng thái đơn hàng (PENDING, PROCESSING, SHIPPED, DELIVERED, CANCELLED)

---

### 4. 📝 Bảng `order_items` - Chi tiết sản phẩm trong đơn hàng

**Vị trí:** `shopquanao` → `Tables` → `order_items`

Lưu trữ chi tiết từng sản phẩm trong đơn hàng.

---

## 🔍 Khi tạo tài khoản, thông tin được lưu vào đâu?

### Quy trình tạo tài khoản:

1. **Người dùng điền form đăng ký:**
   - Username
   - Password (chưa mã hóa)
   - Email (tùy chọn)

2. **Hệ thống xử lý:**
   - Mã hóa password bằng **bcrypt** (thư viện BCrypt)
   - Tạo đối tượng `UserAccount` với các thông tin
   - Lưu vào database qua `UserAccountRepository.save()`

3. **Dữ liệu được lưu vào bảng `users`:**
   ```sql
   INSERT INTO users (username, password, email, role, created_at, updated_at)
   VALUES ('nguyenvana', '$2a$10$hashedpassword...', 'nguyenvana@email.com', 'USER', NOW(), NOW());
   ```

4. **ID tự động tạo:**
   - MySQL tự động tạo `id` (AUTO_INCREMENT)
   - ID này được trả về và lưu vào session

### 📍 Vị trí trong MySQL Workbench:

1. Mở **MySQL Workbench**
2. Kết nối đến server MySQL
3. Chọn database **`shopquanao`**
4. Mở rộng **`Tables`**
5. Click chuột phải vào bảng **`users`** → **`Select Rows`**
6. Xem tất cả các tài khoản đã được tạo

### 🔎 Query để xem dữ liệu:

```sql
-- Xem tất cả người dùng
SELECT * FROM users;

-- Xem thông tin một người dùng cụ thể
SELECT id, username, email, full_name, role, created_at 
FROM users 
WHERE username = 'tên_người_dùng';

-- Xem tất cả admin
SELECT * FROM users WHERE role = 'ADMIN';
```

---

## ⚠️ Lưu ý quan trọng:

1. **Password không bao giờ lưu dạng text thuần:**
   - Tất cả password đều được mã hóa bằng bcrypt
   - Không thể xem được password gốc

2. **ID là tự động:**
   - Không cần nhập ID khi tạo tài khoản
   - MySQL tự động tạo ID tăng dần (1, 2, 3, ...)

3. **Username và Email là duy nhất:**
   - Không thể có 2 tài khoản cùng username
   - Không thể có 2 tài khoản cùng email

4. **Session chỉ lưu tạm thời:**
   - Session lưu `userId`, `username`, `role` trong bộ nhớ
   - Khi đăng xuất hoặc hết hạn session, dữ liệu trong session bị xóa
   - Dữ liệu trong database `users` vẫn được giữ lại vĩnh viễn

---

## 🛠️ Quản lý Database trong SQL Workbench:

Bạn có thể:
- **Xem dữ liệu:** Right-click vào bảng → Select Rows
- **Chỉnh sửa dữ liệu:** Right-click vào bảng → Edit Table Data
- **Thêm dữ liệu:** Insert vào bảng trực tiếp
- **Xóa dữ liệu:** Delete rows trong bảng
- **Thay đổi role:** Sửa cột `role` từ "USER" thành "ADMIN" để tạo admin

---

## 📝 File liên quan trong code:

- **Model:** `electronic/src/main/java/com/example/electronic/model/UserAccount.java`
- **Repository:** `electronic/src/main/java/com/example/electronic/repository/UserAccountRepository.java`
- **Service:** `electronic/src/main/java/com/example/electronic/service/AuthService.java`
- **Controller:** `electronic/src/main/java/com/example/electronic/controller/AuthController.java`

