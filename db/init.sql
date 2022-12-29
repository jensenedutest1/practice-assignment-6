DROP TABLE IF EXISTS artists CASCADE;
DROP TABLE IF EXISTS tracks;

CREATE TABLE artists (
	id SERIAL PRIMARY KEY,
	name TEXT NOT NULL UNIQUE,
    CHECK (name <> '')
);

CREATE TABLE tracks (
	id SERIAL PRIMARY KEY,
	name TEXT NOT NULL,
    CHECK (name <> ''),
	year INT,
	artist_id INT,
	UNIQUE (name, artist_id),
	FOREIGN KEY (artist_id)
	REFERENCES artists (id)
	ON DELETE CASCADE
);


INSERT INTO artists (name) VALUES ('Led Zeppelin');
INSERT INTO tracks (name, year, artist_id) VALUES ('Whole Lotta Love', 1969, 1);
INSERT INTO tracks (name, year, artist_id) VALUES ('Immigrant Song', 1970, 1);
INSERT INTO tracks (name, year, artist_id) VALUES ('Stairway to Heaven', 1971, 1);

INSERT INTO artists (name) VALUES ('Pink Floyd');
INSERT INTO tracks (name, year, artist_id) VALUES ('Wish You Were Here', 1975, 2);
INSERT INTO tracks (name, year, artist_id) VALUES ('The Wall', 1979, 2);

INSERT INTO artists (name) VALUES ('The Black Keys');
INSERT INTO tracks (name, year, artist_id) VALUES ('I Got Mine', 2008, 3);
INSERT INTO tracks (name, year, artist_id) VALUES ('Howlin'' for You', 2011, 3);