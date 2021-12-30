CREATE TABLE persons (
    id serial primary key,
    username varchar (255),
    password varchar (255),
    role_id integer references roles(id)
);

CREATE TABLE roles (
    id serial primary key,
    name varchar (255),
);

CREATE TABLE messages (
    id serial primary key,
    created timestamp,
    text varchar (1000),
    creator_id integer references persons(id),
    room_id integer references rooms(id)
);

CREATE TABLE rooms (
    id serial primary key,
    name varchar (255),
    creator_id integer references persons(id)
);

insert into roles (name) values ('ROLE_ADMIN');
insert into roles (name) values ('ROLE_USER');


insert into persons (username, password, role_id) values ('root', 'root', 1);

insert into rooms (name,  creator_id) values ('test room', 1);

insert into messages (created, text, creator_id, room_id)
values ('2021-12-30 15:00:00.00000', 'test message', 1, 1);