INSERT INTO public.role (id, name) VALUES 
    (0, 'admin'),
    (1, 'client'),
    (2, 'business');

INSERT INTO public.tier (id, name, reservation_limit, tier_cost) VALUES 
    (0, 'basic', 100, 10),
    (1, 'premium', 500, 50),
    (2, 'unlimited', -1, 100);

INSERT INTO product_category (id, name) VALUES
    (0, 'Bebidas'),
    (1, 'Postres'),
    (2, 'Desayuno'),
    (3, 'Aperitivos'),
    (4, 'Comida rápida'),
    (5, 'Comida vegetariana'),
    (6, 'Platos principales');