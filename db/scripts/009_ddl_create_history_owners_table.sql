CREATE TABLE history_owners
(
    id      SERIAL PRIMARY KEY,
    car_id int NOT NULL REFERENCES cars (id),
    owner_id int NOT NULL REFERENCES owners (id)
);