-- ============================================
-- Template thêm sản phẩm mới vào bảng clothing_item
-- Copy và chỉnh sửa thông tin sản phẩm
-- ============================================

USE shopquanao;

-- Cú pháp thêm sản phẩm:
INSERT INTO clothing_item (
    name, 
    description, 
    price, 
    type, 
    image_url, 
    featured, 
    stock,
    category,
    brand,
    size,
    color
) VALUES (
    'Tên sản phẩm',                    -- Tên sản phẩm
    'Mô tả sản phẩm',                  -- Mô tả chi tiết
    500000.00,                          -- Giá (số thập phân)
    'LONG_SLEEVE',                      -- Loại: LONG_SLEEVE, SHORT_SLEEVE, LONG_PANTS, SHORT_PANTS, OUTWEAR, ACCESSORY
    'https://example.com/image.jpg',    -- URL hình ảnh
    TRUE,                               -- Featured: TRUE (nổi bật) hoặc FALSE
    50,                                 -- Số lượng tồn kho
    'Danh mục',                         -- Danh mục (tùy chọn)
    'Thương hiệu',                      -- Thương hiệu (tùy chọn)
    'M',                                -- Size: S, M, L, XL, etc (tùy chọn)
    'Đen'                               -- Màu sắc (tùy chọn)
);

-- ============================================
-- Ví dụ cụ thể - Thêm sản phẩm mới
-- ============================================

-- Ví dụ 1: Áo polo
INSERT INTO clothing_item (name, description, price, type, image_url, featured, stock) VALUES
('Áo polo nam cao cấp', 'Áo polo chất liệu cotton mềm mại, thoáng mát', 450000.00, 'SHORT_SLEEVE', 'https://source.unsplash.com/random/400x400?polo', TRUE, 30);

-- Ví dụ 2: Quần kaki
INSERT INTO clothing_item (name, description, price, type, image_url, featured, stock, brand, size, color) VALUES
('Quần kaki slim fit', 'Quần kaki form slim, chất liệu tốt', 550000.00, 'LONG_PANTS', 'https://source.unsplash.com/random/400x400?khaki', FALSE, 25, 'Brand X', '32', 'Be');

-- Ví dụ 3: Áo khoác gió
INSERT INTO clothing_item (name, description, price, type, image_url, featured, stock, category) VALUES
('Áo khoác gió chống nước', 'Áo khoác gió 2 lớp, chống nước tốt', 750000.00, 'OUTWEAR', 'https://source.unsplash.com/random/400x400?jacket', TRUE, 15, 'Áo khoác');

-- ============================================
-- Kiểm tra sản phẩm vừa thêm
-- ============================================
SELECT * FROM clothing_item ORDER BY id DESC LIMIT 5;

-- Xem tất cả sản phẩm
SELECT id, name, price, type, stock, featured FROM clothing_item ORDER BY id;

