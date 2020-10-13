CREATE TABLE users (
    id    uuid PRIMARY KEY,
    email VARCHAR NOT NULL UNIQUE,
    password  VARCHAR NOT NULL,
    role  VARCHAR NOT NULL DEFAULT 'Normal'
);