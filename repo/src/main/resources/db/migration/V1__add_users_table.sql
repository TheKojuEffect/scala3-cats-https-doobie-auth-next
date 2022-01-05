CREATE TABLE users (
    id    UUID PRIMARY KEY,
    email VARCHAR NOT NULL UNIQUE,
    password  VARCHAR NOT NULL,
    role  VARCHAR NOT NULL DEFAULT 'Normal'
);