-- Date: 2023-02-24

ALTER TABLE invoice ADD COLUMN price money NOT NULL default 0.0;
ALTER TABLE invoice ADD COLUMN client_number int NOT NULL default 0;
ALTER TABLE invoice ADD COLUMN payment varchar(50) NOT NULL default '';
ALTER TABLE invoice ADD COLUMN payment_code varchar(50) NOT NULL default '';