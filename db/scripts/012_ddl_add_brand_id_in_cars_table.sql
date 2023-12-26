ALTER TABLE cars
    ADD COLUMN brand_id int references cars (id) NOT NULL;