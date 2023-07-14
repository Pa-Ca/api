--Date 05-07-2023

-- Create a new table called payment_option
CREATE TABLE payment_option (
    id INT NOT NULL,
    branch_id INT NOT NULL,
    name VARCHAR(64) NOT NULL,
    description VARCHAR(1024) DEFAULT NULL,
     
    CONSTRAINT payment_option_pk PRIMARY KEY (id),
    CONSTRAINT payment_option_branch_fk FOREIGN KEY (branch_id) REFERENCES branch (id)
);

-- Create the sequence for the payment_option table
CREATE SEQUENCE IF NOT EXISTS payment_option_seq MINVALUE 1 START WITH 1000 INCREMENT BY 1;

-- Alter sale table to add payment_option_id
ALTER TABLE sale
ADD COLUMN payment_option_id INT DEFAULT NULL,
ADD CONSTRAINT sale_payment_option_fk FOREIGN KEY (payment_option_id) REFERENCES payment_option (id);
