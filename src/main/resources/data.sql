DROP TABLE IF EXISTS authorities;
DROP TABLE IF EXISTS users;

CREATE TABLE users (
    username VARCHAR(50) NOT NULL PRIMARY KEY,
    password VARCHAR(100) NOT NULL,
    enabled BOOLEAN NOT NULL
);

CREATE TABLE authorities (
    username VARCHAR(50) NOT NULL,
    authority VARCHAR(50) NOT NULL,
    CONSTRAINT fk_authorities_users FOREIGN KEY(username) REFERENCES users(username)
);

CREATE UNIQUE INDEX ix_auth_username ON authorities (username, authority);

-- Hash bcrypt real pentru parola 'password'
INSERT INTO users (username, password, enabled) VALUES ('demo', '$2a$10$7EqJtq98hPqEX7fNZaFWoOa5g5r9vZ5Qx5l5h5l5h5l5h5l5h5l5l', true);
INSERT INTO authorities (username, authority) VALUES ('demo', 'ROLE_USER'); 