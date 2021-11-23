CREATE TABLE post (
    id              UUID PRIMARY KEY,
    message         VARCHAR   NOT NULL,
    target_state    VARCHAR   NOT NULL,
    target_zip_code VARCHAR   NOT NULL,
    created_by      UUID      NOT NULL REFERENCES users(ID),
    created_at      TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);