INSERT INTO instrument_types (name) VALUES 
('Guitars'),
('Keyboards'),
('Drums'),
('Brass');

INSERT INTO instruments (name, type_id, price, stock) VALUES 
('Acoustic Guitar Yamaha', (SELECT id FROM instrument_types WHERE name = 'Guitars'), 15000, 5),
('Fender Electric guitar', (SELECT id FROM instrument_types WHERE name = 'Guitars'), 45000, 3),
('Casio Digital Piano', (SELECT id FROM instrument_types WHERE name = 'Keyboards'), 35000, 2),
('Steinway Grand piano', (SELECT id FROM instrument_types WHERE name = 'Keyboards'), 1200000, 1),
('Pearl drum kit', (SELECT id FROM instrument_types WHERE name = 'Drums'), 60000, 2),
('Yamaha saxophone', (SELECT id FROM instrument_types WHERE name = 'Brass'), 80000, 4);

INSERT INTO users (username, password_hash) VALUES 
('testuser', 'password123');

INSERT INTO orders (user_id, instrument_id, price, card_last4) VALUES 
((SELECT id FROM users WHERE username = 'testuser'), 
 (SELECT id FROM instruments WHERE name = 'Acoustic Guitar Yamaha'), 
 15000, 
 '4444');
