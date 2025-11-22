# Hướng dẫn sửa lỗi /shop không hoạt động

## Nguyên nhân có thể:

1. **Database `shopquanao` chưa có bảng `clothing_item`**
2. **Ứng dụng không chạy hoặc bị lỗi khi khởi động**
3. **Database connection lỗi**

## Cách sửa:

### Bước 1: Kiểm tra ứng dụng có đang chạy không

Chạy lệnh:
```bash
cd C:\Users\nhant\Downloads\electronic\electronic
mvn spring-boot:run
```

Hoặc kiểm tra trong terminal/console có thông báo lỗi gì không.

### Bước 2: Kiểm tra database có bảng chưa

Trong MySQL Workbench, chạy:
```sql
USE shopquanao;
SHOW TABLES;
```

Nếu không có bảng nào, chạy script `database_setup_simple.sql` để tạo bảng.

### Bước 3: Cho Hibernate tự động tạo bảng (Nếu chưa có)

Nếu bạn chưa chạy script SQL, Hibernate sẽ tự động tạo bảng với `ddl-auto=update`.
Chỉ cần:
1. Đảm bảo database `shopquanao` đã được tạo
2. Khởi động ứng dụng
3. Hibernate sẽ tự động tạo bảng `clothing_item` và `users`

### Bước 4: Kiểm tra logs

Xem logs khi khởi động ứng dụng để tìm lỗi. Thường sẽ thấy:
- "Database connection successful" - OK
- Hoặc lỗi kết nối database

## Giải pháp nhanh:

1. **Restart ứng dụng**: Dừng và chạy lại `mvn spring-boot:run`
2. **Kiểm tra MySQL đang chạy**: Đảm bảo MySQL Server đang hoạt động
3. **Chạy script SQL**: Chạy file `database_setup_simple.sql` trong MySQL Workbench
4. **Kiểm tra URL**: Đảm bảo truy cập đúng `http://localhost:8080/shop` (không phải `/shop/`)

