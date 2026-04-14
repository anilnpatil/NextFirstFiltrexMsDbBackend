IF NOT EXISTS (SELECT 1 FROM dbo.users WHERE username = 'admin')
BEGIN
    INSERT INTO dbo.users (username, password, role)
    VALUES (
        'admin',
        '$2a$10$kcsvYn3bOPLc/wfh9WK0Ruca10RbZfld49lgUvBdVHG3tb8ycQw3u',
        'ADMIN'
    );
END;

        -- password:test1234