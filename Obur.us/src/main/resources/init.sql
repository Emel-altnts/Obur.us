-- Enable PostGIS extension
CREATE EXTENSION IF NOT EXISTS postgis;

-- Create tables will be handled by Hibernate
-- This script is for additional initialization

-- Insert sample data for testing

-- Sample users (passwords are 'password123' hashed with BCrypt)
INSERT INTO users (name, email, password, phone_number, is_active, created_at, updated_at)
VALUES
    ('John Doe', 'john@example.com', '$2a$10$rN8KpY8pDWqXKqOwY9TLiOXMQSfDPFqLXzKZhJZhXqLp8KqOwY9TL', '+905551234567', true, NOW(), NOW()),
    ('Jane Smith', 'jane@example.com', '$2a$10$rN8KpY8pDWqXKqOwY9TLiOXMQSfDPFqLXzKZhJZhXqLp8KqOwY9TL', '+905557654321', true, NOW(), NOW()),
    ('Ali Yılmaz', 'ali@example.com', '$2a$10$rN8KpY8pDWqXKqOwY9TLiOXMQSfDPFqLXzKZhJZhXqLp8KqOwY9TL', '+905559876543', true, NOW(), NOW())
    ON CONFLICT DO NOTHING;

-- Sample places in Istanbul
INSERT INTO places (name, description, cuisine, price_range, average_price, location_geo, address, city, phone_number, website_url, image_url, opening_hours, is_active, average_rating, total_reviews, created_at, updated_at)
VALUES
    (
        'Sultanahmet Koftecisi',
        'Famous traditional Turkish meatball restaurant since 1920',
        'Turkish',
        '$$',
        150.00,
        ST_SetSRID(ST_MakePoint(28.9784, 41.0082), 4326),
        'Divanyolu Cad. No:12 Sultanahmet',
        'Istanbul',
        '+902125201200',
        'https://sultanahmetkoftecisi.com',
        'https://example.com/images/sultanahmet-koftecisi.jpg',
        'Mon-Sun: 11:00-23:00',
        true,
        4.5,
        0,
        NOW(),
        NOW()
    ),
    (
        'Mikla Restaurant',
        'Contemporary Turkish cuisine with a view',
        'Turkish',
        '$$$$',
        500.00,
        ST_SetSRID(ST_MakePoint(28.9869, 41.0122), 4326),
        'Marmara Pera Hotel, Meşrutiyet Caddesi',
        'Istanbul',
        '+902122931234',
        'https://miklarestaurant.com',
        'https://example.com/images/mikla.jpg',
        'Tue-Sun: 18:30-23:00',
        true,
        4.8,
        0,
        NOW(),
        NOW()
    ),
    (
        'Karaköy Lokantası',
        'Traditional Turkish meyhane experience',
        'Turkish',
        '$$$',
        300.00,
        ST_SetSRID(ST_MakePoint(28.9744, 41.0206), 4326),
        'Kemankeş Caddesi No:37A Karaköy',
        'Istanbul',
        '+902122921234',
        'https://karakoylokantasi.com',
        'https://example.com/images/karakoy-lokantasi.jpg',
        'Mon-Sun: 12:00-00:00',
        true,
        4.6,
        0,
        NOW(),
        NOW()
    ),
    (
        'Starbucks Taksim',
        'International coffee chain',
        'Cafe',
        '$$',
        50.00,
        ST_SetSRID(ST_MakePoint(28.9869, 41.0369), 4326),
        'İstiklal Caddesi No:187 Beyoğlu',
        'Istanbul',
        '+902122451234',
        'https://starbucks.com.tr',
        'https://example.com/images/starbucks.jpg',
        'Mon-Sun: 07:00-23:00',
        true,
        4.2,
        0,
        NOW(),
        NOW()
    ),
    (
        'Kronotrop Coffee',
        'Specialty coffee roastery and cafe',
        'Cafe',
        '$$',
        80.00,
        ST_SetSRID(ST_MakePoint(28.9750, 41.0200), 4326),
        'Kemankeş Caddesi No:57 Karaköy',
        'Istanbul',
        '+902122441234',
        'https://kronotrop.com',
        'https://example.com/images/kronotrop.jpg',
        'Mon-Sun: 08:00-20:00',
        true,
        4.7,
        0,
        NOW(),
        NOW()
    ),
    (
        'Nusr-Et Steakhouse',
        'Famous luxury steakhouse',
        'Steakhouse',
        '$$$$',
        800.00,
        ST_SetSRID(ST_MakePoint(29.0092, 41.0392), 4326),
        'Nispetiye Caddesi No:87 Etiler',
        'Istanbul',
        '+902123581234',
        'https://nusr-et.com.tr',
        'https://example.com/images/nusret.jpg',
        'Mon-Sun: 12:00-02:00',
        true,
        4.4,
        0,
        NOW(),
        NOW()
    ),
    (
        'Çiya Sofrası',
        'Authentic Anatolian cuisine',
        'Turkish',
        '$$',
        200.00,
        ST_SetSRID(ST_MakePoint(29.0275, 40.9869), 4326),
        'Caferağa Mahallesi Kadıköy',
        'Istanbul',
        '+902163301234',
        'https://ciya.com.tr',
        'https://example.com/images/ciya.jpg',
        'Mon-Sun: 11:00-22:00',
        true,
        4.9,
        0,
        NOW(),
        NOW()
    ),
    (
        'Sidewalk Cafe',
        'American-style brunch cafe',
        'Cafe',
        '$$',
        120.00,
        ST_SetSRID(ST_MakePoint(28.9844, 41.0344), 4326),
        'Tomtom Mahallesi Beyoğlu',
        'Istanbul',
        '+902122451234',
        'https://sidewalkcafe.com',
        'https://example.com/images/sidewalk.jpg',
        'Mon-Sun: 09:00-23:00',
        true,
        4.3,
        0,
        NOW(),
        NOW()
    )
    ON CONFLICT DO NOTHING;

-- Sample reviews
INSERT INTO reviews (user_id, place_id, rating, comment, visit_date, is_verified, created_at, updated_at)
SELECT
    1,
    p.id,
    4.5,
    'Great food and atmosphere!',
    NOW() - INTERVAL '7 days',
    true,
    NOW(),
    NOW()
FROM places p
WHERE p.name = 'Sultanahmet Koftecisi'
    LIMIT 1
ON CONFLICT DO NOTHING;

INSERT INTO reviews (user_id, place_id, rating, comment, visit_date, is_verified, created_at, updated_at)
SELECT
    2,
    p.id,
    5.0,
    'Best coffee in Istanbul!',
    NOW() - INTERVAL '3 days',
    true,
    NOW(),
    NOW()
FROM places p
WHERE p.name = 'Kronotrop Coffee'
    LIMIT 1
ON CONFLICT DO NOTHING;

-- Sample user preferences
INSERT INTO user_preferences (user_id, cuisine_type, price_range, preferred_distance, preference_weight, created_at, updated_at)
VALUES
    (1, 'Turkish', '$$', 5.0, 1.0, NOW(), NOW()),
    (1, 'Cafe', '$$', 3.0, 0.8, NOW(), NOW()),
    (2, 'Cafe', '$$$', 10.0, 1.0, NOW(), NOW()),
    (3, 'Turkish', '$$$$', 15.0, 0.9, NOW(), NOW())
    ON CONFLICT DO NOTHING;