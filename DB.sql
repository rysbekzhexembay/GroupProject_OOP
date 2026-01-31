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

INSERT INTO users (username, password_hash, role) VALUES 
('admin', 'admin123', 'admin'),
('customer', 'pass123', 'customer'),
('testuser', 'password123', 'customer');

INSERT INTO instrument_types (name) VALUES 
('Guitars'),
('Keyboards'),
('Drums'),
('Brass'),
('Strings');

INSERT INTO instruments (name, type_id, price, stock) VALUES 
('Acoustic Guitar Yamaha', (SELECT id FROM instrument_types WHERE name = 'Guitars'), 15000, 5),
('Fender Electric guitar', (SELECT id FROM instrument_types WHERE name = 'Guitars'), 45000, 3),
('Casio Digital Piano', (SELECT id FROM instrument_types WHERE name = 'Keyboards'), 35000, 2),
('Steinway Grand piano', (SELECT id FROM instrument_types WHERE name = 'Keyboards'), 1200000, 1),
('Pearl drum kit', (SELECT id FROM instrument_types WHERE name = 'Drums'), 60000, 2),
('Yamaha saxophone', (SELECT id FROM instrument_types WHERE name = 'Brass'), 80000, 4),
('Violin Stradivarius', (SELECT id FROM instrument_types WHERE name = 'Strings'), 500000, 0);

INSERT INTO orders (user_id, instrument_id, price, card_last4) VALUES 
((SELECT id FROM users WHERE username = 'testuser'), 
 (SELECT id FROM instruments WHERE name = 'Acoustic Guitar Yamaha'), 
 15000, 
 '4444'),
((SELECT id FROM users WHERE username = 'customer'), 
 (SELECT id FROM instruments WHERE name = 'Fender Electric guitar'), 
 45000, 
 '1111');