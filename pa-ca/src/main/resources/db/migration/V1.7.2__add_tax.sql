--Date 15-06-2023

CREATE TABLE tax{
    id INT NOT NULL,
    name VARCHAR(128) NOT NULL,
    type int, -- 0: percentage, 1: fixed
    value DECIMAL(16,4) NOT NULL,
    sale_id INT NOT NULL,
    
    CONSTRAINT tax_pk PRIMARY KEY (id),
    CONSTRAINT tax_sale_fk FOREIGN KEY (sale_id) REFERENCES sale (id),
};

-- precentage type tax: price * (1 + value)
-- fixed type      tax: price + value

CREATE TABLE default_tax{
    id INT NOT NULL,
    name VARCHAR(128) NOT NULL,
    type int, -- 0: percentage, 1: fixed
    value DECIMAL(16,4) NOT NULL,
    sale_id INT NOT NULL, -- Preguntar por este id  

    branch_id INT NOT NULL,

    CONSTRAINT default_tax_pk PRIMARY KEY (id),
    CONSTRAINT default_tax_branch_fk FOREIGN KEY (branch_id) REFERENCES branch (id),

}

-- We create the sequences for the tables
CREATE SEQUENCE IF NOT EXISTS tax MINVALUE 1 START WITH 1000 INCREMENT BY 1;
CREATE SEQUENCE IF NOT EXISTS default_tax MINVALUE 1 START WITH 1000 INCREMENT BY 1;

-- We add a the currencies 
-- In the branch table add a column called "dollar_to_local_currency_exchange" as a DECIMAL(16,4)

ALTER TABLE branch ADD COLUMN dollar_to_local_currency_exchange DECIMAL(16,4) NOT NULL DEFAULT 1.0;
ALTER TABLE sales  ADD COLUMN dollar_to_local_currency_exchange DECIMAL(16,4) NOT NULL DEFAULT 1.0;

