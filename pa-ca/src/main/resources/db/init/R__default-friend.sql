-- Friend

INSERT INTO friend (id, client_requester_id, client_addresser_id, accepted, rejected)
    VALUES
        (1, 1, 2, true, false),
        (2, 1, 3, false, true),
        (3, 1, 4, false, false),
        (4, 2, 3, true, false),
        (5, 2, 4, true, false),
        (6, 2, 5, false, false),
        (7, 3, 4, true, false),
        (8, 3, 5, false, true),
        (9, 3, 6, false, false),
        (10, 4, 5, false, false),
        (11, 4, 6, true, false),
        (12, 4, 7, false, false),
        (13, 5, 6, false, true),
        (14, 5, 7, false, false),
        (15, 5, 8, true, false),
        (16, 6, 7, false, false),
        (17, 6, 8, false, true),
        (18, 6, 9, false, false),
        (19, 7, 8, false, false),
        (20, 7, 9, true, false),
        (21, 7, 10, false, false),
        (22, 8, 9, false, false),
        (24, 8, 10, true, false),
        (25, 9, 10, false, true),
        (26, 10, 11, false, false),
        (27, 11, 12, true, false),
        (28, 11, 13, false, false),
        (29, 11, 14, false, true),
        (30, 12, 13, false, false),
        (31, 12, 14, true, false),
        (32, 12, 15, false, false),
        (33, 13, 14, false, false),
        (34, 13, 15, false, true),
        (35, 13, 16, true, false),
        (36, 14, 15, false, false),
        (37, 14, 16, false, true),
        (38, 14, 17, false, false),
        (39, 15, 16, true, false),
        (40, 15, 17, false, false),
        (41, 15, 18, false, true),
        (42, 16, 17, false, false),
        (43, 16, 18, false, false),
        (44, 16, 19, true, false),
        (45, 17, 18, false, false),
        (46, 17, 19, false, false),
        (47, 17, 20, false, true),
        (48, 18, 19, false, false),
        (49, 18, 20, true, false),
        (50, 19, 20, false, false);

