-- ============================================
-- Script kiểm tra database shopquanao
-- Chạy script này để xem dữ liệu hiện tại
-- ============================================

USE shopquanao;

-- 1. Kiểm tra các bảng có tồn tại không
SHOW TABLES;

-- 2. Kiểm tra cấu trúc bảng clothing_item
DESCRIBE clothing_item;

-- 3. Kiểm tra số lượng sản phẩm
SELECT COUNT(*) AS total_products FROM clothing_item;

-- 4. Xem tất cả sản phẩm
SELECT id, name, price, type, stock, featured FROM clothing_item;

-- 5. Kiểm tra bảng users
DESCRIBE users;
SELECT COUNT(*) AS total_users FROM users;

