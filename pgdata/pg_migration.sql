create table if not exists request (
    request_id int generated always as identity primary key,

    client_from_id int,
    client_to_id int,
    message varchar(1000),
    satisfied boolean not null,
    time_changed timestamp not null
);