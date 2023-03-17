-- Date: 2023-03-14

ALTER TABLE reservation ADD COLUMN occasion varchar(256) NOT NULL default '';
ALTER TABLE reservation ADD COLUMN petition varchar(256) NOT NULL default '';
ALTER TABLE reservation ADD COLUMN by_client bool NOT NULL default TRUE;