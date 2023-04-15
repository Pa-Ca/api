-- Date: 2023-04-07

ALTER TABLE client
ALTER COLUMN stripe_customer_id DROP NOT NULL,
    ALTER COLUMN phone_number DROP NOT NULL,
    ALTER COLUMN address DROP NOT NULL,
    ALTER COLUMN date_of_birth DROP NOT NULL;