-- Created by Vertabelo (http://vertabelo.com)
-- Last modification date: 2023-01-05 01:20:24.078

-- tables
-- Table: amenity
CREATE TABLE amenity (
    id int  NOT NULL,
    name varchar(100)  NOT NULL,
    CONSTRAINT amenity_pk PRIMARY KEY (id)
);

-- Table: branch
CREATE TABLE branch (
    id int  NOT NULL,
    business_id int  NOT NULL,
    client_id int  NOT NULL,
    address varchar(1024)  NOT NULL,
    coordiantes varchar(1024)  NOT NULL,
    name varchar(1024)  NOT NULL,
    overview varchar(1024)  NOT NULL,
    score int  NOT NULL,
    capacity int  NOT NULL,
    CONSTRAINT branch_pk PRIMARY KEY (id)
);

-- Table: branch_amenity
CREATE TABLE branch_amenity (
    id int  NOT NULL,
    branch_id int  NOT NULL,
    amenity_id int  NOT NULL,
    CONSTRAINT branch_amenity_pk PRIMARY KEY (id)
);

-- Table: business
CREATE TABLE business (
    id int  NOT NULL,
    user_id int  NOT NULL,
    name varchar(50)  NOT NULL,
    verified boolean  NOT NULL,
    CONSTRAINT business_pk PRIMARY KEY (id)
);

-- Table: client
CREATE TABLE client (
    id int  NOT NULL,
    user_id int  NOT NULL,
    name varchar(50)  NOT NULL,
    surname varchar(50)  NOT NULL,
    stripe_customer_id varchar(100)  NOT NULL,
    phone_number int  NOT NULL,
    address varchar(1024)  NOT NULL,
    date_of_birth date  NOT NULL,
    CONSTRAINT client_pk PRIMARY KEY (id)
);

-- Table: client_group
CREATE TABLE client_group (
    id int  NOT NULL,
    client_id int  NOT NULL,
    reservation_id int  NOT NULL,
    name varchar(100)  NOT NULL,
    CONSTRAINT client_group_pk PRIMARY KEY (id)
);

-- Table: favorite_branch
CREATE TABLE favorite_branch (
    id int  NOT NULL,
    client_id int  NOT NULL,
    branch_id int  NOT NULL,
    CONSTRAINT favorite_branch_pk PRIMARY KEY (id)
);

-- Table: friend
CREATE TABLE friend (
    id int  NOT NULL,
    client_requester_id int  NOT NULL,
    client_addressee_id int  NOT NULL,
    accepted boolean  NOT NULL,
    CONSTRAINT friend_pk PRIMARY KEY (id)
);

-- Table: invoice
CREATE TABLE invoice (
    id int  NOT NULL,
    reservation_id int  NOT NULL,
    creation_date timestamp  NOT NULL,
    CONSTRAINT invoice_pk PRIMARY KEY (id)
);

-- Table: product
CREATE TABLE product (
    id int  NOT NULL,
    branch_id int  NOT NULL,
    disabled boolean  NOT NULL,
    CONSTRAINT product_pk PRIMARY KEY (id)
);

-- Table: promotion
CREATE TABLE promotion (
    id int  NOT NULL,
    branch_id int  NOT NULL,
    text varchar(100)  NOT NULL,
    disabled boolean  NOT NULL,
    CONSTRAINT promotion_pk PRIMARY KEY (id)
);

-- Table: reservation
CREATE TABLE reservation (
    id int  NOT NULL,
    branch_id int  NOT NULL,
    request_date timestamp  NOT NULL,
    reservation_date timestamp  NOT NULL,
    client_number int  NOT NULL,
    payment varchar(50)  NOT NULL,
    CONSTRAINT reservation_pk PRIMARY KEY (id)
);

-- Table: review
CREATE TABLE review (
    id int  NOT NULL,
    client_id int  NOT NULL,
    branch_id int  NOT NULL,
    text varchar(100)  NOT NULL,
    CONSTRAINT review_pk PRIMARY KEY (id)
);

-- Table: review_like
CREATE TABLE review_like (
    id int  NOT NULL,
    client_id int  NOT NULL,
    review_id int  NOT NULL,
    CONSTRAINT review_like_pk PRIMARY KEY (id)
);

-- Table: role
CREATE TABLE role (
    id int  NOT NULL,
    name varchar(50)  NOT NULL,
    CONSTRAINT role_pk PRIMARY KEY (id)
);

-- Table: user
CREATE TABLE "user" (
    id int  NOT NULL,
    email varchar(100)  NOT NULL,
    password varchar(64)  NOT NULL,
    verified boolean  NOT NULL,
    role_id int  NOT NULL,
    logged_in boolean  NOT NULL,
    CONSTRAINT user_pk PRIMARY KEY (id)
);

-- End of file.

