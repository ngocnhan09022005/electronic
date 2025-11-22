-- ============================================
-- Script kiểm tra và cấu hình MySQL
-- Chạy script này trong MySQL Workbench
-- ============================================

-- 1. Kiểm tra user root hiện tại
SELECT user, host, plugin, authentication_string FROM mysql.user WHERE user='root' AND host='localhost';

-- 2. Kiểm tra database shopquanao có tồn tại không
SHOW DATABASES LIKE 'shopquanao';

-- 3. Nếu bạn muốn đặt password rỗng cho root (CHỈ DÙNG CHO LOCAL DEVELOPMENT)
-- Bỏ comment các dòng dưới đây nếu cần:

-- ALTER USER 'root'@'localhost' IDENTIFIED BY '';
-- FLUSH PRIVILEGES;

-- 4. Hoặc tạo user mới cho ứng dụng (KHUYẾN NGHỊ)
-- Bỏ comment các dòng dưới đây nếu muốn tạo user mới:

-- CREATE DATABASE IF NOT EXISTS shopquanao;
-- CREATE USER IF NOT EXISTS 'shopapp'@'localhost' IDENTIFIED BY 'shopapp123';
-- GRANT ALL PRIVILEGES ON shopquanao.* TO 'shopapp'@'localhost';
-- FLUSH PRIVILEGES;

-- 5. Kiểm tra lại sau khi cấu hình
-- SELECT user, host FROM mysql.user WHERE user IN ('root', 'shopapp');

