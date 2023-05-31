DROP TABLE IF EXISTS people;
CREATE TABLE people(
id integer NOT NULL GENERATED ALWAYS AS IDENTITY,
date_of_birth date,
email varchar NOT NULL UNIQUE,
name varchar NOT NULL,
surname varchar,
patronymic varchar,
phone_number varchar NOT NULL UNIQUE,
photo bytea,
photo_name varchar,
CONSTRAINT people_pkey PRIMARY KEY (id)
);

DROP TABLE IF EXISTS users;
CREATE TABLE users(
id integer NOT NULL GENERATED ALWAYS AS IDENTITY,
username varchar NOT NULL UNIQUE,
password varchar NOT NULL,
role varchar,
CONSTRAINT users_pkey PRIMARY KEY (id)
);