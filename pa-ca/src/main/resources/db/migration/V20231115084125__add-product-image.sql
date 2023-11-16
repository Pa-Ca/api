-- Last modification date: 2023-11-11 20:16:15.242
ALTER TABLE product
ADD COLUMN "image" VARCHAR(256),
    ADD COLUMN highlight BOOLEAN DEFAULT FALSE;
-- End of file.