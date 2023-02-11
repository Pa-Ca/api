-- Date: 2023-02-04

CREATE TABLE product_category (
    id INT NOT NULL,
    name varchar(1024)  NOT NULL,
    CONSTRAINT product_category_pk PRIMARY KEY (id)
);

CREATE TABLE product_sub_category (
    id INT NOT NULL,
    branch_id INT NOT NULL,
    product_category_id INT NOT NULL,
    name varchar(1024)  NOT NULL,
    CONSTRAINT product_sub_category_pk PRIMARY KEY (id)
);

CREATE SEQUENCE IF NOT EXISTS product_category_seq MINVALUE 1 START WITH 1000 INCREMENT BY 1;
CREATE SEQUENCE IF NOT EXISTS product_sub_category_seq MINVALUE 1 START WITH 1000 INCREMENT BY 1;