-- Date: 2023-01-19

CREATE TABLE tier (
    id int  NOT NULL,
    name varchar(1024)  NOT NULL,
    reservation_limit int  NOT NULL,
    tier_cost money  NOT NULL,
    CONSTRAINT tier_pk PRIMARY KEY (id)
);

ALTER TABLE business ADD tier_id int  NOT NULL;

ALTER TABLE business ADD CONSTRAINT business_tier
    FOREIGN KEY (tier_id)
    REFERENCES tier (id)  
    NOT DEFERRABLE 
    INITIALLY IMMEDIATE