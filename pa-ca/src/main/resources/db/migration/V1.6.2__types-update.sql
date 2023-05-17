-- -- Date: 2023-05-14

-- Branch update
ALTER TABLE branch ADD COLUMN hour_in TIME;
ALTER TABLE branch ADD COLUMN hour_out TIME;
ALTER TABLE branch DROP COLUMN average_reserve_time;
ALTER TABLE branch ADD COLUMN average_reserve_time INTERVAL;
ALTER TABLE branch RENAME COLUMN address TO location;
ALTER TABLE branch RENAME COLUMN coordinates TO maps_link;
ALTER TABLE branch ALTER COLUMN reservation_price TYPE DECIMAL;

-- Product update 
ALTER TABLE product ALTER COLUMN price TYPE DECIMAL;

-- Tier update
ALTER TABLE tier ALTER COLUMN tier_cost TYPE DECIMAL;

-- Invoice update
ALTER TABLE invoice ALTER COLUMN price TYPE DECIMAL;

-- Reservation update
ALTER TABLE reservation ALTER COLUMN price TYPE DECIMAL;