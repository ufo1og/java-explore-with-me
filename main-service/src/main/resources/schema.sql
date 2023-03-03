CREATE TABLE IF NOT EXISTS USERS (
    id BIGINT GENERATED ALWAYS AS IDENTITY NOT NULL,
    email VARCHAR(64) NOT NULL,
    name VARCHAR(64) NOT NULL,
    CONSTRAINT user_id_pk PRIMARY KEY (id),
    CONSTRAINT user_email_uq UNIQUE (email)
);