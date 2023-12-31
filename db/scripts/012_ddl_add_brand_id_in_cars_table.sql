ALTER TABLE cars
    ADD COLUMN brand_id int references brands (id) NOT NULL;