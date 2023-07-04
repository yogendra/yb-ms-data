CREATE EXTENSION IF NOT EXISTS "uuid-ossp" schema public;
CREATE SCHEMA IF NOT EXISTS qtodo;
CREATE TABLE IF NOT EXISTS qtodo.todo
(
    id           uuid PRIMARY KEY,
    task         VARCHAR(255),
    status       boolean
);
