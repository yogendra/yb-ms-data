CREATE EXTENSION IF NOT EXISTS "uuid-ossp" schema public;
CREATE SCHEMA IF NOT EXISTS stodo;
CREATE TABLE IF NOT EXISTS stodo.todo
(
    id           uuid PRIMARY KEY,
    task         VARCHAR(255),
    status       boolean
);
