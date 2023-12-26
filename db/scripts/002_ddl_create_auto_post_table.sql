create table auto_post
(
    id           serial primary key,
    description  varchar not null,
    created      TIMESTAMP WITHOUT TIME ZONE DEFAULT now(),
    auto_user_id int references auto_user (id)
);