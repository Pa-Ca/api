-- Date: 2023-06-13

-- We create the table table (this is in reference to the tables where the clients sit)
CREATE TABLE table{
    id INT NOT NULL,
    branch_id INT NOT NULL,
    name VARCHAR(128) NOT NULL,
    
    CONSTRAINT table_pk PRIMARY KEY (id),
    CONSTRAINT table_branch_fk FOREIGN KEY (branch_id) REFERENCES branch (id),
};

-- Now the sales are going to be related to the tables, not the branches
-- (The reference to the branch is going to be in the table table)
ALTER TABLE sale DROP COLUMN branch_id;
ALTER TABLE sale DROP CONSTRAINT sale_branch_fk;

-- We alter the table sale to add the column table_id, which is a foreign key to the 'table' table
-- This column is not null, since every sale must be related to a table
ALTER TABLE sale ADD COLUMN table_id INT NOT NULL;
ALTER TABLE sale ADD CONSTRAINT sale_table_fk
    FOREIGN KEY (table_id)
    REFERENCES table (id)  
    ON DELETE SET NULL
    NOT DEFERRABLE 
    INITIALLY IMMEDIATE
    ;

-- We add the column table_name to the sale table, which is the name of the table where the sale was made
-- We add the column name to the sale table, which is the name of the sale
-- We add the column note to the sale table, which is a note that the client can add to the sale
ALTER TABLE sale ADD COLUMN note VARCHAR(2048);
ALTER TABLE sale ADD COLUMN table_name VARCHAR(128);
ALTER TABLE sale ADD COLUMN table_name VARCHAR(128);

CREATE SEQUENCE IF NOT EXISTS table MINVALUE 1 START WITH 1000 INCREMENT BY 1;








