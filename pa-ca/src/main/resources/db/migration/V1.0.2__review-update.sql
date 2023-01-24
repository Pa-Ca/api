-- Date: 2023-01-19

ALTER TABLE review ADD COLUMN date timestamp NOT NULL DEFAULT now();