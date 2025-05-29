CREATE TABLE employee_invitations (
    id BIGSERIAL PRIMARY KEY,
    shop_id BIGINT NOT NULL,
    email VARCHAR(255) NOT NULL,
    position VARCHAR(255) NOT NULL,
    token VARCHAR(255) NOT NULL UNIQUE,
    status VARCHAR(20) NOT NULL,
    expires_at TIMESTAMP NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (shop_id) REFERENCES shops(id) ON DELETE CASCADE
);

CREATE INDEX idx_shop_id ON employee_invitations(shop_id);
CREATE INDEX idx_token ON employee_invitations(token);
CREATE INDEX idx_email_status ON employee_invitations(email, status); 