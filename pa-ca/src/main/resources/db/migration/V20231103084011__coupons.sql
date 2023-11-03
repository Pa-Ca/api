-- Created by Vertabelo (http://vertabelo.com)
-- Last modification date: 2023-11-03 13:37:05.814

-- tables
-- Table: coupon
CREATE TABLE public.coupon (
    id int  NOT NULL,
    type smallint  NOT NULL,
    value decimal(16,4)  NOT NULL,
    description text  NOT NULL,
    start_date timestamp  NOT NULL,
    end_date timestamp  NOT NULL,
    CONSTRAINT coupon_pk PRIMARY KEY (id)
);

-- Table: product_category_coupon_item
CREATE TABLE public.product_category_coupon_item (
    id int  NOT NULL,
    product_category_id int  NOT NULL,
    coupon_id int  NOT NULL,
    branch_id int  NOT NULL,
    CONSTRAINT product_category_coupon_item_pk PRIMARY KEY (id)
);

CREATE INDEX product_category_coupon_item_index_product_category_id on public.product_category_coupon_item (product_category_id ASC);

CREATE INDEX product_category_coupon_item_index_coupon_id on public.product_category_coupon_item (coupon_id ASC);

CREATE INDEX product_category_coupon_item_index_branch_id on public.product_category_coupon_item (branch_id ASC);

-- Table: product_coupon_item
CREATE TABLE public.product_coupon_item (
    id int  NULL,
    product_id int  NOT NULL,
    coupon_id int  NULL,
    CONSTRAINT product_coupon_item_pk PRIMARY KEY (id)
);

CREATE INDEX product_coupon_item_index_product_id on public.product_coupon_item (product_id ASC);

CREATE INDEX product_coupon_item_index_coupon_id on public.product_coupon_item (coupon_id ASC);

-- Table: product_sub_category_coupon_item
CREATE TABLE public.product_sub_category_coupon_item (
    id int  NULL,
    product_sub_category_id int  NOT NULL,
    coupon_id int  NOT NULL,
    CONSTRAINT product_coupon_item_pk PRIMARY KEY (id)
);

CREATE INDEX product_sub_category_coupon_item_index_product_sub_category_id on public.product_sub_category_coupon_item (product_sub_category_id ASC);

CREATE INDEX product_sub_category_coupon_item_idx_coupon_id on public.product_sub_category_coupon_item (coupon_id ASC);

-- foreign keys
-- Reference: coupon_product_coupon_item (table: product_coupon_item)
ALTER TABLE public.product_coupon_item ADD CONSTRAINT coupon_product_coupon_item
    FOREIGN KEY (coupon_id)
    REFERENCES public.coupon (id)
    ON DELETE  CASCADE
    NOT DEFERRABLE
    INITIALLY IMMEDIATE
;

-- Reference: coupon_product_sub_category_coupon_item (table: product_sub_category_coupon_item)
ALTER TABLE public.product_sub_category_coupon_item ADD CONSTRAINT coupon_product_sub_category_coupon_item
    FOREIGN KEY (coupon_id)
    REFERENCES public.coupon (id)
    ON DELETE  CASCADE
    NOT DEFERRABLE
    INITIALLY IMMEDIATE
;

-- Reference: product_category_coupon_item_branch (table: product_category_coupon_item)
ALTER TABLE public.product_category_coupon_item ADD CONSTRAINT product_category_coupon_item_branch
    FOREIGN KEY (branch_id)
    REFERENCES public.branch (id)
    ON DELETE  CASCADE
    NOT DEFERRABLE
    INITIALLY IMMEDIATE
;

-- Reference: product_category_coupon_item_coupon (table: product_category_coupon_item)
ALTER TABLE public.product_category_coupon_item ADD CONSTRAINT product_category_coupon_item_coupon
    FOREIGN KEY (coupon_id)
    REFERENCES public.coupon (id)
    ON DELETE  CASCADE
    NOT DEFERRABLE
    INITIALLY IMMEDIATE
;

-- Reference: product_category_coupon_item_product_category (table: product_category_coupon_item)
ALTER TABLE public.product_category_coupon_item ADD CONSTRAINT product_category_coupon_item_product_category
    FOREIGN KEY (product_category_id)
    REFERENCES public.product_category (id)
    ON DELETE  CASCADE
    NOT DEFERRABLE
    INITIALLY IMMEDIATE
;

-- Reference: product_coupon_item_product (table: product_coupon_item)
ALTER TABLE public.product_coupon_item ADD CONSTRAINT product_coupon_item_product
    FOREIGN KEY (product_id)
    REFERENCES public.product (id)
    ON DELETE  CASCADE
    NOT DEFERRABLE
    INITIALLY IMMEDIATE
;

-- Reference: product_sub_category_coupon_item_product_sub_category (table: product_sub_category_coupon_item)
ALTER TABLE public.product_sub_category_coupon_item ADD CONSTRAINT product_sub_category_coupon_item_product_sub_category
    FOREIGN KEY (product_sub_category_id)
    REFERENCES public.product_sub_category (id)
    ON DELETE  CASCADE
    NOT DEFERRABLE
    INITIALLY IMMEDIATE
;

-- sequences
-- Sequence: coupon_seq
CREATE SEQUENCE public.coupon_seq
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    START WITH 1000
    CACHE 1
    NO CYCLE
    AS bigint
;

-- Sequence: product_category_coupon_item
CREATE SEQUENCE public.product_category_coupon_item
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    START WITH 1000
    CACHE 1
    NO CYCLE
    AS bigint
;

-- Sequence: product_coupon_item
CREATE SEQUENCE public.product_coupon_item
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    START WITH 1000
    CACHE 1
    NO CYCLE
    AS bigint
;

-- Sequence: product_sub_category_coupon_item
CREATE SEQUENCE public.product_sub_category_coupon_item
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    START WITH 1000
    CACHE 1
    NO CYCLE
    AS bigint
;

-- End of file.

