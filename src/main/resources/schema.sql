
CREATE TABLE IF NOT EXISTS filmorate_user (
                                              id int AUTO_INCREMENT PRIMARY KEY,
                                              email varchar(100) UNIQUE,
    login varchar(100) UNIQUE,
    name varchar(100),
    birthday date
    );

CREATE TABLE IF NOT EXISTS friendship (
                                          id int AUTO_INCREMENT PRIMARY KEY,
                                          active_user_id int,
                                          passive_user_id int,
                                          is_accepted boolean DEFAULT FALSE,
                                          FOREIGN KEY (active_user_id) REFERENCES filmorate_user(id),
    FOREIGN KEY (passive_user_id) REFERENCES filmorate_user(id)
    );

CREATE TABLE IF NOT EXISTS genre (
                                     id int AUTO_INCREMENT PRIMARY KEY,
                                     name varchar(50)
    );

CREATE TABLE IF NOT EXISTS mpa (
                                   id int AUTO_INCREMENT PRIMARY KEY,
                                   name varchar(50)
    );

CREATE TABLE IF NOT EXISTS film (
                                    id int AUTO_INCREMENT PRIMARY KEY,
                                    name varchar(100),
    description text,
    release_date date,
    duration int,
    mpa_id int,
    rate int,
    FOREIGN KEY (mpa_id) REFERENCES mpa(id)
    );

CREATE TABLE IF NOT EXISTS user_like (
                                         user_id int,
                                         film_id int,
                                         PRIMARY KEY (user_id, film_id),
    FOREIGN KEY (user_id) REFERENCES filmorate_user(id),
    FOREIGN KEY (film_id) REFERENCES film(id)
    );

CREATE TABLE IF NOT EXISTS film_genres (
                                           film_id int,
                                           genre_id int,
                                           PRIMARY KEY (film_id, genre_id),
    FOREIGN KEY (film_id) REFERENCES film(id),
    FOREIGN KEY (genre_id) REFERENCES genre(id)
    )