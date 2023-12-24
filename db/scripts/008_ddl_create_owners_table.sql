create table owners
(
    id       serial primary key,
    name    varchar not null,
    user_id int not null UNIQUE REFERENCES auto_user(id)
);
