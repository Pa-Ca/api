-- Business

INSERT INTO "user" (id, email, password, verified, role_id, logged_in, registration_status) 
    VALUES
        (21, 'user21@example.com', 'password21', false, 2, false, 0),
        (22, 'user22@example.com', 'password22', false, 2, false, 0),
        (23, 'user23@example.com', 'password23', false, 2, false, 0),
        (24, 'user24@example.com', 'password24', false, 2, false, 0),
        (25, 'user25@example.com', 'password25', false, 2, false, 0),
        (26, 'user26@example.com', 'password26', false, 2, false, 0),
        (27, 'user27@example.com', 'password27', false, 2, false, 0),
        (28, 'user28@example.com', 'password28', false, 2, false, 0),
        (29, 'user29@example.com', 'password29', false, 2, false, 0),
        (30, 'user30@example.com', 'password30', false, 2, false, 0);

INSERT INTO tier VALUES (0, 'basic', 100, 10);
INSERT INTO tier VALUES (1, 'premium', 500, 50);
INSERT INTO tier VALUES (2, 'unlimited', -1, 100);

INSERT INTO business (id, user_id, tier_id, name, verified) 
    VALUES
        (1, 21, 1, 'business1', false),
        (2, 22, 1, 'business2', false),
        (3, 23, 1, 'business3', false),
        (4, 24, 1, 'business4', false),
        (5, 25, 1, 'business5', false),
        (6, 26, 1, 'business6', false),
        (7, 27, 1, 'business7', false),
        (8, 28, 1, 'business8', false),
        (9, 29, 1, 'business9', false),
        (10, 30, 1, 'business10', false);