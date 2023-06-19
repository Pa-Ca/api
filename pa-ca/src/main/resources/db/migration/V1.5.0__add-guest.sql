-- Date: 2023-04-13

-- Table: guest
CREATE TABLE guest (
    id int NOT NULL,
    name VARCHAR(50) NOT NULL,
    surname VARCHAR(50) NOT NULL,
    email VARCHAR(100) NOT NULL,
    phone_number VARCHAR(20) NOT NULL,
    CONSTRAINT guest_pk PRIMARY KEY (id)
);

ALTER TABLE reservation ADD COLUMN guest_id INT;
ALTER TABLE reservation ADD CONSTRAINT reservation_guest
    FOREIGN KEY (guest_id)
    REFERENCES guest (id)  
    ON DELETE CASCADE
    NOT DEFERRABLE 
    INITIALLY IMMEDIATE
;

CREATE SEQUENCE IF NOT EXISTS guest_seq MINVALUE 1 START WITH 1000 INCREMENT BY 1;
