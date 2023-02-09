-- Clients

INSERT INTO "user" (id, email, password, verified, role_id, logged_in)
    VALUES 
        (1, 'user1@email.com', 'password1', false, 1, false),
        (2, 'user2@email.com', 'password2', false, 1, false),
        (3, 'user3@email.com', 'password3', false, 1, false),
        (4, 'user4@email.com', 'password4', false, 1, false),
        (5, 'user5@email.com', 'password5', false, 1, false),
        (6, 'user6@email.com', 'password6', false, 1, false),
        (7, 'user7@email.com', 'password7', false, 1, false),
        (8, 'user8@email.com', 'password8', false, 1, false),
        (9, 'user9@email.com', 'password9', false, 1, false),
        (10, 'user10@email.com', 'password10', false, 1, false),
        (11, 'user11@email.com', 'password11', false, 1, false),
        (12, 'user12@email.com', 'password12', false, 1, false),
        (13, 'user13@email.com', 'password13', false, 1, false),
        (14, 'user14@email.com', 'password14', false, 1, false),
        (15, 'user15@email.com', 'password15', false, 1, false),
        (16, 'user16@email.com', 'password16', false, 1, false),
        (17, 'user17@email.com', 'password17', false, 1, false),
        (18, 'user18@email.com', 'password18', false, 1, false),
        (19, 'user19@email.com', 'password19', false, 1, false),
        (20, 'user20@email.com', 'password20', false, 1, false);

INSERT INTO client (id, user_id, name, surname, stripe_customer_id, phone_number, address, date_of_birth)
    VALUES 
        (1, 1, 'name1', 'surname1', 'stripe1', 'phone1', 'address1', '1980-01-01'),
        (2, 2, 'name2', 'surname2', 'stripe2', 'phone2', 'address2', '1980-02-01'),
        (3, 3, 'name3', 'surname3', 'stripe3', 'phone3', 'address3', '1980-03-01'),
        (4, 4, 'name4', 'surname4', 'stripe4', 'phone4', 'address4', '1980-04-01'),
        (5, 5, 'name5', 'surname5', 'stripe5', 'phone5', 'address5', '1980-05-01'),
        (6, 6, 'name6', 'surname6', 'stripe6', 'phone6', 'address6', '1980-06-01'),
        (7, 7, 'name7', 'surname7', 'stripe7', 'phone7', 'address7', '1980-07-01'),
        (8, 8, 'name8', 'surname8', 'stripe8', 'phone8', 'address8', '1980-08-01'),
        (9, 9, 'name9', 'surname9', 'stripe9', 'phone9', 'address9', '1980-09-01'),
        (10, 10, 'name10', 'surname10', 'stripe10', 'phone10', 'address10', '1980-10-01'),
        (11, 11, 'name11', 'surname11', 'stripe11', 'phone11', 'address11', '1980-11-01'),
        (12, 12, 'name12', 'surname12', 'stripe12', 'phone12', 'address12', '1980-12-01'),
        (13, 13, 'name13', 'surname13', 'stripe13', 'phone13', 'address13', '1981-01-01'),
        (14, 14, 'name14', 'surname14', 'stripe14', 'phone14', 'address14', '1981-02-01'),
        (15, 15, 'name15', 'surname15', 'stripe15', 'phone15', 'address15', '1981-03-01'),
        (16, 16, 'name16', 'surname16', 'stripe16', 'phone16', 'address16', '1981-04-01'),
        (17, 17, 'name17', 'surname17', 'stripe17', 'phone17', 'address17', '1981-05-01'),
        (18, 18, 'name18', 'surname18', 'stripe18', 'phone18', 'address18', '1981-06-01'),
        (19, 19, 'name19', 'surname19', 'stripe19', 'phone19', 'address19', '1981-07-01'),
        (20, 20, 'name20', 'surname20', 'stripe20', 'phone20', 'address20', '1981-08-01');