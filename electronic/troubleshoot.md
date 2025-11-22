# Hướng dẫn khắc phục lỗi "ERR_CONNECTION_REFUSED"

## Nguyên nhân:
- Ứng dụng không khởi động được
- Port 8080 không có process nào chạy

## Các bước kiểm tra:

### 1. Kiểm tra MySQL đang chạy
- Mở MySQL Workbench
- Thử kết nối đến MySQL Server
- Nếu không kết nối được → Khởi động MySQL Service

### 2. Kiểm tra database shopquanao
Trong MySQL Workbench, chạy:
```sql
SHOW DATABASES;
USE shopquanao;
SHOW TABLES;
```

### 3. Khởi động ứng dụng và xem logs
```bash
cd C:\Users\nhant\Downloads\electronic\electronic
mvn spring-boot:run
```

**Xem logs để tìm lỗi:**
- Nếu thấy "Started ElectronicApplication" → Ứng dụng đã chạy
- Nếu thấy "Database connection failed" → Lỗi kết nối database
- Nếu có exception khác → Ghi lại lỗi cụ thể

### 4. Kiểm tra port 8080
```bash
netstat -ano | findstr :8080
```

### 5. Nếu MySQL chưa chạy
Có thể tạm thời sử dụng H2 database (in-memory) để test:

Trong `application.properties`, comment MySQL và uncomment H2:
```properties
# MySQL (comment out)
# spring.datasource.url=jdbc:mysql://127.0.0.1:3306/shopquanao?createDatabaseIfNotExist=true&serverTimezone=UTC

# H2 (uncomment)
spring.datasource.url=jdbc:h2:mem:shopquanao;MODE=MYSQL
spring.datasource.driver-class-name=org.h2.Driver
```

