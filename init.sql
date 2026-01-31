DROP TABLE IF EXISTS orders CASCADE;
DROP TABLE IF EXISTS instruments CASCADE;
DROP TABLE IF EXISTS instrument_types CASCADE;
DROP TABLE IF EXISTS users CASCADE;

CREATE TABLE users (
    id SERIAL PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    role VARCHAR(20) DEFAULT 'customer'
);

CREATE TABLE instrument_types (
    id SERIAL PRIMARY KEY,
    name VARCHAR(50) UNIQUE NOT NULL
);

CREATE TABLE instruments (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    type_id INTEGER REFERENCES instrument_types(id),
    price INTEGER NOT NULL,
    stock INTEGER DEFAULT 0
);

CREATE TABLE orders (
    id SERIAL PRIMARY KEY,
    user_id INTEGER REFERENCES users(id),
    instrument_id INTEGER REFERENCES instruments(id),
    price INTEGER,
    card_last4 VARCHAR(4),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);