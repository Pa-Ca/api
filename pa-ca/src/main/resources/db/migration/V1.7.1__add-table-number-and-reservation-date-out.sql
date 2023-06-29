ALTER TABLE reservation
    ALTER COLUMN reservation_date DROP NOT NULL;
ALTER TABLE reservation DROP COLUMN reservation_date;
ALTER TABLE reservation ADD COLUMN reservation_date_in TIMESTAMP NOT NULL;
ALTER TABLE reservation ADD COLUMN reservation_date_out TIMESTAMP NULL;
ALTER TABLE reservation ADD COLUMN table_number INT  NOT NULL;
ALTER TABLE guest ADD COLUMN identity_document VARCHAR(16)  NOT NULL;