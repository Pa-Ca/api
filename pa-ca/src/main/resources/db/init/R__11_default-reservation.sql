INSERT INTO reservation (id, branch_id, request_date, reservation_date_in, reservation_date_out, client_number, table_number, payment, status, pay_date, price, occasion, by_client)
    VALUES
        (1, 1,   '2023-02-06 10:00:00', '2023-02-07 10:00:00','2023-02-07 11:00:00', 1, 1, 'VISA',       1, '2023-02-06 10:30:00', 200, 'Anniversary', TRUE),
        (2, 2,   '2023-02-06 11:00:00', '2023-02-07 11:00:00','2023-02-07 12:00:00', 1, 1, 'Mastercard', 1, '2023-02-06 11:30:00', 300, 'Anniversary', TRUE),
        (3, 3,   '2023-02-06 12:00:00', '2023-02-07 12:00:00','2023-02-07 13:00:00', 1, 1, 'Paypal',     1, '2023-02-06 12:30:00', 400, 'Anniversary', TRUE),
        (4, 4,   '2023-02-06 13:00:00', '2023-02-07 13:00:00','2023-02-07 14:00:00', 1, 1, 'VISA',       1, '2023-02-06 13:30:00', 500, 'Anniversary', TRUE),
        (5, 5,   '2023-02-06 14:00:00', '2023-02-07 14:00:00','2023-02-07 15:00:00', 1, 1, 'Mastercard', 1, '2023-02-06 14:30:00', 600, 'Anniversary', TRUE),
        (6, 6,   '2023-02-06 15:00:00', '2023-02-07 15:00:00','2023-02-07 16:00:00', 1, 1, 'Paypal',     1, '2023-02-06 15:30:00', 700, 'Anniversary', TRUE),
        (7, 7,   '2023-02-06 16:00:00', '2023-02-07 16:00:00','2023-02-07 17:00:00', 1, 1, 'VISA',       1, '2023-02-06 16:30:00', 800, 'Anniversary', TRUE),
        (8, 8,   '2023-02-06 17:00:00', '2023-02-07 17:00:00','2023-02-07 18:00:00', 1, 1, 'Mastercard', 1, '2023-02-06 17:30:00', 900, 'Anniversary', TRUE),
        (9, 9,   '2023-02-06 18:00:00', '2023-02-07 18:00:00','2023-02-07 19:00:00', 1, 1, 'Paypal',     1, '2023-02-06 18:30:00', 1000, 'Anniversary', TRUE),
        (10, 10, '2023-02-06 19:00:00', '2023-02-07 19:00:00','2023-02-07 10:00:00', 1, 1, 'VISA',       1, '2023-02-06 19:30:00', 1100, 'Anniversary', TRUE),
        (11, 11, '2023-02-06 10:00:00', '2023-02-07 10:00:00','2023-02-07 11:00:00', 1, 1, 'Mastercard', 1, '2023-02-06 10:30:00', 1200, 'Anniversary', TRUE),
        (12, 12, '2023-02-06 11:00:00', '2023-02-07 11:00:00','2023-02-07 12:00:00', 1, 1, 'Paypal',     1, '2023-02-06 11:30:00', 1300, 'Anniversary', TRUE),
        (13, 13, '2023-02-06 12:00:00', '2023-02-07 12:00:00','2023-02-07 13:00:00', 1, 1, 'VISA',       1, '2023-02-06 12:30:00', 1400, 'Anniversary', TRUE),
        (14, 14, '2023-02-06 13:00:00', '2023-02-07 13:00:00','2023-02-07 14:00:00', 1, 1, 'Mastercard', 1, '2023-02-06 13:30:00', 1500, 'Anniversary', TRUE),
        (15, 15, '2023-02-06 14:00:00', '2023-02-07 14:00:00','2023-02-07 15:00:00', 1, 1, 'Paypal',     1, '2023-02-06 14:30:00', 1600, 'Anniversary', TRUE),
        (16, 1,  '2023-02-06 15:00:00', '2023-02-07 15:00:00','2023-02-07 16:00:00', 1, 1, 'VISA',       1, '2023-02-06 15:30:00', 1700, 'Anniversary', TRUE),
        (17, 2,  '2023-02-06 16:00:00', '2023-02-07 16:00:00','2023-02-07 17:00:00', 1, 1, 'Mastercard', 1, '2023-02-06 16:30:00', 1800, 'Anniversary', TRUE),
        (18, 3,  '2023-02-06 17:00:00', '2023-02-07 17:00:00','2023-02-07 18:00:00', 1, 1, 'Paypal',     1, '2023-02-06 17:30:00', 1900, 'Anniversary', TRUE),
        (19, 4,  '2023-02-06 18:00:00', '2023-02-07 18:00:00','2023-02-07 19:00:00', 1, 1, 'VISA',       1, '2023-02-06 18:30:00', 2000, 'Anniversary', TRUE),
        (20, 5,  '2023-02-06 19:00:00', '2023-02-07 19:00:00','2023-02-07 10:00:00', 1, 1, 'Mastercard', 1, '2023-02-06 19:30:00', 2100, 'Anniversary', TRUE),
        (21, 6,  '2023-02-06 10:00:00', '2023-02-07 10:00:00','2023-02-07 11:00:00', 1, 1, 'Paypal',     1, '2023-02-06 10:30:00', 2200, 'Anniversary', TRUE),
        (22, 7,  '2023-02-06 11:00:00', '2023-02-07 11:00:00','2023-02-07 12:00:00', 1, 1, 'VISA',       1, '2023-02-06 11:30:00', 2300, 'Anniversary', TRUE),
        (23, 8,  '2023-02-06 12:00:00', '2023-02-07 12:00:00','2023-02-07 13:00:00', 1, 1, 'Mastercard', 1, '2023-02-06 12:30:00', 2400, 'Anniversary', TRUE),
        (24, 9,  '2023-02-06 13:00:00', '2023-02-07 13:00:00','2023-02-07 14:00:00', 1, 1, 'Paypal',     1, '2023-02-06 13:30:00', 2500, 'Anniversary', TRUE),
        (25, 10, '2023-02-06 14:00:00', '2023-02-07 14:00:00','2023-02-07 15:00:00', 1, 1, 'VISA',       1, '2023-02-06 14:30:00', 2600, 'Anniversary', TRUE),
        (26, 11, '2023-02-06 15:00:00', '2023-02-07 15:00:00','2023-02-07 16:00:00', 1, 1, 'Mastercard', 1, '2023-02-06 15:30:00', 2700, 'Anniversary', TRUE),
        (27, 12, '2023-02-06 16:00:00', '2023-02-07 16:00:00','2023-02-07 17:00:00', 1, 1, 'Paypal',     1, '2023-02-06 16:30:00', 2800, 'Anniversary', TRUE),
        (28, 13, '2023-02-06 17:00:00', '2023-02-07 17:00:00','2023-02-07 18:00:00', 1, 1, 'VISA',       1, '2023-02-06 17:30:00', 2900, 'Anniversary', TRUE),
        (29, 14, '2023-02-06 18:00:00', '2023-02-07 18:00:00','2023-02-07 19:00:00', 1, 1, 'Mastercard', 1, '2023-02-06 18:30:00', 3000, 'Anniversary', TRUE),
        (30, 15, '2023-02-06 19:00:00', '2023-02-07 19:00:00','2023-02-07 10:00:00', 1, 1, 'Paypal',     1, '2023-02-06 19:30:00', 3100, 'Anniversary', TRUE),
        (31, 1,  '2023-02-06 10:00:00', '2023-02-07 10:00:00','2023-02-07 11:00:00', 1, 1, 'VISA',       1, '2023-02-06 10:30:00', 200, 'Anniversary', TRUE),
        (32, 2,  '2023-02-06 11:00:00', '2023-02-07 11:00:00','2023-02-07 12:00:00', 1, 1, 'Mastercard', 1, '2023-02-06 11:30:00', 300, 'Anniversary', TRUE),
        (33, 3,  '2023-02-06 12:00:00', '2023-02-07 12:00:00','2023-02-07 13:00:00', 1, 1, 'Paypal',     1, '2023-02-06 12:30:00', 400, 'Anniversary', TRUE),
        (34, 4,  '2023-02-06 13:00:00', '2023-02-07 13:00:00','2023-02-07 14:00:00', 1, 1, 'VISA',       1, '2023-02-06 13:30:00', 500, 'Anniversary', TRUE),
        (35, 5,  '2023-02-06 14:00:00', '2023-02-07 14:00:00','2023-02-07 15:00:00', 1, 1, 'Mastercard', 1, '2023-02-06 14:30:00', 600, 'Anniversary', TRUE),
        (36, 6,  '2023-02-06 15:00:00', '2023-02-07 15:00:00','2023-02-07 16:00:00', 1, 1, 'Paypal',     1, '2023-02-06 15:30:00', 700, 'Anniversary', TRUE),
        (37, 7,  '2023-02-06 16:00:00', '2023-02-07 16:00:00','2023-02-07 17:00:00', 1, 1, 'VISA',       1, '2023-02-06 16:30:00', 800, 'Anniversary', TRUE),
        (38, 8,  '2023-02-06 17:00:00', '2023-02-07 17:00:00','2023-02-07 18:00:00', 1, 1, 'Mastercard', 1, '2023-02-06 17:30:00', 900, 'Anniversary', TRUE),
        (39, 9,  '2023-02-06 18:00:00', '2023-02-07 18:00:00','2023-02-07 19:00:00', 1, 1, 'Paypal',     1, '2023-02-06 18:30:00', 1000, 'Anniversary', TRUE),
        (40, 10, '2023-02-06 19:00:00', '2023-02-07 19:00:00','2023-02-07 10:00:00', 1, 1, 'VISA',       1, '2023-02-06 19:30:00', 1100, 'Anniversary', TRUE),
        (41, 11, '2023-02-06 10:00:00', '2023-02-07 10:00:00','2023-02-07 11:00:00', 1, 1, 'Mastercard', 1, '2023-02-06 10:30:00', 1200, 'Anniversary', TRUE),
        (42, 12, '2023-02-06 11:00:00', '2023-02-07 11:00:00','2023-02-07 12:00:00', 1, 1, 'Paypal',     1, '2023-02-06 11:30:00', 1300, 'Anniversary', TRUE),
        (43, 13, '2023-02-06 12:00:00', '2023-02-07 12:00:00','2023-02-07 13:00:00', 1, 1, 'VISA',       1, '2023-02-06 12:30:00', 1400, 'Anniversary', TRUE),
        (44, 14, '2023-02-06 13:00:00', '2023-02-07 13:00:00','2023-02-07 14:00:00', 1, 1, 'Mastercard', 1, '2023-02-06 13:30:00', 1500, 'Anniversary', TRUE),
        (45, 15, '2023-02-06 14:00:00', '2023-02-07 14:00:00','2023-02-07 15:00:00', 1, 1, 'Paypal',     1, '2023-02-06 14:30:00', 1600, 'Anniversary', TRUE),
        (46, 1,  '2023-02-06 15:00:00', '2023-02-07 15:00:00','2023-02-07 16:00:00', 1, 1, 'VISA',       1, '2023-02-06 15:30:00', 1700, 'Anniversary', TRUE),
        (47, 2,  '2023-02-06 16:00:00', '2023-02-07 16:00:00','2023-02-07 17:00:00', 1, 1, 'Mastercard', 1, '2023-02-06 16:30:00', 1800, 'Anniversary', TRUE),
        (48, 3,  '2023-02-06 17:00:00', '2023-02-07 17:00:00','2023-02-07 18:00:00', 1, 1, 'Paypal',     1, '2023-02-06 17:30:00', 1900, 'Anniversary', TRUE),
        (49, 4,  '2023-02-06 18:00:00', '2023-02-07 18:00:00','2023-02-07 19:00:00', 1, 1, 'VISA',       1, '2023-02-06 18:30:00', 2000, 'Anniversary', TRUE),
        (50, 5,  '2023-02-06 19:00:00', '2023-02-07 19:00:00','2023-02-07 10:00:00', 1, 1, 'Mastercard', 1, '2023-02-06 19:30:00', 2100, 'Anniversary', TRUE),
        (51, 6,  '2023-02-06 10:00:00', '2023-02-07 10:00:00','2023-02-07 11:00:00', 1, 1, 'Paypal',     1, '2023-02-06 10:30:00', 2200, 'Anniversary', TRUE),
        (52, 7,  '2023-02-06 11:00:00', '2023-02-07 11:00:00','2023-02-07 12:00:00', 1, 1, 'VISA',       1, '2023-02-06 11:30:00', 2300, 'Anniversary', TRUE),
        (53, 8,  '2023-02-06 12:00:00', '2023-02-07 12:00:00','2023-02-07 13:00:00', 1, 1, 'Mastercard', 1, '2023-02-06 12:30:00', 2400, 'Anniversary', TRUE),
        (54, 9,  '2023-02-06 13:00:00', '2023-02-07 13:00:00','2023-02-07 14:00:00', 1, 1, 'Paypal',     1, '2023-02-06 13:30:00', 2500, 'Anniversary', TRUE),
        (55, 10, '2023-02-06 14:00:00', '2023-02-07 14:00:00','2023-02-07 15:00:00', 1, 1, 'VISA',       1, '2023-02-06 14:30:00', 2600, 'Anniversary', TRUE),
        (56, 11, '2023-02-06 15:00:00', '2023-02-07 15:00:00','2023-02-07 16:00:00', 1, 1, 'Mastercard', 1, '2023-02-06 15:30:00', 2700, 'Anniversary', TRUE),
        (57, 12, '2023-02-06 16:00:00', '2023-02-07 16:00:00','2023-02-07 17:00:00', 1, 1, 'Paypal',     1, '2023-02-06 16:30:00', 2800, 'Anniversary', TRUE),
        (58, 13, '2023-02-06 17:00:00', '2023-02-07 17:00:00','2023-02-07 18:00:00', 1, 1, 'VISA',       1, '2023-02-06 17:30:00', 2900, 'Anniversary', TRUE),
        (59, 14, '2023-02-06 18:00:00', '2023-02-07 18:00:00','2023-02-07 19:00:00', 1, 1, 'Mastercard', 1, '2023-02-06 18:30:00', 3000, 'Anniversary', TRUE),
        (60, 15, '2023-02-06 19:00:00', '2023-02-07 19:00:00','2023-02-07 19:00:00', 1, 1, 'Paypal',     1, '2023-02-06 19:30:00', 3100, 'Anniversary', TRUE);