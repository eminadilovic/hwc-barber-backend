-- Users table
CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(100) NOT NULL,
    phone_number VARCHAR(20),
    image_url TEXT,
    role VARCHAR(20) NOT NULL,
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Shops table
CREATE TABLE shops (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    description TEXT NOT NULL,
    address VARCHAR(200) NOT NULL,
    city VARCHAR(100) NOT NULL,
    phone_number VARCHAR(20) NOT NULL,
    email VARCHAR(100),
    image_url TEXT,
    rating DECIMAL(3,2) NOT NULL DEFAULT 0.0,
    total_reviews INTEGER NOT NULL DEFAULT 0,
    owner_id BIGINT NOT NULL REFERENCES users(id),
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Employees table
CREATE TABLE employees (
    id BIGSERIAL PRIMARY KEY,
    shop_id BIGINT NOT NULL REFERENCES shops(id),
    user_id BIGINT NOT NULL REFERENCES users(id),
    position VARCHAR(100) NOT NULL,
    bio TEXT,
    image_url TEXT,
    rating DECIMAL(3,2) NOT NULL DEFAULT 0.0,
    total_reviews INTEGER NOT NULL DEFAULT 0,
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    UNIQUE(shop_id, user_id)
);

-- Services table
CREATE TABLE services (
    id BIGSERIAL PRIMARY KEY,
    shop_id BIGINT NOT NULL REFERENCES shops(id),
    name VARCHAR(100) NOT NULL,
    description TEXT NOT NULL,
    duration_minutes INTEGER NOT NULL,
    price DECIMAL(10,2) NOT NULL,
    image_url TEXT,
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Employee Services table (many-to-many relationship)
CREATE TABLE employee_services (
    employee_id BIGINT NOT NULL REFERENCES employees(id),
    service_id BIGINT NOT NULL REFERENCES services(id),
    PRIMARY KEY (employee_id, service_id)
);

-- Bookings table
CREATE TABLE bookings (
    id BIGSERIAL PRIMARY KEY,
    shop_id BIGINT NOT NULL REFERENCES shops(id),
    customer_id BIGINT NOT NULL REFERENCES users(id),
    employee_id BIGINT NOT NULL REFERENCES employees(id),
    service_id BIGINT NOT NULL REFERENCES services(id),
    start_time TIMESTAMP NOT NULL,
    end_time TIMESTAMP NOT NULL,
    status VARCHAR(20) NOT NULL,
    notes TEXT,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Reviews table
CREATE TABLE reviews (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL REFERENCES users(id),
    shop_id BIGINT NOT NULL REFERENCES shops(id),
    employee_id BIGINT REFERENCES employees(id),
    booking_id BIGINT REFERENCES bookings(id),
    rating INTEGER NOT NULL CHECK (rating >= 1 AND rating <= 5),
    comment TEXT,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Favorite Shops table
CREATE TABLE favorite_shops (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL REFERENCES users(id),
    shop_id BIGINT NOT NULL REFERENCES shops(id),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    UNIQUE(user_id, shop_id)
);

-- Create indexes
CREATE INDEX idx_users_email ON users(email);
CREATE INDEX idx_shops_city ON shops(city);
CREATE INDEX idx_shops_owner ON shops(owner_id);
CREATE INDEX idx_employees_shop ON employees(shop_id);
CREATE INDEX idx_employees_user ON employees(user_id);
CREATE INDEX idx_services_shop ON services(shop_id);
CREATE INDEX idx_bookings_shop ON bookings(shop_id);
CREATE INDEX idx_bookings_customer ON bookings(customer_id);
CREATE INDEX idx_bookings_employee ON bookings(employee_id);
CREATE INDEX idx_reviews_shop ON reviews(shop_id);
CREATE INDEX idx_reviews_user ON reviews(user_id);
CREATE INDEX idx_reviews_employee ON reviews(employee_id);
CREATE INDEX idx_favorite_shops_user ON favorite_shops(user_id);
CREATE INDEX idx_favorite_shops_shop ON favorite_shops(shop_id); 