ALTER TABLE auto_post
    ADD COLUMN file_id int references files (id);