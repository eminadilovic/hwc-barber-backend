-- Modify rating columns to use float type
ALTER TABLE shops ALTER COLUMN rating TYPE float USING rating::float;
ALTER TABLE employees ALTER COLUMN rating TYPE float USING rating::float; 