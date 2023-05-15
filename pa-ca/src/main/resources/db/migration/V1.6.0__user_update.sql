-- -- Date: 2023-05-12

ALTER TABLE "user" ADD COLUMN provider VARCHAR(64) NULL;
ALTER TABLE "user" ADD COLUMN provider_id VARCHAR(64) NULL;
ALTER TABLE "user" ADD COLUMN registration_status INT NOT NULL;
