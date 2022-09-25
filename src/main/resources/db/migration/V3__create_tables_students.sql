create table if not exists courses_students
(
    id              bigint not null auto_increment primary key,
    course_id       bigint not null,
    student_id      bigint not null,
    date_registered date   not null,
    comments        varchar(255),
    likes           boolean default 0,

    foreign key (course_id) references courses (id) on delete cascade,
    foreign key (student_id) references users (id) on delete cascade
);

create table if not exists contents_students
(
    id         bigint  not null auto_increment primary key,
    content_id bigint  not null,
    student_id bigint  not null,
    passed     boolean not null default 0,

    foreign key (content_id) references courses_contents (id) on delete cascade,
    foreign key (student_id) references users (id) on delete cascade
);
