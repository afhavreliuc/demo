DELETE FROM authorities WHERE username = 'demo';
DELETE FROM users WHERE username = 'demo';

INSERT INTO users (username, password, enabled) VALUES ('demo', '$2a$10$CwTycUXWue0Thq9StjUM0uJ8i6rQ4p1rQ4p1rQ4p1rQ4p1rQ4p1rG', true);
INSERT INTO authorities (username, authority) VALUES ('demo', 'ROLE_USER');
