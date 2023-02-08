INSERT INTO reservation (id, branch_id, request_date, reservation_date, client_number, payment, status, pay_date, price)
    VALUES
        (1, 1,   '2023-02-06 10:00:00', '2023-02-07 10:00:00', 1, 'VISA',       1, '2023-02-06 10:30:00', 200),
        (2, 2,   '2023-02-06 11:00:00', '2023-02-07 11:00:00', 1, 'Mastercard', 1, '2023-02-06 11:30:00', 300),
        (3, 3,   '2023-02-06 12:00:00', '2023-02-07 12:00:00', 1, 'Paypal',     1, '2023-02-06 12:30:00', 400),
        (4, 4,   '2023-02-06 13:00:00', '2023-02-07 13:00:00', 1, 'VISA',       1, '2023-02-06 13:30:00', 500),
        (5, 5,   '2023-02-06 14:00:00', '2023-02-07 14:00:00', 1, 'Mastercard', 1, '2023-02-06 14:30:00', 600),
        (6, 6,   '2023-02-06 15:00:00', '2023-02-07 15:00:00', 1, 'Paypal',     1, '2023-02-06 15:30:00', 700),
        (7, 7,   '2023-02-06 16:00:00', '2023-02-07 16:00:00', 1, 'VISA',       1, '2023-02-06 16:30:00', 800),
        (8, 8,   '2023-02-06 17:00:00', '2023-02-07 17:00:00', 1, 'Mastercard', 1, '2023-02-06 17:30:00', 900),
        (9, 9,   '2023-02-06 18:00:00', '2023-02-07 18:00:00', 1, 'Paypal',     1, '2023-02-06 18:30:00', 1000),
        (10, 10, '2023-02-06 19:00:00', '2023-02-07 19:00:00', 1, 'VISA',       1, '2023-02-06 19:30:00', 1100),
        (11, 11, '2023-02-06 10:00:00', '2023-02-07 10:00:00', 1, 'Mastercard', 1, '2023-02-06 10:30:00', 1200),
        (12, 12, '2023-02-06 11:00:00', '2023-02-07 11:00:00', 1, 'Paypal',     1, '2023-02-06 11:30:00', 1300),
        (13, 13, '2023-02-06 12:00:00', '2023-02-07 12:00:00', 1, 'VISA',       1, '2023-02-06 12:30:00', 1400),
        (14, 14, '2023-02-06 13:00:00', '2023-02-07 13:00:00', 1, 'Mastercard', 1, '2023-02-06 13:30:00', 1500),
        (15, 15, '2023-02-06 14:00:00', '2023-02-07 14:00:00', 1, 'Paypal',     1, '2023-02-06 14:30:00', 1600),
        (16, 1,  '2023-02-06 15:00:00', '2023-02-07 15:00:00', 1, 'VISA',       1, '2023-02-06 15:30:00', 1700),
        (17, 2,  '2023-02-06 16:00:00', '2023-02-07 16:00:00', 1, 'Mastercard', 1, '2023-02-06 16:30:00', 1800),
        (18, 3,  '2023-02-06 17:00:00', '2023-02-07 17:00:00', 1, 'Paypal',     1, '2023-02-06 17:30:00', 1900),
        (19, 4,  '2023-02-06 18:00:00', '2023-02-07 18:00:00', 1, 'VISA',       1, '2023-02-06 18:30:00', 2000),
        (20, 5,  '2023-02-06 19:00:00', '2023-02-07 19:00:00', 1, 'Mastercard', 1, '2023-02-06 19:30:00', 2100),
        (21, 6,  '2023-02-06 10:00:00', '2023-02-07 10:00:00', 1, 'Paypal',     1, '2023-02-06 10:30:00', 2200),
        (22, 7,  '2023-02-06 11:00:00', '2023-02-07 11:00:00', 1, 'VISA',       1, '2023-02-06 11:30:00', 2300),
        (23, 8,  '2023-02-06 12:00:00', '2023-02-07 12:00:00', 1, 'Mastercard', 1, '2023-02-06 12:30:00', 2400),
        (24, 9,  '2023-02-06 13:00:00', '2023-02-07 13:00:00', 1, 'Paypal',     1, '2023-02-06 13:30:00', 2500),
        (25, 10, '2023-02-06 14:00:00', '2023-02-07 14:00:00', 1, 'VISA',       1, '2023-02-06 14:30:00', 2600),
        (26, 11, '2023-02-06 15:00:00', '2023-02-07 15:00:00', 1, 'Mastercard', 1, '2023-02-06 15:30:00', 2700),
        (27, 12, '2023-02-06 16:00:00', '2023-02-07 16:00:00', 1, 'Paypal',     1, '2023-02-06 16:30:00', 2800),
        (28, 13, '2023-02-06 17:00:00', '2023-02-07 17:00:00', 1, 'VISA',       1, '2023-02-06 17:30:00', 2900),
        (29, 14, '2023-02-06 18:00:00', '2023-02-07 18:00:00', 1, 'Mastercard', 1, '2023-02-06 18:30:00', 3000),
        (30, 15, '2023-02-06 19:00:00', '2023-02-07 19:00:00', 1, 'Paypal',     1, '2023-02-06 19:30:00', 3100),
        (31, 1,  '2023-02-06 10:00:00', '2023-02-07 10:00:00', 1, 'VISA',       1, '2023-02-06 10:30:00', 200),
        (32, 2,  '2023-02-06 11:00:00', '2023-02-07 11:00:00', 1, 'Mastercard', 1, '2023-02-06 11:30:00', 300),
        (33, 3,  '2023-02-06 12:00:00', '2023-02-07 12:00:00', 1, 'Paypal',     1, '2023-02-06 12:30:00', 400),
        (34, 4,  '2023-02-06 13:00:00', '2023-02-07 13:00:00', 1, 'VISA',       1, '2023-02-06 13:30:00', 500),
        (35, 5,  '2023-02-06 14:00:00', '2023-02-07 14:00:00', 1, 'Mastercard', 1, '2023-02-06 14:30:00', 600),
        (36, 6,  '2023-02-06 15:00:00', '2023-02-07 15:00:00', 1, 'Paypal',     1, '2023-02-06 15:30:00', 700),
        (37, 7,  '2023-02-06 16:00:00', '2023-02-07 16:00:00', 1, 'VISA',       1, '2023-02-06 16:30:00', 800),
        (38, 8,  '2023-02-06 17:00:00', '2023-02-07 17:00:00', 1, 'Mastercard', 1, '2023-02-06 17:30:00', 900),
        (39, 9,  '2023-02-06 18:00:00', '2023-02-07 18:00:00', 1, 'Paypal',     1, '2023-02-06 18:30:00', 1000),
        (40, 10, '2023-02-06 19:00:00', '2023-02-07 19:00:00', 1, 'VISA',       1, '2023-02-06 19:30:00', 1100),
        (41, 11, '2023-02-06 10:00:00', '2023-02-07 10:00:00', 1, 'Mastercard', 1, '2023-02-06 10:30:00', 1200),
        (42, 12, '2023-02-06 11:00:00', '2023-02-07 11:00:00', 1, 'Paypal',     1, '2023-02-06 11:30:00', 1300),
        (43, 13, '2023-02-06 12:00:00', '2023-02-07 12:00:00', 1, 'VISA',       1, '2023-02-06 12:30:00', 1400),
        (44, 14, '2023-02-06 13:00:00', '2023-02-07 13:00:00', 1, 'Mastercard', 1, '2023-02-06 13:30:00', 1500),
        (45, 15, '2023-02-06 14:00:00', '2023-02-07 14:00:00', 1, 'Paypal',     1, '2023-02-06 14:30:00', 1600),
        (46, 1,  '2023-02-06 15:00:00', '2023-02-07 15:00:00', 1, 'VISA',       1, '2023-02-06 15:30:00', 1700),
        (47, 2,  '2023-02-06 16:00:00', '2023-02-07 16:00:00', 1, 'Mastercard', 1, '2023-02-06 16:30:00', 1800),
        (48, 3,  '2023-02-06 17:00:00', '2023-02-07 17:00:00', 1, 'Paypal',     1, '2023-02-06 17:30:00', 1900),
        (49, 4,  '2023-02-06 18:00:00', '2023-02-07 18:00:00', 1, 'VISA',       1, '2023-02-06 18:30:00', 2000),
        (50, 5,  '2023-02-06 19:00:00', '2023-02-07 19:00:00', 1, 'Mastercard', 1, '2023-02-06 19:30:00', 2100),
        (51, 6,  '2023-02-06 10:00:00', '2023-02-07 10:00:00', 1, 'Paypal',     1, '2023-02-06 10:30:00', 2200),
        (52, 7,  '2023-02-06 11:00:00', '2023-02-07 11:00:00', 1, 'VISA',       1, '2023-02-06 11:30:00', 2300),
        (53, 8,  '2023-02-06 12:00:00', '2023-02-07 12:00:00', 1, 'Mastercard', 1, '2023-02-06 12:30:00', 2400),
        (54, 9,  '2023-02-06 13:00:00', '2023-02-07 13:00:00', 1, 'Paypal',     1, '2023-02-06 13:30:00', 2500),
        (55, 10, '2023-02-06 14:00:00', '2023-02-07 14:00:00', 1, 'VISA',       1, '2023-02-06 14:30:00', 2600),
        (56, 11, '2023-02-06 15:00:00', '2023-02-07 15:00:00', 1, 'Mastercard', 1, '2023-02-06 15:30:00', 2700),
        (57, 12, '2023-02-06 16:00:00', '2023-02-07 16:00:00', 1, 'Paypal',     1, '2023-02-06 16:30:00', 2800),
        (58, 13, '2023-02-06 17:00:00', '2023-02-07 17:00:00', 1, 'VISA',       1, '2023-02-06 17:30:00', 2900),
        (59, 14, '2023-02-06 18:00:00', '2023-02-07 18:00:00', 1, 'Mastercard', 1, '2023-02-06 18:30:00', 3000),
        (60, 15, '2023-02-06 19:00:00', '2023-02-07 19:00:00', 1, 'Paypal',     1, '2023-02-06 19:30:00', 3100);