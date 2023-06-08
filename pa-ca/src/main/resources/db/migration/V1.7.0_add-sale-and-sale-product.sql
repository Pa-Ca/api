-- Date: 2023-06-06
CREATE TABLE sale (
    id INT NOT NULL,
    client_quantity INT NOT NULL,
    branch_id int NOT NULL,
    start_time timestamp NOT NULL,
    end_time timestamp NOT NULL,

    CONSTRAINT sale_pk PRIMARY KEY (id),
    CONSTRAINT sale_branch_fk FOREIGN KEY (branch_id) REFERENCES branch (id),
);

CREATE TABLE sale_product (
    id INT NOT NULL,
    sale_id INT NOT NULL,
    product_id INT NOT NULL,
    ammount INT NOT NULL,
    price FLOAT,

    CONSTRAINT sale_product_pk PRIMARY KEY (id),
    CONSTRAINT sale_product_sale_fk FOREIGN KEY (sale_id) REFERENCES sale (id),
    CONSTRAINT sale_product_product_fk FOREIGN KEY (product_id) REFERENCES product (id),
);


CREATE SEQUENCE IF NOT EXISTS sale_seq MINVALUE 1 START WITH 1000 INCREMENT BY 1;

CREATE SEQUENCE IF NOT EXISTS sale_product_seq MINVALUE 1 START WITH 1000 INCREMENT BY 1;


ALTER TABLE branch ADD COLUMN num_of_tables int NOT NULL;