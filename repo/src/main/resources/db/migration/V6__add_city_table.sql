CREATE TABLE city (
    id    UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name  VARCHAR(50) NOT NULL,
    state CHAR(2) REFERENCES state(abbreviation)
);