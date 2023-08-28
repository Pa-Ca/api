-- Created by Vertabelo (http://vertabelo.com)
-- Last modification date: 2023-08-20 14:31:04.212

-- tables
-- Table: amenity
CREATE TABLE public.amenity (
    id int  NOT NULL,
    name varchar(100)  NOT NULL,
    CONSTRAINT amenity_unique_name UNIQUE (name) NOT DEFERRABLE  INITIALLY IMMEDIATE,
    CONSTRAINT amenity_pk PRIMARY KEY (id)
);

-- Table: branch
CREATE TABLE public.branch (
    id int  NOT NULL,
    business_id int  NOT NULL,
    name varchar(100)  NOT NULL,
    score decimal(10,4)  NOT NULL,
    capacity smallint  NOT NULL,
    reservation_price money  NOT NULL,
    maps_link varchar(1024)  NULL,
    location varchar(95)  NULL,
    overview text  NULL,
    visibility boolean  NOT NULL,
    reserve_off boolean  NULL,
    phone_number varchar(31)  NULL,
    type varchar(100)  NULL,
    hour_in time  NULL,
    hour_out time  NULL,
    average_reserve_time interval  NULL,
    dollar_exchange decimal(16,4)  NULL,
    deleted boolean  NOT NULL DEFAULT false,
    CONSTRAINT branch_pk PRIMARY KEY (id)
);

CREATE INDEX branch_index_business_id on public.branch (business_id ASC);

-- Table: branch_amenity
CREATE TABLE public.branch_amenity (
    id int  NOT NULL,
    branch_id int  NOT NULL,
    amenity_id int  NOT NULL,
    CONSTRAINT branch_amenity_pk PRIMARY KEY (id)
);

CREATE INDEX branch_amenity_index_branch_id on public.branch_amenity (branch_id ASC);

CREATE INDEX branch_amenity_index_amenity_id on public.branch_amenity (amenity_id ASC);

-- Table: business
CREATE TABLE public.business (
    id int  NOT NULL,
    user_id int  NOT NULL,
    tier_id int  NOT NULL,
    name varchar(100)  NOT NULL,
    verified boolean  NOT NULL,
    phone_number varchar(31)  NULL,
    CONSTRAINT business_pk PRIMARY KEY (id)
);

CREATE INDEX business_index_tier_id on public.business (tier_id ASC);

CREATE INDEX business_index_user_id on public.business (user_id ASC);

CREATE INDEX business_index_name on public.business (name ASC);

-- Table: client
CREATE TABLE public.client (
    id int  NOT NULL,
    user_id int  NOT NULL,
    name varchar(35)  NOT NULL,
    surname varchar(35)  NOT NULL,
    identity_document varchar(16)  NOT NULL,
    address varchar(95)  NULL,
    phone_number varchar(31)  NULL,
    stripe_customer_id varchar(64)  NULL,
    date_of_birth date  NULL,
    CONSTRAINT client_unique_identity_document UNIQUE (identity_document) NOT DEFERRABLE  INITIALLY IMMEDIATE,
    CONSTRAINT client_unique_stripe_customer_id UNIQUE (stripe_customer_id) NOT DEFERRABLE  INITIALLY IMMEDIATE,
    CONSTRAINT client_unique_phone_number UNIQUE (phone_number) NOT DEFERRABLE  INITIALLY IMMEDIATE,
    CONSTRAINT client_pk PRIMARY KEY (id)
);

CREATE INDEX client_index_user_id on public.client (user_id ASC);

CREATE INDEX client_index_name on public.client (name ASC);

CREATE INDEX client_index_surname on public.client (surname ASC);

CREATE INDEX client_index_identity_document on public.client (identity_document ASC);

-- Table: client_group
CREATE TABLE public.client_group (
    id int  NOT NULL,
    client_id int  NOT NULL,
    reservation_id int  NOT NULL,
    is_owner boolean  NOT NULL,
    CONSTRAINT client_group_pk PRIMARY KEY (id)
);

CREATE INDEX client_group_index_client_id on public.client_group (client_id ASC);

CREATE INDEX client_group_index_reservation_id on public.client_group (reservation_id ASC);

-- Only one owner per reservation
CREATE UNIQUE INDEX client_group_unique_owner
ON "client_group" (reservation_id)
WHERE is_owner = TRUE;;

-- Table: client_guest
CREATE TABLE client_guest (
    id int  NOT NULL,
    client_id int  NULL,
    guest_id int  NULL,
    have_guest boolean  NOT NULL,
    CONSTRAINT client_guest_check_client_id__guest_id CHECK ((NOT have_guest AND client_id IS NOT NULL)  OR (have_guest AND guest_id IS NOT NULL)) NOT DEFERRABLE INITIALLY IMMEDIATE,
    CONSTRAINT client_guest_pk PRIMARY KEY (id)
);

CREATE INDEX client_guest_index_client_id on client_guest (client_id ASC);

CREATE INDEX client_guest_index_guest_id on client_guest (guest_id ASC);

-- Table: default_tax
CREATE TABLE default_tax (
    id int  NOT NULL,
    branch_id int  NOT NULL,
    tax_id int  NOT NULL,
    CONSTRAINT default_tax_pk PRIMARY KEY (id)
);

CREATE INDEX default_tax_index_branch_id on default_tax (branch_id ASC);

CREATE INDEX default_tax_index_tax_id on default_tax (tax_id ASC);

-- Table: favorite_branch
CREATE TABLE public.favorite_branch (
    id int  NOT NULL,
    client_id int  NOT NULL,
    branch_id int  NOT NULL,
    CONSTRAINT favorite_branch_pk PRIMARY KEY (id)
);

CREATE INDEX favorite_branch_index_client_id on public.favorite_branch (client_id ASC);

CREATE INDEX favorite_branch_index_branch_id on public.favorite_branch (branch_id ASC);

-- Table: friend
CREATE TABLE public.friend (
    id int  NOT NULL,
    client_requester_id int  NOT NULL,
    client_addresser_id int  NOT NULL,
    accepted boolean  NOT NULL DEFAULT false,
    rejected boolean  NOT NULL DEFAULT false,
    CONSTRAINT friend_unique_requester_and_addresser UNIQUE (client_requester_id, client_addresser_id) NOT DEFERRABLE  INITIALLY IMMEDIATE,
    CONSTRAINT friend_check_requester_not_equal_to_addresser CHECK (client_requester_id != client_addresser_id) NOT DEFERRABLE INITIALLY IMMEDIATE,
    CONSTRAINT friend_check_accepted_nor_rejected CHECK (NOT accepted OR NOT rejected) NOT DEFERRABLE INITIALLY IMMEDIATE,
    CONSTRAINT friend_pk PRIMARY KEY (id)
);

CREATE INDEX friend_index_client_requester_id on public.friend (client_requester_id ASC);

CREATE INDEX friend_index_client_addresser_id on public.friend (client_addresser_id ASC);

-- Table: guest
CREATE TABLE public.guest (
    id int  NOT NULL,
    name varchar(35)  NOT NULL,
    surname varchar(35)  NOT NULL,
    email varchar(320)  NULL,
    phone_number varchar(31)  NULL,
    identity_document varchar(16)  NOT NULL,
    CONSTRAINT guest_unique_identity_document UNIQUE (identity_document) NOT DEFERRABLE  INITIALLY IMMEDIATE,
    CONSTRAINT guest_unique_email UNIQUE (email) NOT DEFERRABLE  INITIALLY IMMEDIATE,
    CONSTRAINT guest_unique_phone_number UNIQUE (phone_number) NOT DEFERRABLE  INITIALLY IMMEDIATE,
    CONSTRAINT guest_pk PRIMARY KEY (id)
);

CREATE INDEX guest_index_identity_document on public.guest (identity_document ASC);

CREATE INDEX guest_index_name on public.guest (name ASC);

CREATE INDEX guest_index_surname on public.guest (surname ASC);

-- Table: insite_sale
CREATE TABLE insite_sale (
    id int  NOT NULL,
    sale_id int  NOT NULL,
    reservation_id int  NULL,
    CONSTRAINT insite_sale_pk PRIMARY KEY (id)
);

CREATE INDEX insite_sale_index_sale_id on insite_sale (sale_id ASC);

CREATE INDEX insite_sale_index_reservation_id on insite_sale (reservation_id ASC);

-- Table: insite_sale_table
CREATE TABLE insite_sale_table (
    id int  NOT NULL,
    insite_sale_id int  NOT NULL,
    table_id int  NOT NULL,
    CONSTRAINT insite_sale_table_pk PRIMARY KEY (id)
);

CREATE INDEX insite_sale_table_index_insite_sale_id on insite_sale_table (insite_sale_id ASC);

CREATE INDEX insite_sale_table_idex_table_id on insite_sale_table (table_id ASC);

-- Table: invoice
CREATE TABLE public.invoice (
    id int  NOT NULL,
    pay_date timestamp  NOT NULL,
    price money  NOT NULL DEFAULT 0.0,
    payment varchar(100)  NOT NULL,
    payment_code varchar(64)  NOT NULL,
    CONSTRAINT invoice_unique_payment_code UNIQUE (payment_code) NOT DEFERRABLE  INITIALLY IMMEDIATE,
    CONSTRAINT invoice_pk PRIMARY KEY (id)
);

-- Table: jwt_black_list
CREATE TABLE public.jwt_black_list (
    id bigint  NOT NULL,
    token varchar(256)  NOT NULL,
    expiration timestamp  NOT NULL,
    CONSTRAINT jwt_black_list_pk PRIMARY KEY (id)
);

-- Table: online_sale
CREATE TABLE online_sale (
    id int  NOT NULL,
    sale_id int  NOT NULL,
    CONSTRAINT online_sale_pk PRIMARY KEY (id)
);

CREATE INDEX online_sale_index_sale_id on online_sale (sale_id ASC);

-- Table: payment_option
CREATE TABLE payment_option (
    id int  NOT NULL,
    branch_id int  NOT NULL,
    name varchar(100)  NOT NULL,
    description text  NOT NULL,
    CONSTRAINT payment_option_pk PRIMARY KEY (id)
);

CREATE INDEX payment_option_index_branch_id on payment_option (branch_id ASC);

-- Table: product
CREATE TABLE public.product (
    id int  NOT NULL,
    product_sub_category_id int  NOT NULL,
    name varchar(100)  NOT NULL,
    price money  NOT NULL,
    description varchar(256)  NULL,
    disabled boolean  NOT NULL,
    CONSTRAINT product_unique_name UNIQUE (product_sub_category_id, name) NOT DEFERRABLE  INITIALLY IMMEDIATE,
    CONSTRAINT product_pk PRIMARY KEY (id)
);

CREATE INDEX product_index_product_sub_category_id on public.product (product_sub_category_id ASC);

-- Table: product_category
CREATE TABLE public.product_category (
    id int  NOT NULL,
    name varchar(100)  NOT NULL,
    CONSTRAINT product_category_unique_name UNIQUE (name) NOT DEFERRABLE  INITIALLY IMMEDIATE,
    CONSTRAINT product_category_pk PRIMARY KEY (id)
);

-- Table: product_sub_category
CREATE TABLE public.product_sub_category (
    id int  NOT NULL,
    branch_id int  NOT NULL,
    product_category_id int  NOT NULL,
    name varchar(100)  NOT NULL,
    CONSTRAINT product_sub_category_unique_name UNIQUE (branch_id, product_category_id, name) NOT DEFERRABLE  INITIALLY IMMEDIATE,
    CONSTRAINT product_sub_category_pk PRIMARY KEY (id)
);

CREATE INDEX product_sub_category_index_branch_id on public.product_sub_category (branch_id ASC);

CREATE INDEX product_sub_category_index_product_category_id on public.product_sub_category (product_category_id ASC);

-- Table: promotion
CREATE TABLE public.promotion (
    id int  NOT NULL,
    branch_id int  NOT NULL,
    text text  NOT NULL,
    disabled boolean  NOT NULL,
    CONSTRAINT promotion_pk PRIMARY KEY (id)
);

CREATE INDEX promotion_index_branch_id on public.promotion (branch_id ASC);

-- Table: reservation
CREATE TABLE public.reservation (
    id int  NOT NULL,
    branch_id int  NOT NULL,
    guest_id int  NULL,
    invoice_id int  NULL,
    request_date timestamp  NOT NULL,
    reservation_date_in timestamp  NOT NULL,
    reservation_date_out timestamp  NULL,
    price money  NOT NULL,
    status smallint  NOT NULL,
    table_number smallint  NOT NULL,
    client_number smallint  NOT NULL,
    occasion text  NOT NULL,
    by_client boolean  NOT NULL DEFAULT true,
    CONSTRAINT reservation_unique_invoice_id UNIQUE (invoice_id) NOT DEFERRABLE  INITIALLY IMMEDIATE,
    CONSTRAINT reservation_check_by_client_and_guest_id CHECK ((by_client = TRUE AND guest_id IS NULL) OR  (by_client = FALSE AND guest_id IS NOT NULL)) NOT DEFERRABLE INITIALLY IMMEDIATE,
    CONSTRAINT reservation_check_date_in_less_than_dat_out CHECK ((reservation_date_out IS NULL) OR (reservation_date_in < reservation_date_out)) NOT DEFERRABLE INITIALLY IMMEDIATE,
    CONSTRAINT reservation_pk PRIMARY KEY (id)
);

CREATE INDEX reservation_index_branch_id on public.reservation (branch_id ASC);

CREATE INDEX reservation_index_guest_id on public.reservation (guest_id ASC);

CREATE INDEX reservation_index_invoice_id on public.reservation (invoice_id ASC);

-- Table: review
CREATE TABLE public.review (
    id int  NOT NULL,
    client_id int  NOT NULL,
    branch_id int  NOT NULL,
    text text  NOT NULL,
    date timestamp  NOT NULL DEFAULT now(),
    CONSTRAINT review_pk PRIMARY KEY (id)
);

CREATE INDEX review_index_client_id on public.review (client_id ASC);

CREATE INDEX review_index_branch_id on public.review (branch_id ASC);

-- Table: review_like
CREATE TABLE public.review_like (
    id int  NOT NULL,
    client_id int  NOT NULL,
    review_id int  NOT NULL,
    CONSTRAINT review_like_pk PRIMARY KEY (id)
);

CREATE INDEX review_like_index_client_id on public.review_like (client_id ASC);

CREATE INDEX review_like_index_review_id on public.review_like (review_id ASC);

-- Table: role
CREATE TABLE public.role (
    id int  NOT NULL,
    name varchar(50)  NOT NULL,
    CONSTRAINT role_pk PRIMARY KEY (id)
);

-- Table: sale
CREATE TABLE sale (
    id int  NOT NULL,
    branch_id int  NOT NULL,
    client_guest_id int  NOT NULL,
    invoice_id int  NULL,
    client_quantity int  NOT NULL,
    status int  NOT NULL,
    start_time timestamp  NOT NULL,
    end_time timestamp  NULL,
    dollar_exchange decimal(16,4)  NOT NULL,
    note text  NOT NULL,
    CONSTRAINT sale_check_start_time_less_than_end_time CHECK (start_time < end_time) NOT DEFERRABLE INITIALLY IMMEDIATE,
    CONSTRAINT sale_pk PRIMARY KEY (id)
);

CREATE INDEX sale_index_client_guest_id on sale (client_guest_id ASC);

CREATE INDEX sale_index_branch_id on sale (branch_id ASC);

CREATE INDEX sale_index_invoice_id on sale (invoice_id ASC);

-- Table: sale_product
CREATE TABLE sale_product (
    id int  NOT NULL,
    sale_id int  NOT NULL,
    product_id int  NULL,
    name varchar(100)  NOT NULL,
    amount int  NOT NULL,
    price money  NOT NULL,
    CONSTRAINT sale_product_pk PRIMARY KEY (id)
);

CREATE INDEX sale_product_index_product_id on sale_product (product_id ASC);

CREATE INDEX sale_product_index_sale_id on sale_product (sale_id ASC);

-- Table: sale_tax
CREATE TABLE sale_tax (
    id int  NOT NULL,
    sale_id int  NOT NULL,
    tax_id int  NOT NULL,
    CONSTRAINT sale_tax_pk PRIMARY KEY (id)
);

CREATE INDEX sale_tax_index_sale_id on sale_tax (sale_id ASC);

CREATE INDEX sale_tax_index_tax_id on sale_tax (tax_id ASC);

-- Table: table
CREATE TABLE "table" (
    id int  NOT NULL,
    name varchar(4)  NOT NULL,
    branch_id int  NOT NULL,
    CONSTRAINT table_pk PRIMARY KEY (id)
);

CREATE INDEX table_index_branch_id on "table" (branch_id ASC);

-- Table: tax
CREATE TABLE tax (
    id int  NOT NULL,
    type smallint  NOT NULL,
    name varchar(128)  NOT NULL,
    value decimal(16,4)  NOT NULL,
    CONSTRAINT tax_pk PRIMARY KEY (id)
);

-- Table: tier
CREATE TABLE public.tier (
    id int  NOT NULL,
    name varchar(64)  NOT NULL,
    reservation_limit int  NOT NULL,
    tier_cost money  NOT NULL,
    CONSTRAINT tier_pk PRIMARY KEY (id)
);

-- Table: user
CREATE TABLE public."user" (
    id int  NOT NULL,
    role_id int  NOT NULL,
    email varchar(320)  NOT NULL,
    password varchar(64)  NOT NULL,
    verified boolean  NOT NULL,
    provider varchar(64)  NULL,
    provider_id varchar(64)  NULL,
    CONSTRAINT user_unique_email UNIQUE (email) NOT DEFERRABLE  INITIALLY IMMEDIATE,
    CONSTRAINT user_unique_provider_id UNIQUE (provider_id) NOT DEFERRABLE  INITIALLY IMMEDIATE,
    CONSTRAINT user_pk PRIMARY KEY (id)
);

CREATE INDEX user_index_role_id on public."user" (role_id ASC);

CREATE INDEX user_index_email on public."user" (email ASC);

-- foreign keys
-- Reference: business_tier (table: business)
ALTER TABLE public.business ADD CONSTRAINT business_tier
    FOREIGN KEY (tier_id)
    REFERENCES public.tier (id)
    ON DELETE  RESTRICT  
    NOT DEFERRABLE 
    INITIALLY IMMEDIATE
;

-- Reference: client_group_client (table: client_group)
ALTER TABLE public.client_group ADD CONSTRAINT client_group_client
    FOREIGN KEY (client_id)
    REFERENCES public.client (id)
    ON DELETE  RESTRICT  
    NOT DEFERRABLE 
    INITIALLY IMMEDIATE
;

-- Reference: client_guest_client (table: client_guest)
ALTER TABLE client_guest ADD CONSTRAINT client_guest_client
    FOREIGN KEY (client_id)
    REFERENCES public.client (id)
    ON DELETE  RESTRICT  
    NOT DEFERRABLE 
    INITIALLY IMMEDIATE
;

-- Reference: client_guest_guest (table: client_guest)
ALTER TABLE client_guest ADD CONSTRAINT client_guest_guest
    FOREIGN KEY (guest_id)
    REFERENCES public.guest (id)
    ON DELETE  RESTRICT  
    NOT DEFERRABLE 
    INITIALLY IMMEDIATE
;

-- Reference: client_user (table: client)
ALTER TABLE public.client ADD CONSTRAINT client_user
    FOREIGN KEY (user_id)
    REFERENCES public."user" (id)
    ON DELETE  RESTRICT  
    NOT DEFERRABLE 
    INITIALLY IMMEDIATE
;

-- Reference: default_tax_branch (table: default_tax)
ALTER TABLE default_tax ADD CONSTRAINT default_tax_branch
    FOREIGN KEY (branch_id)
    REFERENCES public.branch (id)
    ON DELETE  CASCADE  
    NOT DEFERRABLE 
    INITIALLY IMMEDIATE
;

-- Reference: default_tax_tax (table: default_tax)
ALTER TABLE default_tax ADD CONSTRAINT default_tax_tax
    FOREIGN KEY (tax_id)
    REFERENCES tax (id)
    ON DELETE  CASCADE  
    NOT DEFERRABLE 
    INITIALLY IMMEDIATE
;

-- Reference: favorite_sede_client (table: favorite_branch)
ALTER TABLE public.favorite_branch ADD CONSTRAINT favorite_sede_client
    FOREIGN KEY (client_id)
    REFERENCES public.client (id)
    ON DELETE  CASCADE  
    NOT DEFERRABLE 
    INITIALLY IMMEDIATE
;

-- Reference: favorite_sede_sede (table: favorite_branch)
ALTER TABLE public.favorite_branch ADD CONSTRAINT favorite_sede_sede
    FOREIGN KEY (branch_id)
    REFERENCES public.branch (id)
    ON DELETE  CASCADE  
    NOT DEFERRABLE 
    INITIALLY IMMEDIATE
;

-- Reference: friend_client_addresser (table: friend)
ALTER TABLE public.friend ADD CONSTRAINT friend_client_addresser
    FOREIGN KEY (client_addresser_id)
    REFERENCES public.client (id)
    ON DELETE  CASCADE  
    NOT DEFERRABLE 
    INITIALLY IMMEDIATE
;

-- Reference: friend_client_requester (table: friend)
ALTER TABLE public.friend ADD CONSTRAINT friend_client_requester
    FOREIGN KEY (client_requester_id)
    REFERENCES public.client (id)
    ON DELETE  CASCADE  
    NOT DEFERRABLE 
    INITIALLY IMMEDIATE
;

-- Reference: insite_sale_table_insite_sale (table: insite_sale_table)
ALTER TABLE insite_sale_table ADD CONSTRAINT insite_sale_table_insite_sale
    FOREIGN KEY (insite_sale_id)
    REFERENCES insite_sale (id)
    ON DELETE  CASCADE  
    NOT DEFERRABLE 
    INITIALLY IMMEDIATE
;

-- Reference: insite_sale_table_table (table: insite_sale_table)
ALTER TABLE insite_sale_table ADD CONSTRAINT insite_sale_table_table
    FOREIGN KEY (table_id)
    REFERENCES "table" (id)
    ON DELETE  CASCADE  
    NOT DEFERRABLE 
    INITIALLY IMMEDIATE
;

-- Reference: offline_sale_reservation (table: insite_sale)
ALTER TABLE insite_sale ADD CONSTRAINT offline_sale_reservation
    FOREIGN KEY (reservation_id)
    REFERENCES public.reservation (id)
    ON DELETE  RESTRICT  
    NOT DEFERRABLE 
    INITIALLY IMMEDIATE
;

-- Reference: offline_sale_sale (table: insite_sale)
ALTER TABLE insite_sale ADD CONSTRAINT offline_sale_sale
    FOREIGN KEY (sale_id)
    REFERENCES sale (id)
    ON DELETE  CASCADE  
    NOT DEFERRABLE 
    INITIALLY IMMEDIATE
;

-- Reference: online_sale_sale (table: online_sale)
ALTER TABLE online_sale ADD CONSTRAINT online_sale_sale
    FOREIGN KEY (sale_id)
    REFERENCES sale (id)
    ON DELETE  CASCADE  
    NOT DEFERRABLE 
    INITIALLY IMMEDIATE
;

-- Reference: payment_option_branch (table: payment_option)
ALTER TABLE payment_option ADD CONSTRAINT payment_option_branch
    FOREIGN KEY (branch_id)
    REFERENCES public.branch (id)
    ON DELETE  RESTRICT  
    NOT DEFERRABLE 
    INITIALLY IMMEDIATE
;

-- Reference: product_product_sub_category (table: product)
ALTER TABLE public.product ADD CONSTRAINT product_product_sub_category
    FOREIGN KEY (product_sub_category_id)
    REFERENCES public.product_sub_category (id)
    ON DELETE  CASCADE  
    NOT DEFERRABLE 
    INITIALLY IMMEDIATE
;

-- Reference: product_sub_category_branch (table: product_sub_category)
ALTER TABLE public.product_sub_category ADD CONSTRAINT product_sub_category_branch
    FOREIGN KEY (branch_id)
    REFERENCES public.branch (id)
    ON DELETE  CASCADE  
    NOT DEFERRABLE 
    INITIALLY IMMEDIATE
;

-- Reference: product_sub_category_product_category (table: product_sub_category)
ALTER TABLE public.product_sub_category ADD CONSTRAINT product_sub_category_product_category
    FOREIGN KEY (product_category_id)
    REFERENCES public.product_category (id)
    ON DELETE  CASCADE  
    NOT DEFERRABLE 
    INITIALLY IMMEDIATE
;

-- Reference: reservation_client_group (table: client_group)
ALTER TABLE public.client_group ADD CONSTRAINT reservation_client_group
    FOREIGN KEY (reservation_id)
    REFERENCES public.reservation (id)
    ON DELETE  RESTRICT  
    NOT DEFERRABLE 
    INITIALLY IMMEDIATE
;

-- Reference: reservation_guest (table: reservation)
ALTER TABLE public.reservation ADD CONSTRAINT reservation_guest
    FOREIGN KEY (guest_id)
    REFERENCES public.guest (id)
    ON DELETE  RESTRICT  
    NOT DEFERRABLE 
    INITIALLY IMMEDIATE
;

-- Reference: reservation_invoice (table: reservation)
ALTER TABLE public.reservation ADD CONSTRAINT reservation_invoice
    FOREIGN KEY (invoice_id)
    REFERENCES public.invoice (id)  
    NOT DEFERRABLE 
    INITIALLY IMMEDIATE
;

-- Reference: reservation_sede (table: reservation)
ALTER TABLE public.reservation ADD CONSTRAINT reservation_sede
    FOREIGN KEY (branch_id)
    REFERENCES public.branch (id)
    ON DELETE  SET NULL  
    NOT DEFERRABLE 
    INITIALLY IMMEDIATE
;

-- Reference: review_client (table: review)
ALTER TABLE public.review ADD CONSTRAINT review_client
    FOREIGN KEY (client_id)
    REFERENCES public.client (id)
    ON DELETE  CASCADE  
    NOT DEFERRABLE 
    INITIALLY IMMEDIATE
;

-- Reference: review_list_client (table: review_like)
ALTER TABLE public.review_like ADD CONSTRAINT review_list_client
    FOREIGN KEY (client_id)
    REFERENCES public.client (id)
    ON DELETE  CASCADE  
    NOT DEFERRABLE 
    INITIALLY IMMEDIATE
;

-- Reference: review_list_review (table: review_like)
ALTER TABLE public.review_like ADD CONSTRAINT review_list_review
    FOREIGN KEY (review_id)
    REFERENCES public.review (id)
    ON DELETE  CASCADE  
    NOT DEFERRABLE 
    INITIALLY IMMEDIATE
;

-- Reference: review_sede (table: review)
ALTER TABLE public.review ADD CONSTRAINT review_sede
    FOREIGN KEY (branch_id)
    REFERENCES public.branch (id)
    ON DELETE  CASCADE  
    NOT DEFERRABLE 
    INITIALLY IMMEDIATE
;

-- Reference: role_user (table: user)
ALTER TABLE public."user" ADD CONSTRAINT role_user
    FOREIGN KEY (role_id)
    REFERENCES public.role (id)
    ON DELETE  RESTRICT  
    NOT DEFERRABLE 
    INITIALLY IMMEDIATE
;

-- Reference: sale_branch (table: sale)
ALTER TABLE sale ADD CONSTRAINT sale_branch
    FOREIGN KEY (branch_id)
    REFERENCES public.branch (id)  
    NOT DEFERRABLE 
    INITIALLY IMMEDIATE
;

-- Reference: sale_client_guest (table: sale)
ALTER TABLE sale ADD CONSTRAINT sale_client_guest
    FOREIGN KEY (client_guest_id)
    REFERENCES client_guest (id)
    ON DELETE  RESTRICT  
    NOT DEFERRABLE 
    INITIALLY IMMEDIATE
;

-- Reference: sale_invoice (table: sale)
ALTER TABLE sale ADD CONSTRAINT sale_invoice
    FOREIGN KEY (invoice_id)
    REFERENCES public.invoice (id)  
    NOT DEFERRABLE 
    INITIALLY IMMEDIATE
;

-- Reference: sale_product_product (table: sale_product)
ALTER TABLE sale_product ADD CONSTRAINT sale_product_product
    FOREIGN KEY (product_id)
    REFERENCES public.product (id)
    ON DELETE  SET NULL  
    NOT DEFERRABLE 
    INITIALLY IMMEDIATE
;

-- Reference: sale_product_sale (table: sale_product)
ALTER TABLE sale_product ADD CONSTRAINT sale_product_sale
    FOREIGN KEY (sale_id)
    REFERENCES sale (id)
    ON DELETE  CASCADE  
    NOT DEFERRABLE 
    INITIALLY IMMEDIATE
;

-- Reference: sale_tax_tax (table: sale_tax)
ALTER TABLE sale_tax ADD CONSTRAINT sale_tax_tax
    FOREIGN KEY (tax_id)
    REFERENCES tax (id)
    ON DELETE  CASCADE  
    NOT DEFERRABLE 
    INITIALLY IMMEDIATE
;

-- Reference: sede_business (table: branch)
ALTER TABLE public.branch ADD CONSTRAINT sede_business
    FOREIGN KEY (business_id)
    REFERENCES public.business (id)
    ON DELETE  RESTRICT  
    NOT DEFERRABLE 
    INITIALLY IMMEDIATE
;

-- Reference: sede_preferences_list_sede (table: branch_amenity)
ALTER TABLE public.branch_amenity ADD CONSTRAINT sede_preferences_list_sede
    FOREIGN KEY (branch_id)
    REFERENCES public.branch (id)
    ON DELETE  CASCADE  
    NOT DEFERRABLE 
    INITIALLY IMMEDIATE
;

-- Reference: sede_preferences_list_sede_preferences (table: branch_amenity)
ALTER TABLE public.branch_amenity ADD CONSTRAINT sede_preferences_list_sede_preferences
    FOREIGN KEY (amenity_id)
    REFERENCES public.amenity (id)
    ON DELETE  CASCADE  
    NOT DEFERRABLE 
    INITIALLY IMMEDIATE
;

-- Reference: sede_promotion (table: promotion)
ALTER TABLE public.promotion ADD CONSTRAINT sede_promotion
    FOREIGN KEY (branch_id)
    REFERENCES public.branch (id)
    ON DELETE  CASCADE  
    NOT DEFERRABLE 
    INITIALLY IMMEDIATE
;

-- Reference: table_branch (table: table)
ALTER TABLE "table" ADD CONSTRAINT table_branch
    FOREIGN KEY (branch_id)
    REFERENCES public.branch (id)
    ON DELETE  CASCADE  
    NOT DEFERRABLE 
    INITIALLY IMMEDIATE
;

-- Reference: tax_sale (table: sale_tax)
ALTER TABLE sale_tax ADD CONSTRAINT tax_sale
    FOREIGN KEY (sale_id)
    REFERENCES sale (id)
    ON DELETE  RESTRICT  
    NOT DEFERRABLE 
    INITIALLY IMMEDIATE
;

-- Reference: user_business (table: business)
ALTER TABLE public.business ADD CONSTRAINT user_business
    FOREIGN KEY (user_id)
    REFERENCES public."user" (id)
    ON DELETE  RESTRICT  
    NOT DEFERRABLE 
    INITIALLY IMMEDIATE
;

-- sequences
-- Sequence: amenity_seq
CREATE SEQUENCE public.amenity_seq
      INCREMENT BY 1
      NO MINVALUE
      NO MAXVALUE
      START WITH 1000
      CACHE 1
      NO CYCLE
      AS bigint
;

-- Sequence: branch_amenity_seq
CREATE SEQUENCE public.branch_amenity_seq
      INCREMENT BY 1
      NO MINVALUE
      NO MAXVALUE
      START WITH 1000
      CACHE 1
      NO CYCLE
      AS bigint
;

-- Sequence: branch_seq
CREATE SEQUENCE public.branch_seq
      INCREMENT BY 1
      NO MINVALUE
      NO MAXVALUE
      START WITH 1000
      CACHE 1
      NO CYCLE
      AS bigint
;

-- Sequence: business_seq
CREATE SEQUENCE public.business_seq
      INCREMENT BY 1
      NO MINVALUE
      NO MAXVALUE
      START WITH 1000
      CACHE 1
      NO CYCLE
      AS bigint
;

-- Sequence: client_group_seq
CREATE SEQUENCE public.client_group_seq
      INCREMENT BY 1
      NO MINVALUE
      NO MAXVALUE
      START WITH 1000
      CACHE 1
      NO CYCLE
      AS bigint
;

-- Sequence: client_guest_seq
CREATE SEQUENCE client_guest_seq
      INCREMENT BY 1
      NO MINVALUE
      NO MAXVALUE
      START WITH 1000
      CACHE 1
      NO CYCLE
;

-- Sequence: client_seq
CREATE SEQUENCE public.client_seq
      INCREMENT BY 1
      NO MINVALUE
      NO MAXVALUE
      START WITH 1000
      CACHE 1
      NO CYCLE
      AS bigint
;

-- Sequence: default_tax_seq
CREATE SEQUENCE default_tax_seq
      INCREMENT BY 1
      NO MINVALUE
      NO MAXVALUE
      START WITH 1000
      CACHE 1
      NO CYCLE
;

-- Sequence: favorite_branch_seq
CREATE SEQUENCE public.favorite_branch_seq
      INCREMENT BY 1
      NO MINVALUE
      NO MAXVALUE
      START WITH 1000
      CACHE 1
      NO CYCLE
      AS bigint
;

-- Sequence: friend_seq
CREATE SEQUENCE public.friend_seq
      INCREMENT BY 1
      NO MINVALUE
      NO MAXVALUE
      START WITH 1000
      CACHE 1
      NO CYCLE
      AS bigint
;

-- Sequence: guest_seq
CREATE SEQUENCE public.guest_seq
      INCREMENT BY 1
      NO MINVALUE
      NO MAXVALUE
      START WITH 1000
      CACHE 1
      NO CYCLE
      AS bigint
;

-- Sequence: insite_sale_seq
CREATE SEQUENCE insite_sale_seq
      INCREMENT BY 1
      NO MINVALUE
      NO MAXVALUE
      START WITH 1000
      CACHE 1
      NO CYCLE
;

-- Sequence: insite_sale_table_seq
CREATE SEQUENCE insite_sale_table_seq
      INCREMENT BY 1
      NO MINVALUE
      NO MAXVALUE
      START WITH 1000
      CACHE 1
      NO CYCLE
;

-- Sequence: invoice_seq
CREATE SEQUENCE public.invoice_seq
      INCREMENT BY 1
      NO MINVALUE
      NO MAXVALUE
      START WITH 1000
      CACHE 1
      NO CYCLE
      AS bigint
;

-- Sequence: jwt_black_list_seq
CREATE SEQUENCE public.jwt_black_list_seq
      INCREMENT BY 1
      NO MINVALUE
      NO MAXVALUE
      START WITH 1000
      CACHE 1
      CYCLE
      AS bigint
;

-- Sequence: online_sale_seq
CREATE SEQUENCE online_sale_seq
      INCREMENT BY 1
      NO MINVALUE
      NO MAXVALUE
      START WITH 1000
      CACHE 1
      NO CYCLE
;

-- Sequence: payment_option_seq
CREATE SEQUENCE payment_option_seq
      INCREMENT BY 1
      NO MINVALUE
      NO MAXVALUE
      START WITH 1000
      CACHE 1
      NO CYCLE
;

-- Sequence: product_category_seq
CREATE SEQUENCE public.product_category_seq
      INCREMENT BY 1
      NO MINVALUE
      NO MAXVALUE
      START WITH 1000
      CACHE 1
      NO CYCLE
      AS bigint
;

-- Sequence: product_seq
CREATE SEQUENCE public.product_seq
      INCREMENT BY 1
      NO MINVALUE
      NO MAXVALUE
      START WITH 1000
      CACHE 1
      NO CYCLE
      AS bigint
;

-- Sequence: product_sub_category_seq
CREATE SEQUENCE public.product_sub_category_seq
      INCREMENT BY 1
      NO MINVALUE
      NO MAXVALUE
      START WITH 1000
      CACHE 1
      NO CYCLE
      AS bigint
;

-- Sequence: promotion_seq
CREATE SEQUENCE public.promotion_seq
      INCREMENT BY 1
      NO MINVALUE
      NO MAXVALUE
      START WITH 1000
      CACHE 1
      NO CYCLE
      AS bigint
;

-- Sequence: reservation_seq
CREATE SEQUENCE public.reservation_seq
      INCREMENT BY 1
      NO MINVALUE
      NO MAXVALUE
      START WITH 1000
      CACHE 1
      NO CYCLE
      AS bigint
;

-- Sequence: review_like_seq
CREATE SEQUENCE public.review_like_seq
      INCREMENT BY 1
      NO MINVALUE
      NO MAXVALUE
      START WITH 1000
      CACHE 1
      NO CYCLE
      AS bigint
;

-- Sequence: review_seq
CREATE SEQUENCE public.review_seq
      INCREMENT BY 1
      NO MINVALUE
      NO MAXVALUE
      START WITH 1000
      CACHE 1
      NO CYCLE
      AS bigint
;

-- Sequence: role_seq
CREATE SEQUENCE public.role_seq
      INCREMENT BY 1
      NO MINVALUE
      NO MAXVALUE
      START WITH 1000
      CACHE 1
      NO CYCLE
      AS bigint
;

-- Sequence: sale_product_seq
CREATE SEQUENCE sale_product_seq
      NO MINVALUE
      NO MAXVALUE
      START WITH 1000
      CACHE 1
      NO CYCLE
;

-- Sequence: sale_seq
CREATE SEQUENCE sale_seq
      INCREMENT BY 1
      NO MINVALUE
      NO MAXVALUE
      START WITH 1000
      CACHE 1
      NO CYCLE
;

-- Sequence: sale_tax_seq
CREATE SEQUENCE sale_tax_seq
      INCREMENT BY 1
      NO MINVALUE
      NO MAXVALUE
      START WITH 1000
      CACHE 1
      NO CYCLE
;

-- Sequence: table_seq
CREATE SEQUENCE table_seq
      INCREMENT BY 1
      NO MINVALUE
      NO MAXVALUE
      START WITH 1000
      CACHE 1
      NO CYCLE
;

-- Sequence: tax_seq
CREATE SEQUENCE tax_seq
      NO MINVALUE
      NO MAXVALUE
      START WITH 1000
      CACHE 1
      NO CYCLE
;

-- Sequence: tier_seq
CREATE SEQUENCE tier_seq
      INCREMENT BY 1
      NO MINVALUE
      NO MAXVALUE
      START WITH 1000
      CACHE 1
      NO CYCLE
;

-- Sequence: user_seq
CREATE SEQUENCE user_seq
      INCREMENT BY 1
      NO MINVALUE
      NO MAXVALUE
      START WITH 1000
      CACHE 1
      NO CYCLE
;

CREATE EXTENSION IF NOT EXISTS pg_trgm;;

-- End of file.

