CREATE TABLE assets
(
    id              BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    symbol          VARCHAR(255),
    asset_type_name VARCHAR(255),
    CONSTRAINT pk_assets PRIMARY KEY (id)
);

CREATE TABLE investment_positions
(
    id                      BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    ticker_id               BIGINT,
    portfolio_id            BIGINT,
    open_date               TIMESTAMP WITHOUT TIME ZONE,
    close_date              TIMESTAMP WITHOUT TIME ZONE,
    open_quote_asset_price  DECIMAL,
    close_quote_asset_price DECIMAL,
    base_asset_amount       DECIMAL,
    CONSTRAINT pk_investment_positions PRIMARY KEY (id)
);

CREATE TABLE portfolios
(
    id      BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    user_id BIGINT,
    name    VARCHAR(255),
    CONSTRAINT pk_portfolios PRIMARY KEY (id)
);

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

CREATE TABLE tickers
(
    id                     BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    symbol                 VARCHAR(255),
    exchange_ticker_symbol VARCHAR(255),
    exchange_name          VARCHAR(255),
    base_asset_id          BIGINT,
    quote_asset_id         BIGINT,
    denomination_type      VARCHAR(255),
    expiration_type        VARCHAR(255),
    market_type            VARCHAR(255),
    in_use                 BOOLEAN,
    CONSTRAINT pk_tickers PRIMARY KEY (id)
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

ALTER TABLE investment_positions
    ADD CONSTRAINT FK_INVESTMENT_POSITIONS_ON_PORTFOLIO FOREIGN KEY (portfolio_id) REFERENCES portfolios (id);

ALTER TABLE investment_positions
    ADD CONSTRAINT FK_INVESTMENT_POSITIONS_ON_TICKER FOREIGN KEY (ticker_id) REFERENCES tickers (id);

ALTER TABLE portfolios
    ADD CONSTRAINT FK_PORTFOLIOS_ON_USER FOREIGN KEY (user_id) REFERENCES users (id);

ALTER TABLE tickers
    ADD CONSTRAINT FK_TICKERS_ON_BASE_ASSET FOREIGN KEY (base_asset_id) REFERENCES assets (id);

ALTER TABLE tickers
    ADD CONSTRAINT FK_TICKERS_ON_QUOTE_ASSET FOREIGN KEY (quote_asset_id) REFERENCES assets (id);