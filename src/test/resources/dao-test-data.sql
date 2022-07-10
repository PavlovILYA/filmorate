DELETE FROM friendship;
DELETE FROM user_like;
DELETE FROM filmorate_user;
DELETE FROM film_genres;
DELETE FROM film;

ALTER TABLE friendship ALTER COLUMN id RESTART WITH 1;
ALTER TABLE filmorate_user ALTER COLUMN id RESTART WITH 1;
ALTER TABLE film ALTER COLUMN id RESTART WITH 1;

INSERT INTO filmorate_user (email, login, name, birthday)
    VALUES ('superduper@gmail.com', 'superduper', 'Игорь', '1998-12-20'),
           ('cheapailna@gmail.com', 'cheapalina', 'Алина', '1997-04-23'),
           ('lol.ololoev@yahoo.com', 'lol!', 'Илья', '1999-06-17'),
           ('ohmygadr@ya.ru', 'ohmy', 'Федор', '1996-10-24'),
           ('prettygood@ya.ru', 'good-good', 'Даша', '2000-10-14');

INSERT INTO friendship (active_user_id, passive_user_id, is_accepted)
    VALUES (1, 2, TRUE),
           (1, 3, TRUE),
           (4, 5, TRUE),
           (5, 3, TRUE),
           (2, 1, FALSE);

INSERT INTO film (name, description, release_date, duration, mpa_id)
    VALUES ('Титаник', 'Корабль тонет', '1999-09-12', 180, 1),
           ('Бойцовский клуб', 'У парня шиза', '1999-07-08', 120, 2),
           ('Нет', 'Мирная ревалюция в Чили. Как ушел Пиночет.', '2012-06-10', 110, 3),
           ('Шрек', 'В двух словах и не опишешь', '2001-09-12', 120, 4),
           ('Евротур', 'Легендарная американская комедия про европейцев', '2003-03-09', 114, 5);

INSERT INTO film_genres (film_id, genre_id)
    VALUES (1, 2), (1, 5),
           (2, 4), (2, 6),
           (3, 2), (3, 5),
           (4, 3),
           (5, 1);

INSERT INTO user_like (user_id, film_id)
    values (1, 1), (1, 3), (1, 4),
           (2, 1), (2, 2),
           (3, 4), (3, 2),
           (4, 4), (4, 1);