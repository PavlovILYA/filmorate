DELETE FROM friendship;
DELETE FROM likes;
DELETE FROM users;
DELETE FROM film_genres;
DELETE FROM films;

ALTER TABLE users ALTER COLUMN id RESTART WITH 1;
ALTER TABLE films ALTER COLUMN id RESTART WITH 1;

INSERT INTO users (email, login, name, birthday)
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
           (4, 1, FALSE);

INSERT INTO films (name, description, release_date, duration, mpa_id, rate)
    VALUES ('Титаник', 'Корабль тонет', '1999-09-12', 180, 1, 8),
           ('Бойцовский клуб', 'У парня шиза', '2000-01-13', 139, 5, 8),
           ('Нет', 'Мирная революция в Чили. Как ушел Пиночет.', '2012-06-10', 110, 3, 9),
           ('Шрек', 'В двух словах и не опишешь', '2001-09-12', 120, 4, 10),
           ('Евротур', 'Легендарная американская комедия про европейцев', '2003-03-09', 114, 5, 6);

INSERT INTO film_genres (film_id, genre_id)
    VALUES (1, 2), (1, 5),
           (2, 4), (2, 6),
           (3, 2), (3, 5),
           (4, 3),
           (5, 1);

INSERT INTO likes (user_id, film_id)
    values (1, 1), (1, 3), (1, 4),
           (2, 1), (2, 2),
           (3, 4), (3, 2), (3, 1),
           (4, 4), (4, 1);