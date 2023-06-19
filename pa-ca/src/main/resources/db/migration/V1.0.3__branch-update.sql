-- Date: 2023-01-19

ALTER TABLE branch RENAME COLUMN coordiantes TO coordinates;
ALTER TABLE branch ADD COLUMN reserve_off boolean;