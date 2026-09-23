-- Seed data for local development.
-- ddl-auto is create-drop, so the tables start empty on every run and this file fills them again.

INSERT INTO customers (id, username, family_name, given_name)
VALUES ('62c06820-f7da-4090-85bd-05f5f10b6e07', 'koklim', 'Ou', 'Koklim');

-- One row per (business, product): these two rows are one business with two products.
INSERT INTO businesses (business_id, product_id, business_active, name, price) VALUES
('d215b5f8-0249-4dc5-89a3-51fd148cfb41', 'd215b5f8-0249-4dc5-89a3-51fd148cfb47', true, 'Product 1', 25.00),
('d215b5f8-0249-4dc5-89a3-51fd148cfb41', 'd215b5f8-0249-4dc5-89a3-51fd148cfb48', true, 'Product 2', 50.00);
