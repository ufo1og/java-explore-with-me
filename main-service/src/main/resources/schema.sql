CREATE TABLE IF NOT EXISTS USERS (
    id BIGINT GENERATED ALWAYS AS IDENTITY NOT NULL,
    email VARCHAR(64) NOT NULL,
    name VARCHAR(64) NOT NULL,
    CONSTRAINT user_id_pk PRIMARY KEY (id),
    CONSTRAINT user_email_uq UNIQUE (email)
);

CREATE TABLE IF NOT EXISTS CATEGORIES (
    id BIGINT GENERATED ALWAYS AS IDENTITY NOT NULL,
    name VARCHAR(256) NOT NULL,
    CONSTRAINT category_id_pk PRIMARY KEY (id),
    CONSTRAINT category_name_uq UNIQUE (name)
);

CREATE TABLE IF NOT EXISTS EVENTS (
    id BIGINT GENERATED ALWAYS AS IDENTITY NOT NULL,
    title VARCHAR(120) NOT NULL,
    annotation VARCHAR(2000) NOT NULL,
    description VARCHAR(7000) NOT NULL,
    event_date TIMESTAMP NOT NULL,
    latitude DOUBLE PRECISION NOT NULL,
    longitude DOUBLE PRECISION NOT NULL,
    paid BOOLEAN NOT NULL,
    participant_limit INT NOT NULL,
    request_moderation BOOLEAN NOT NULL,
    initiator BIGINT NOT NULL,
    category BIGINT NOT NULL,
    CONSTRAINT event_id_pk PRIMARY KEY (id),
    CONSTRAINT event_initiator_fk FOREIGN KEY (initiator) REFERENCES USERS (id),
    CONSTRAINT event_category_fk FOREIGN KEY (category) REFERENCES CATEGORIES (id),
    CONSTRAINT event_pl_positive CHECK (participant_limit >= 0)
);