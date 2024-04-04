ALTER TABLE gigachat_token
    ALTER COLUMN access_token TYPE VARCHAR(1200) USING (access_token::VARCHAR(1200));