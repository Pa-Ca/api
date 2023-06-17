-- Date 16-06-2023

ALTER TABLE sale Add COLUMN reservation_id INT DEFAULT NULL;

-- Add a constraint to the sale table to make sure that the reservation_id is a foreign key to the reservation table
-- Note that the reservation_id can be null, since the sale can be made without a reservation

ALTER TABLE sale ADD CONSTRAINT sale_reservation_fk
    FOREIGN KEY (reservation_id)
    REFERENCES reservation (id)  
    ON DELETE SET NULL
    NOT DEFERRABLE 
    INITIALLY IMMEDIATE
    ;