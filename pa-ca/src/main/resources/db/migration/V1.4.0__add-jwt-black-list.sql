--Date: 2023-02-08

CREATE TABLE jwt_black_list (
    id INT NOT NULL,
    token VARCHAR(256) NOT NULL,
    expiration DATE NOT NULL,
    CONSTRAINT jwt_black_list_pk PRIMARY KEY (id)
);

CREATE SEQUENCE IF NOT EXISTS jwt_black_list_seq MINVALUE 1 START WITH 1000 INCREMENT BY 1;