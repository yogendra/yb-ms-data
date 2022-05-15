CREATE EXTENSION IF NOT EXISTS "uuid-ossp" schema public;
CREATE TABLE IF NOT EXISTS todo
(
    id           uuid PRIMARY KEY,
    task         VARCHAR(255),
    status       boolean
);
