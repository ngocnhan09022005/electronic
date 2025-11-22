-- ============================================
-- Database Setup Script for shopquanao
-- Chạy script này trong MySQL Workbench
-- ============================================

-- Sử dụng database shopquanao
USE shopquanao;

-- ============================================
-- 1. Bảng USERS - Quản lý người dùng
-- ============================================
CREATE TABLE IF NOT EXISTS users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL COMMENT 'bcrypt hashed password',
    email VARCHAR(255) UNIQUE,
    full_name VARCHAR(255) COMMENT 'Họ và tên',
    phone VARCHAR(20) COMMENT 'Số điện thoại',
    address VARCHAR(500) COMMENT 'Địa chỉ chi tiết',
    city VARCHAR(100) COMMENT 'Thành phố/Tỉnh',
    district VARCHAR(100) COMMENT 'Quận/Huyện',
    ward VARCHAR(100) COMMENT 'Phường/Xã',
    postal_code VARCHAR(10) COMMENT 'Mã bưu điện',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT 'Thời gian tạo',
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Thời gian cập nhật',
    INDEX idx_username (username),
    INDEX idx_email (email)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ============================================
-- 2. Bảng CLOTHING_ITEM - Quản lý sản phẩm quần áo
-- ============================================
CREATE TABLE IF NOT EXISTS clothing_item (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL COMMENT 'Tên sản phẩm',
    description VARCHAR(1000) COMMENT 'Mô tả sản phẩm',
    price DECIMAL(19,2) NOT NULL COMMENT 'Giá sản phẩm',
    type VARCHAR(50) NOT NULL COMMENT 'Loại sản phẩm: LONG_SLEEVE, SHORT_SLEEVE, LONG_PANTS, SHORT_PANTS, OUTWEAR, ACCESSORY',
    image_url VARCHAR(1000) COMMENT 'URL hình ảnh',
    featured BOOLEAN DEFAULT FALSE COMMENT 'Sản phẩm nổi bật',
    stock INT DEFAULT 0 COMMENT 'Số lượng tồn kho',
    category VARCHAR(100) COMMENT 'Danh mục',
    brand VARCHAR(100) COMMENT 'Thương hiệu',
    size VARCHAR(50) COMMENT 'Kích thước (S, M, L, XL, etc)',
    color VARCHAR(50) COMMENT 'Màu sắc',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT 'Thời gian tạo',
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Thời gian cập nhật',
    INDEX idx_type (type),
    INDEX idx_featured (featured),
    INDEX idx_name (name)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ============================================
-- 3. Bảng ORDERS - Quản lý đơn hàng
-- ============================================
CREATE TABLE IF NOT EXISTS orders (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    order_code VARCHAR(50) NOT NULL UNIQUE COMMENT 'Mã đơn hàng',
    user_id BIGINT NOT NULL COMMENT 'ID người dùng',
    total_amount DECIMAL(19,2) NOT NULL COMMENT 'Tổng tiền',
    payment_method VARCHAR(50) COMMENT 'Phương thức thanh toán',
    payment_status VARCHAR(50) DEFAULT 'PENDING' COMMENT 'Trạng thái thanh toán: PENDING, PAID, FAILED',
    order_status VARCHAR(50) DEFAULT 'PENDING' COMMENT 'Trạng thái đơn hàng: PENDING, PROCESSING, SHIPPED, DELIVERED, CANCELLED',
    shipping_address VARCHAR(500) COMMENT 'Địa chỉ giao hàng',
    shipping_phone VARCHAR(20) COMMENT 'Số điện thoại nhận hàng',
    shipping_name VARCHAR(255) COMMENT 'Tên người nhận',
    notes TEXT COMMENT 'Ghi chú đơn hàng',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT 'Thời gian tạo',
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Thời gian cập nhật',
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    INDEX idx_user_id (user_id),
    INDEX idx_order_code (order_code),
    INDEX idx_order_status (order_status),
    INDEX idx_created_at (created_at)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ============================================
-- 4. Bảng ORDER_ITEMS - Chi tiết đơn hàng
-- ============================================
CREATE TABLE IF NOT EXISTS order_items (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    order_id BIGINT NOT NULL COMMENT 'ID đơn hàng',
    clothing_item_id BIGINT NOT NULL COMMENT 'ID sản phẩm',
    quantity INT NOT NULL DEFAULT 1 COMMENT 'Số lượng',
    price DECIMAL(19,2) NOT NULL COMMENT 'Giá tại thời điểm đặt hàng',
    subtotal DECIMAL(19,2) NOT NULL COMMENT 'Thành tiền (quantity * price)',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT 'Thời gian tạo',
    FOREIGN KEY (order_id) REFERENCES orders(id) ON DELETE CASCADE,
    FOREIGN KEY (clothing_item_id) REFERENCES clothing_item(id) ON DELETE RESTRICT,
    INDEX idx_order_id (order_id),
    INDEX idx_clothing_item_id (clothing_item_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ============================================
-- 5. Dữ liệu mẫu - Sản phẩm quần áo
-- ============================================
INSERT INTO clothing_item (name, description, price, type, image_url, featured, stock) VALUES
('Áo sơ mi Oxford', 'Áo tay dài kiểu dáng Hàn Quốc', 520000.00, 'LONG_SLEEVE', 'https://source.unsplash.com/random/400x400?shirt', TRUE, 25),
('Áo thun Oversize', 'Chất cotton 100%, thấm hút tốt', 280000.00, 'SHORT_SLEEVE', 'https://source.unsplash.com/random/400x401?tshirt', TRUE, 50),
('Quần jean slim-fit', 'Form ôm, co giãn 4 chiều', 650000.00, 'LONG_PANTS', 'https://source.unsplash.com/random/400x402?jeans', FALSE, 15),
('Quần short linen', 'Mang phong cách resort', 390000.00, 'SHORT_PANTS', 'https://source.unsplash.com/random/400x403?shorts', FALSE, 34),
('Áo khoác denim', 'Layer cá tính cho mùa thu', 820000.00, 'OUTWEAR', 'https://source.unsplash.com/random/400x404?jacket', TRUE, 10),
('Nón bucket canvas', 'Phụ kiện đi biển hot trend', 210000.00, 'ACCESSORY', 'https://source.unsplash.com/random/400x405?hat', FALSE, 65);

-- ============================================
-- Hiển thị kết quả
-- ============================================
SELECT 'Database setup completed successfully!' AS status;
SELECT COUNT(*) AS total_products FROM clothing_item;

