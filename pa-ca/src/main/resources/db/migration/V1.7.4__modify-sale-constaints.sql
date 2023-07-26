-- 2023-07-14
ALTER TABLE tax DROP CONSTRAINT tax_sale_fk;
ALTER TABLE tax
    ADD CONSTRAINT tax_sale_fk 
    FOREIGN KEY (sale_id) 
    REFERENCES sale (id) 
    ON DELETE CASCADE;

ALTER TABLE sale_product DROP CONSTRAINT sale_product_sale_fk;
ALTER TABLE sale_product
    ADD CONSTRAINT sale_product_sale_fk 
    FOREIGN KEY (sale_id) 
    REFERENCES sale (id) 
    ON DELETE CASCADE;