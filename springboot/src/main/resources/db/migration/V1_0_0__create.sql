CREATE EXTENSION IF NOT EXISTS "uuid-ossp";
CREATE TABLE IF NOT EXISTS todo
(
    id           uuid PRIMARY KEY,
    task         VARCHAR(255),
    status       boolean
);
