create table if not exists courses
(
    id          bigint        not null auto_increment primary key,
    name        varchar(255)  not null unique,
    description varchar(1000) not null,
    date_start  date          not null,
    date_end    date          not null,
    status      varchar(20)   not null,
    logo_url    varchar(255),
    movie_url   varchar(255)
);

create table if not exists courses_contents
(
    id        bigint       not null auto_increment primary key,
    course_id bigint,
    name      varchar(255) not null unique,
    text      text,
    movie_url varchar(255),
    type      varchar(50)  not null,
    foreign key (course_id) references courses (id) on delete cascade
);

create table if not exists courses_mentors
(
    course_id bigint not null,
    mentor_id bigint not null,
    foreign key (course_id) references courses (id) on delete cascade,
    foreign key (mentor_id) references users (id) on delete cascade,

    constraint program_mentor UNIQUE (course_id, mentor_id)
);


