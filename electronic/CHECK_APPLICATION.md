# Hướng dẫn kiểm tra tại sao ứng dụng không chạy

## Bước 1: Kiểm tra MySQL đang chạy

1. Mở **MySQL Workbench**
2. Kết nối đến MySQL Server
3. Nếu không kết nối được → Khởi động MySQL Service:
   - Windows Services → MySQL → Start

## Bước 2: Kiểm tra database shopquanao có tồn tại

Trong MySQL Workbench, chạy:
```sql
SHOW DATABASES;
```

Nếu không thấy `shopquanao`, database sẽ được tạo tự động khi ứng dụng chạy.

## Bước 3: Khởi động ứng dụng và xem logs

Chạy lệnh:
```bash
cd C:\Users\nhant\Downloads\electronic\electronic
mvn spring-boot:run
```

**Chú ý xem logs:**
- ✅ "Started ElectronicApplication" → Ứng dụng đã chạy
- ❌ "Failed to connect" → Lỗi kết nối MySQL
- ❌ "Table 'shopquanao.clothing_item' doesn't exist" → Bảng chưa có
- ❌ "Access denied" → Lỗi username/password MySQL

## Bước 4: Nếu vẫn không chạy

Gửi lại logs đầy đủ từ terminal để tôi kiểm tra.

