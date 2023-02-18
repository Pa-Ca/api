--Date: 2023-02-17

CREATE TABLE favorite_branch (
    id INT NOT NULL,
    client_id INT NOT NULL,
    branch_id INT NOT NULL,
    CONSTRAINT favorite_branch_pk PRIMARY KEY (id)
);

ALTER TABLE favorite_branch ADD CONSTRAINT favorite_branch_client
    FOREIGN KEY (client_id)
    REFERENCES client (id)  
    ON DELETE CASCADE
    NOT DEFERRABLE 
    INITIALLY IMMEDIATE
;

ALTER TABLE favorite_branch ADD CONSTRAINT favorite_branch_branch
    FOREIGN KEY (branch_id)
    REFERENCES branch (id)  
    ON DELETE CASCADE
    NOT DEFERRABLE 
    INITIALLY IMMEDIATE
;

CREATE SEQUENCE IF NOT EXISTS favorite_branch_seq MINVALUE 1 START WITH 1000 INCREMENT BY 1;
