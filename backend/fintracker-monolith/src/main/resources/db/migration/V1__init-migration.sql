CREATE TABLE support_request_entity
(
    id          INTEGER GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    description VARCHAR(255),
    email       VARCHAR(255),
    name        VARCHAR(255),
    created_at  TIMESTAMP WITHOUT TIME ZONE,
    status      SMALLINT,
    CONSTRAINT pk_supportrequestentity PRIMARY KEY (id)
);

CREATE TABLE users
(
    id         BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    username   VARCHAR(255)                            NOT NULL,
    email      VARCHAR(255)                            NOT NULL,
    password   VARCHAR(255)                            NOT NULL,
    created_at TIMESTAMP WITHOUT TIME ZONE,
    updated_at TIMESTAMP WITHOUT TIME ZONE,
    CONSTRAINT pk_users PRIMARY KEY (id)
);

ALTER TABLE users
    ADD CONSTRAINT uc_users_email UNIQUE (email);

ALTER TABLE users
    ADD CONSTRAINT uc_users_username UNIQUE (username);