-- Date: 2023-02-04

CREATE SEQUENCE IF NOT EXISTS business_seq MINVALUE 1 START WITH 1000 INCREMENT BY 1;
CREATE SEQUENCE IF NOT EXISTS branch_seq MINVALUE 1 START WITH 1000 INCREMENT BY 1;

ALTER TABLE branch ALTER COLUMN score SET DATA TYPE FLOAT;
ALTER TABLE branch ADD COLUMN reservation_price FLOAT NOT NULL;
ALTER TABLE branch DROP COLUMN client_id;