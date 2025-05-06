-- Rename user_id to customer_id in reviews table
ALTER TABLE reviews RENAME COLUMN user_id TO customer_id;

-- Update the foreign key constraint
ALTER TABLE reviews DROP CONSTRAINT reviews_user_id_fkey;
ALTER TABLE reviews ADD CONSTRAINT reviews_customer_id_fkey FOREIGN KEY (customer_id) REFERENCES users(id);

-- Update the index
DROP INDEX idx_reviews_user;
CREATE INDEX idx_reviews_customer ON reviews(customer_id); 