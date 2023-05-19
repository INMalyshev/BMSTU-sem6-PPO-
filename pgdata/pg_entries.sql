create table if not exists role (
    role_id int generated always as identity primary key,
    name varchar(200) unique not null
);

insert into role (name) values 
    ('Тренер'),
    ('Авторизованный пользователь')
    on conflict do nothing;

create table if not exists client (
    client_id int generated always as identity primary key,
    name varchar(200) not null,
    role_id int not null,

    foreign key (role_id) references role(role_id) 
);

create table if not exists training_plan (
    training_plan_id int generated always as identity primary key,
    creator_user_id int not null,

    foreign key (creator_user_id) references client(client_id)
);

create table if not exists excersize_type (
    excersize_type_id int generated always as identity primary key,
    name varchar(200) not null unique
);

insert into excersize_type (name) values 
    ('Подтягивания'),
    ('Присядания'),
    ('Отжимания')
    on conflict do nothing;

create table if not exists approach_plan (
    approach_plan_id int generated always as identity primary key,
    expected_amount int not null,
    training_plan_id int not null,
    excersize_type_id int not null,

    foreign key (training_plan_id) references training_plan(training_plan_id),
    foreign key (excersize_type_id) references excersize_type(excersize_type_id)
);

create table if not exists training (
    training_id int generated always as identity primary key,
    completed boolean not null,
    holder_user_id int not null,

    foreign key (holder_user_id) references client(client_id) 
);

create table if not exists approach (
    approach_id int generated always as identity primary key,
    training_id int not null,
    amount int not null,
    expected_amount int not null,
    excersize_type_id int not null,
    completed boolean not null,

    foreign key (training_id) references training(training_id),
    foreign key (excersize_type_id) references excersize_type(excersize_type_id)
);
