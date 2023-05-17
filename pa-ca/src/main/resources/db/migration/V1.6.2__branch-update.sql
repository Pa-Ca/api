-- -- Date: 2023-05-14

-- Branch update
ALTER TABLE branch ADD COLUMN hour_in TIME;
ALTER TABLE branch ADD COLUMN hour_out TIME;
ALTER TABLE branch DROP COLUMN average_reserve_time;
ALTER TABLE branch ADD COLUMN average_reserve_time INTERVAL;
ALTER TABLE branch RENAME COLUMN address TO location;
ALTER TABLE branch RENAME COLUMN coordinates TO maps_link;
ALTER TABLE branch DROP COLUMN reservation_price;
ALTER TABLE branch ADD COLUMN reservation_price DECIMAL;

-- JWT black list update
-- ALTER TABLE jwt_black_list ALTER COLUMN expiration TYPE TIMESTAMP;