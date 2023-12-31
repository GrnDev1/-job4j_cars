CREATE TABLE files
(
    id   SERIAL PRIMARY KEY,
    name VARCHAR NOT NULL,
    path VARCHAR NOT NULL unique,
    post_id INT REFERENCES auto_post(id)
);
