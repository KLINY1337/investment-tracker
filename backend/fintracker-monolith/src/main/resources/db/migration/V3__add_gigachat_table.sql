CREATE TABLE gigachat_token
(
    access_token VARCHAR(255) NOT NULL,
    expires_at   VARCHAR(255),
    CONSTRAINT pk_gigachattoken PRIMARY KEY (access_token)
);