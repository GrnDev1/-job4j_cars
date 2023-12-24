ALTER TABLE auto_post
    ADD COLUMN car_id int references cars (id) NOT NULL;