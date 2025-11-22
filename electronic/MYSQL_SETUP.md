# Hướng dẫn cấu hình MySQL

## Lỗi: "Access denied for user 'root'@'localhost' (using password: NO)"

Lỗi này xảy ra khi MySQL yêu cầu mật khẩu nhưng ứng dụng không gửi mật khẩu (hoặc mật khẩu không đúng).

## Giải pháp:

### Cách 1: Đặt mật khẩu MySQL trong application.properties

1. Mở file `src/main/resources/application.properties`
2. Tìm dòng: `spring.datasource.password=`
3. Đặt mật khẩu MySQL root của bạn:
   ```
   spring.datasource.password=your_mysql_password
   ```
4. Lưu file và khởi động lại ứng dụng

### Cách 2: Cấu hình MySQL root user không có mật khẩu (chỉ dùng cho local development)

**Cảnh báo: Chỉ làm điều này trên máy local của bạn, KHÔNG dùng trong production!**

Chạy trong MySQL Workbench:

```sql
-- Đặt password thành rỗng cho root user
ALTER USER 'root'@'localhost' IDENTIFIED BY '';
FLUSH PRIVILEGES;
```

Hoặc nếu bạn muốn root không yêu cầu password authentication:

```sql
ALTER USER 'root'@'localhost' IDENTIFIED WITH mysql_native_password BY '';
FLUSH PRIVILEGES;
```

### Cách 3: Tạo MySQL user mới cho ứng dụng (Khuyến nghị)

1. Mở MySQL Workbench và kết nối với MySQL server
2. Chạy các lệnh sau:

```sql
-- Tạo user mới
CREATE USER 'shopapp'@'localhost' IDENTIFIED BY 'shopapp123';

-- Cấp quyền cho user mới
GRANT ALL PRIVILEGES ON shopquanao.* TO 'shopapp'@'localhost';

-- Áp dụng thay đổi
FLUSH PRIVILEGES;
```

3. Cập nhật `application.properties`:
   ```
   spring.datasource.username=shopapp
   spring.datasource.password=shopapp123
   ```

## Kiểm tra kết nối MySQL

1. Mở MySQL Workbench
2. Kết nối với MySQL server
3. Chạy lệnh để kiểm tra user:
   ```sql
   SELECT user, host, authentication_string FROM mysql.user WHERE user='root';
   ```

4. Kiểm tra database `shopquanao` đã tồn tại chưa:
   ```sql
   SHOW DATABASES LIKE 'shopquanao';
   ```

## Sau khi cấu hình xong

1. Khởi động lại ứng dụng: `mvn spring-boot:run`
2. Kiểm tra logs xem có thông báo "Database connection successful" không
3. Truy cập: `http://localhost:8080/shop`

