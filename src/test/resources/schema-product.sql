drop table if exists product;
create table product
(
    id VARCHAR(255) PRIMARY KEY,
    name VARCHAR(255) NOT NULL ,
    description VARCHAR(255),
    date_added DATE NOT NULL
);

drop table if exists product_tag;
create table product_tag
(
    product_id VARCHAR(255) NOT NULL,
    tag VARCHAR(255) NOT NULL,
    PRIMARY KEY (product_id, tag)
);