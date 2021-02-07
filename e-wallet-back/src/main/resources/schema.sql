CREATE TABLE IF NOT EXISTS users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL
);

INSERT INTO users (username, password) VALUES ('admin', '$2y$12$XMja0Hr0vIQrINexs5zn1OQkjPtQhyhUuXDLADOqWBI9lJOa0rxCu');

CREATE TABLE IF NOT EXISTS wallets (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    name VARCHAR(255) NOT NULL,
    balance DECIMAL(15, 2) NOT NULL DEFAULT 0.0,
    CHECK (balance >= 0.0),
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

INSERT INTO wallets (user_id, name, balance) VALUES
    (1, 'kiwi', 10000.00),
    (1, 'azcoin', 20.00);
