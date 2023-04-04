-- Date: 2023-03-16

ALTER TABLE reservation ADD COLUMN is_owner bool NOT NULL default FALSE;