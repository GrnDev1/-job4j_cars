CREATE TABLE history_owners
(
    id       SERIAL PRIMARY KEY,
    car_id   INT       NOT NULL REFERENCES cars (id),
    owner_id INT       NOT NULL REFERENCES owners (id),
    startAt  TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    endAt    TIMESTAMP WITHOUT TIME ZONE NOT NULL
);
