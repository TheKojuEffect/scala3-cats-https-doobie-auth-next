CREATE TABLE user_profile (
    user_id        uuid PRIMARY KEY references users(id),
    first_name     VARCHAR NOT NULL,
    last_name      VARCHAR NOT NULL,
    city           VARCHAR NOT NULL,
    state          VARCHAR NOT NULL,
    ethnic_country VARCHAR NOT NULL
);