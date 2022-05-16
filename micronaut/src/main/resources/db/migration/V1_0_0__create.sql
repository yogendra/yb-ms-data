CREATE EXTENSION IF NOT EXISTS "uuid-ossp" schema public;
CREATE SCHEMA IF NOT EXISTS mtodo;
CREATE TABLE IF NOT EXISTS mtodo.todo
(
    id           uuid PRIMARY KEY,
    task         VARCHAR(255),
    status       boolean
);
