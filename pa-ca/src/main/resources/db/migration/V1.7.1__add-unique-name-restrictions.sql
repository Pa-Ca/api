--Date: 2023-07-03
ALTER TABLE product_sub_category
    ADD CONSTRAINT product_sub_category_unique_name 
    UNIQUE (branch_id, product_category_id, name) 
    NOT DEFERRABLE 
    INITIALLY IMMEDIATE;

ALTER TABLE "table"
    ADD CONSTRAINT table_unique_name 
    UNIQUE (branch_id, name) 
    NOT DEFERRABLE  
    INITIALLY IMMEDIATE;

ALTER TABLE product 
    ADD CONSTRAINT product_unique_name 
    UNIQUE (product_sub_category_id, name) 
    NOT DEFERRABLE 
    INITIALLY IMMEDIATE;