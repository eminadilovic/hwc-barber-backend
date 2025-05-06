-- Modify rating columns to use numeric type
ALTER TABLE shops ALTER COLUMN rating TYPE numeric(3,2);
ALTER TABLE employees ALTER COLUMN rating TYPE numeric(3,2); 