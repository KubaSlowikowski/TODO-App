create table projects
(
    id              int primary key auto_increment,
    description     varchar(100) not null,
    created_on          datetime null,
    updated_on          datetime null
);
create table project_steps
(
    id                  int primary key auto_increment,
    description         varchar(100) not null,
    days_to_deadline    long not null,
    project_id          int null
);
alter table project_steps add foreign key (project_id) references projects(id);

alter table task_groups add column project_id int null;
alter table task_groups add foreign key (project_id) references projects(id);