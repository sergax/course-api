create table if not exists users
(
    id          bigint auto_increment primary key,
    first_name  varchar(50),
    last_name   varchar(50),
    email       varchar(255) not null unique,
    created     timestamp    not null,
    updated     timestamp    not null,
    password    varchar(64)  not null unique,
    user_status varchar(15)  not null
);

create table if not exists roles
(
    id   bigint auto_increment primary key,
    name varchar(32)
);

create table if not exists users_roles
(
    user_id bigint not null unique,
    role_id bigint not null unique,

    foreign key (user_id)
        references users (id)
        on delete cascade,

    foreign key (role_id)
        references roles (id)
        on delete cascade
);

# insert into roles (id, name)
# values (1, 'ADMIN'),
#        (2, 'USER')

