-- Add new columns to shops table
ALTER TABLE shops
    ADD COLUMN state VARCHAR(50) NOT NULL DEFAULT 'Unknown',
    ADD COLUMN zip_code VARCHAR(10) NOT NULL DEFAULT '00000',
    ADD COLUMN opening_time TIME,
    ADD COLUMN closing_time TIME,
    ADD COLUMN website VARCHAR(200); 