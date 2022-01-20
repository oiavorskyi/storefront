drop table if exists product_inventory;
create table product_inventory
(
    product_id VARCHAR(255) PRIMARY KEY,
    inventory  INTEGER NOT NULL
);