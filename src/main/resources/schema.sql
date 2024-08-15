create table if not exists bot_users
(
    id        bigint       not null
        constraint bot_users_pk
            primary key,
    username  varchar(255) not null,
    local_tag varchar(10)
);

create table if not exists workout_weeks
(
    id              bigserial
        constraint workout_weeks_pk
            primary key,
    current         boolean,
    week_start_date date   not null,
    week_end_date   date   not null,
    user_id         bigint not null
        constraint user_id
            references bot_users
);

create table if not exists workout_days
(
    id              bigserial
        constraint workout_day_pk
            primary key,
    name            varchar(255),
    date            date   not null,
    is_workout_day  boolean,
    workout_week_id bigint not null
        constraint workout_week_id
            references workout_weeks
);

create table if not exists workout_exercises
(
    id             bigserial
        primary key,
    number         smallint,
    workout_day_id bigint not null
        constraint fk_workout_day
            references workout_days,
    name           varchar(255)
);

create table if not exists workout_sets
(
    id                 bigserial
        constraint workout_sets_pk
            primary key,
    number             integer,
    reps               integer,
    weight             integer,
    workout_ex_id      bigint
        constraint fk_workout_exercise
            references workout_exercises
            on delete cascade,
    prev_workout_ex_id bigint
        constraint fk_prev_workout_exercise
            references workout_exercises
            on delete cascade
);
