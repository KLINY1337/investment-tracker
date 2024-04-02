ALTER TABLE gigachat_token
    ALTER COLUMN access_token TYPE VARCHAR(32768) USING (access_token::VARCHAR(32768));