-- Amenities

INSERT INTO amenity (id, name) 
    VALUES 
        (0, 'pet friendly'),
        (1, 'smoking'),
        (2, '24 hours'),
        (3, 'music'),
        (4, 'music live'),
        (5, 'wifi'),
        (6, 'parking'),
        (7, 'outdoor'),
        (8, 'hotel'),
        (9, 'internet'),
        (10, 'delivery');

INSERT INTO branch_amenity (id, branch_id, amenity_id) 
    VALUES
        (1, 1, 1),
        (2, 2, 2),
        (3, 3, 3),
        (4, 4, 4),
        (5, 5, 5),
        (6, 6, 6),
        (7, 7, 7),
        (8, 8, 8),
        (9, 9, 9),
        (10, 10, 10),
        (11, 11, 1),
        (12, 12, 2),
        (13, 13, 3),
        (14, 14, 4),
        (15, 15, 5),
        (16, 1, 6),
        (17, 2, 7),
        (18, 3, 8),
        (19, 4, 9),
        (20, 5, 10);