ALTER TABLE tickers
    ADD CONSTRAINT uc_ae0c0961621d283d4f233e347 UNIQUE (exchange_ticker_symbol, exchange_name);