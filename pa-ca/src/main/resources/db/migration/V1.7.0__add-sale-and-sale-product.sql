-- Date: 2023-06-06
-- Table: table
CREATE TABLE "table"(
    id INT NOT NULL,
    branch_id INT NOT NULL,
    name VARCHAR(128) NOT NULL,
    CONSTRAINT table_pk PRIMARY KEY (id),
    CONSTRAINT table_branch_fk FOREIGN KEY (branch_id) REFERENCES branch (id)
);

-- Table: sale
CREATE TABLE sale (
    id INT NOT NULL,
    client_quantity INT NOT NULL,
    table_id int NOT NULL,
    start_time timestamp NOT NULL,
    end_time timestamp DEFAULT NULL,
    note VARCHAR(2048) DEFAULT NULL,
    reservation_id int DEFAULT NULL,
    dollar_to_local_currency_exchange DECIMAL(16, 4) NOT NULL DEFAULT 1.0,
    CONSTRAINT sale_pk PRIMARY KEY (id),
    CONSTRAINT sale_table_fk FOREIGN KEY (table_id) REFERENCES "table" (id),
    CONSTRAINT sale_reservation_fk FOREIGN KEY (reservation_id) REFERENCES reservation (id)
);

CREATE TABLE sale_product (
    id INT NOT NULL,
    sale_id INT NOT NULL,
    product_id INT NOT NULL,
    ammount INT NOT NULL,
    price FLOAT,
    CONSTRAINT sale_product_pk PRIMARY KEY (id),
    CONSTRAINT sale_product_sale_fk FOREIGN KEY (sale_id) REFERENCES sale (id),
    CONSTRAINT sale_product_product_fk FOREIGN KEY (product_id) REFERENCES product (id)
);

CREATE TABLE tax(
    id INT NOT NULL,
    name VARCHAR(128) NOT NULL,
    type int,
    -- 0: percentage, 1: fixed
    value DECIMAL(16, 4) NOT NULL,
    sale_id INT NOT NULL,
    CONSTRAINT tax_pk PRIMARY KEY (id),
    CONSTRAINT tax_sale_fk FOREIGN KEY (sale_id) REFERENCES sale (id)
);

-- precentage type tax: price * (1 + value)
-- fixed type      tax: price + value
CREATE TABLE default_tax(
    id INT NOT NULL,
    name VARCHAR(128) NOT NULL,
    type int,
    -- 0: percentage, 1: fixed
    value DECIMAL(16, 4) NOT NULL,
    sale_id INT NOT NULL,
    -- Preguntar por este id  
    branch_id INT NOT NULL,
    CONSTRAINT default_tax_pk PRIMARY KEY (id),
    CONSTRAINT default_tax_branch_fk FOREIGN KEY (branch_id) REFERENCES branch (id)
);

-- Existing table modifications
ALTER TABLE
    branch
ADD
    COLUMN dollar_to_local_currency_exchange DECIMAL(16, 4) NOT NULL DEFAULT 1.0;

-- We create the sequences 
CREATE SEQUENCE IF NOT EXISTS tax MINVALUE 1 START WITH 1000 INCREMENT BY 1;

CREATE SEQUENCE IF NOT EXISTS default_tax MINVALUE 1 START WITH 1000 INCREMENT BY 1;

CREATE SEQUENCE IF NOT EXISTS "table" MINVALUE 1 START WITH 1000 INCREMENT BY 1;

CREATE SEQUENCE IF NOT EXISTS sale_seq MINVALUE 1 START WITH 1000 INCREMENT BY 1;

CREATE SEQUENCE IF NOT EXISTS sale_product_seq MINVALUE 1 START WITH 1000 INCREMENT BY 1;