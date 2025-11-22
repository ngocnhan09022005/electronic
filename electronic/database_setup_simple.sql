-- ============================================
-- Script tạo bảng đơn giản cho shopquanao
-- Copy và paste toàn bộ script này vào MySQL Workbench
-- ============================================

USE shopquanao;

-- Xóa bảng cũ nếu có (cẩn thận: sẽ mất dữ liệu)
DROP TABLE IF EXISTS order_items;
DROP TABLE IF EXISTS orders;
DROP TABLE IF EXISTS clothing_item;
DROP TABLE IF EXISTS users;

-- ============================================
-- 1. Bảng USERS - Quản lý người dùng
-- ============================================
CREATE TABLE users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    email VARCHAR(255) UNIQUE,
    full_name VARCHAR(255),
    phone VARCHAR(20),
    address VARCHAR(500),
    city VARCHAR(100),
    district VARCHAR(100),
    ward VARCHAR(100),
    postal_code VARCHAR(10),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_username (username),
    INDEX idx_email (email)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ============================================
-- 2. Bảng CLOTHING_ITEM - Quản lý sản phẩm quần áo
-- ============================================
CREATE TABLE clothing_item (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description VARCHAR(1000),
    price DECIMAL(19,2) NOT NULL,
    type VARCHAR(50) NOT NULL,
    image_url VARCHAR(1000),
    featured BOOLEAN DEFAULT FALSE,
    stock INT DEFAULT 0,
    category VARCHAR(100),
    brand VARCHAR(100),
    size VARCHAR(50),
    color VARCHAR(50),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_type (type),
    INDEX idx_featured (featured)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ============================================
-- 3. Bảng ORDERS - Quản lý đơn hàng
-- ============================================
CREATE TABLE orders (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    order_code VARCHAR(50) NOT NULL UNIQUE,
    user_id BIGINT NOT NULL,
    total_amount DECIMAL(19,2) NOT NULL,
    payment_method VARCHAR(50),
    payment_status VARCHAR(50) DEFAULT 'PENDING',
    order_status VARCHAR(50) DEFAULT 'PENDING',
    shipping_address VARCHAR(500),
    shipping_phone VARCHAR(20),
    shipping_name VARCHAR(255),
    notes TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    INDEX idx_user_id (user_id),
    INDEX idx_order_code (order_code)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ============================================
-- 4. Bảng ORDER_ITEMS - Chi tiết đơn hàng
-- ============================================
CREATE TABLE order_items (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    order_id BIGINT NOT NULL,
    clothing_item_id BIGINT NOT NULL,
    quantity INT NOT NULL DEFAULT 1,
    price DECIMAL(19,2) NOT NULL,
    subtotal DECIMAL(19,2) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (order_id) REFERENCES orders(id) ON DELETE CASCADE,
    FOREIGN KEY (clothing_item_id) REFERENCES clothing_item(id) ON DELETE RESTRICT,
    INDEX idx_order_id (order_id),
    INDEX idx_clothing_item_id (clothing_item_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ============================================
-- 5. Thêm dữ liệu mẫu
-- ============================================
INSERT INTO clothing_item (name, description, price, type, image_url, featured, stock) VALUES
('Áo sơ mi Oxford', 'Áo tay dài kiểu dáng Hàn Quốc', 520000.00, 'LONG_SLEEVE', 'https://source.unsplash.com/random/400x400?shirt', TRUE, 25),
('Áo thun Oversize', 'Chất cotton 100%, thấm hút tốt', 280000.00, 'SHORT_SLEEVE', 'https://source.unsplash.com/random/400x401?tshirt', TRUE, 50),
('Quần jean slim-fit', 'Form ôm, co giãn 4 chiều', 650000.00, 'LONG_PANTS', 'https://source.unsplash.com/random/400x402?jeans', FALSE, 15),
('Quần short linen', 'Mang phong cách resort', 390000.00, 'SHORT_PANTS', 'https://source.unsplash.com/random/400x403?shorts', FALSE, 34),
('Áo khoác denim', 'Layer cá tính cho mùa thu', 820000.00, 'OUTWEAR', 'https://source.unsplash.com/random/400x404?jacket', TRUE, 10),
('Nón bucket canvas', 'Phụ kiện đi biển hot trend', 210000.00, 'ACCESSORY', 'https://source.unsplash.com/random/400x405?hat', FALSE, 65);

-- Hiển thị kết quả
SELECT 'Bảng đã được tạo thành công!' AS status;
SELECT 'Tổng số sản phẩm:' AS info, COUNT(*) AS total FROM clothing_item;

