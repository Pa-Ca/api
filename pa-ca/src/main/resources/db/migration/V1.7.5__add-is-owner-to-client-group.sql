-- 2023-07-20

ALTER TABLE reservation DROP COLUMN is_owner;

ALTER TABLE client_group ADD COLUMN is_owner bool NOT NULL default FALSE;
ALTER TABLE client ADD COLUMN identity_document VARCHAR(16)  NOT NULL;

ALTER TABLE client 
    ADD CONSTRAINT client_unique_identity_document 
    UNIQUE (identity_document) 
    NOT DEFERRABLE  
    INITIALLY IMMEDIATE;