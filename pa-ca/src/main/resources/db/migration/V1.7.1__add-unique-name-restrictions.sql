--Date: 2023-07-03
ALTER TABLE product_sub_category
    ADD CONSTRAINT product_sub_category_unique_name 
    UNIQUE (branch_id, product_category_id, name) 
    NOT DEFERRABLE 
    INITIALLY IMMEDIATE;

CREATE UNIQUE INDEX table_unique_name
    ON "table" (branch_id, name)
    WHERE deleted = FALSE;

ALTER TABLE product 
    ADD CONSTRAINT product_unique_name 
    UNIQUE (product_sub_category_id, name) 
    NOT DEFERRABLE 
    INITIALLY IMMEDIATE;