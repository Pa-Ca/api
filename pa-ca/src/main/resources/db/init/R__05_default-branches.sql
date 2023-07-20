-- Branches

INSERT INTO branch (id, business_id, location, maps_link, name, overview, score, capacity, reservation_price, reserve_off, average_reserve_time, hour_in, hour_out,dollar_to_local_currency_exchange)
    VALUES
    
        (1, 1, 'Location 1-1', 'Maps link 1-1', 'Name 1-1', 'Overview 1-1', 4.5, 20, 5, FALSE, '05:30:25', '09:00:00', '21:00:00', 1.0),
        (2, 1, 'Location 1-2', 'Maps link 1-2', 'Name 1-2', 'Overview 1-2', 4.0, 30, 5, FALSE, '05:30:25', '09:00:00', '21:00:00', 1.0),
        (3, 2, 'Location 2-1', 'Maps link 2-1', 'Name 2-1', 'Overview 2-1', 4.7, 25, 5, FALSE, '05:30:25', '09:00:00', '21:00:00', 1.0),
        (4, 3, 'Location 3-1', 'Maps link 3-1', 'Name 3-1', 'Overview 3-1', 4.6, 22, 5, FALSE, '05:30:25', '09:00:00', '21:00:00', 1.0),
        (5, 3, 'Location 3-2', 'Maps link 3-2', 'Name 3-2', 'Overview 3-2', 4.2, 33, 5, FALSE, '05:30:25', '09:00:00', '21:00:00', 1.0),
        (6, 4, 'Location 4-1', 'Maps link 4-1', 'Name 4-1', 'Overview 4-1', 4.8, 15, 5, FALSE, '05:30:25', '09:00:00', '21:00:00', 1.0),
        (7, 5, 'Location 5-1', 'Maps link 5-1', 'Name 5-1', 'Overview 5-1', 4.3, 25, 5, FALSE, '05:30:25', '09:00:00', '21:00:00', 1.0),
        (8, 5, 'Location 5-2', 'Maps link 5-2', 'Name 5-2', 'Overview 5-2', 4.9, 30, 5, FALSE, '05:30:25', '09:00:00', '21:00:00', 1.0),
        (9, 5, 'Location 5-3', 'Maps link 5-3', 'Name 5-3', 'Overview 5-3', 4.0, 20, 5, FALSE, '05:30:25', '09:00:00', '21:00:00', 1.0),
        (10, 6, 'Location 6-1', 'Maps link 6-1', 'Name 6-1', 'Overview 6-1', 4.4, 25, 5, FALSE, '05:30:25', '09:00:00', '21:00:00', 1.0),
        (11, 7, 'Location 7-1', 'Maps link 7-1', 'Name 7-1', 'Overview 7-1', 4.5, 30, 5, FALSE, '05:30:25', '09:00:00', '21:00:00', 1.0),
        (12, 8, 'Location 8-1', 'Maps link 8-1', 'Name 8-1', 'Overview 8-1', 4.0, 20, 5, FALSE, '05:30:25', '09:00:00', '21:00:00', 1.0),
        (13, 9, 'Location 9-1', 'Maps link 9-1', 'Name 9-1', 'Overview 9-1', 4.7, 25, 5, FALSE, '05:30:25', '09:00:00', '21:00:00', 1.0),
        (14, 10, 'Location 10-1', 'Maps link 10-1', 'Name 10-1', 'Overview 10-1', 4.2, 33, 5, FALSE, '05:30:25', '09:00:00', '21:00:00', 1.0),
        (15, 10, 'Location 10-2', 'Maps link 10-2', 'Name 10-2', 'Overview 10-2', 4.9, 30, 5, FALSE, '05:30:25', '09:00:00', '21:00:00', 1.0);
