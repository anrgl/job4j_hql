create table if not exists orders
(
    id          serial primary key,
    name        varchar(50),
    description varchar(50),
    created     timestamp
);