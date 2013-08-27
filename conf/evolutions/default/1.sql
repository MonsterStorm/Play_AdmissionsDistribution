# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table course (
  id                        bigint auto_increment not null,
  name                      varchar(255),
  money                     double,
  start_time                bigint,
  contact                   varchar(255),
  info                      longtext,
  detail                    longtext,
  constraint pk_course primary key (id))
;

create table course_type (
  id                        bigint auto_increment not null,
  name                      varchar(255),
  constraint pk_course_type primary key (id))
;

create table domain (
  id                        bigint auto_increment not null,
  domain                    varchar(255),
  user_id                   bigint,
  constraint pk_domain primary key (id))
;

create table enroll (
  id                        bigint auto_increment not null,
  enroll_time               bigint,
  enroll_ip                 varchar(255),
  enroll_domain             varchar(255),
  name                      varchar(255),
  sex                       integer,
  idcard                    varchar(255),
  birth                     bigint,
  company_name              varchar(255),
  position                  varchar(255),
  phone                     varchar(255),
  mobile                    varchar(255),
  address                   varchar(255),
  qq                        varchar(255),
  email                     varchar(255),
  info                      varchar(255),
  constraint pk_enroll primary key (id))
;

create table function (
  id                        bigint auto_increment not null,
  name                      varchar(255),
  info                      varchar(255),
  constraint pk_function primary key (id))
;

create table log_login (
  id                        bigint auto_increment not null,
  time                      bigint,
  type                      integer,
  constraint pk_log_login primary key (id))
;

create table log_operation (
  id                        bigint auto_increment not null,
  user_id                   bigint,
  time                      bigint,
  function_id               bigint,
  constraint pk_log_operation primary key (id))
;

create table message (
  id                        bigint auto_increment not null,
  name                      varchar(255),
  phone                     varchar(255),
  qq                        varchar(255),
  email                     varchar(255),
  address                   varchar(255),
  time                      bigint,
  constraint pk_message primary key (id))
;

create table news (
  id                        bigint auto_increment not null,
  title                     varchar(255),
  detail                    longtext,
  type_id                   bigint,
  time                      bigint,
  constraint pk_news primary key (id))
;

create table news_type (
  id                        bigint auto_increment not null,
  name                      varchar(255),
  constraint pk_news_type primary key (id))
;

create table project (
  id                        bigint auto_increment not null,
  name                      varchar(255),
  info                      varchar(255),
  status                    integer,
  constraint pk_project primary key (id))
;

create table role (
  id                        bigint auto_increment not null,
  name                      varchar(255),
  info                      varchar(255),
  constraint pk_role primary key (id))
;

create table scholl_fellow (
  id                        bigint auto_increment not null,
  name                      varchar(255),
  company                   varchar(255),
  position                  varchar(255),
  constraint pk_scholl_fellow primary key (id))
;

create table user (
  id                        bigint auto_increment not null,
  username                  varchar(255),
  password                  varchar(255),
  nickname                  varchar(255),
  idcard                    varchar(255),
  birthday                  bigint,
  sex                       varchar(255),
  phone                     varchar(255),
  mobile                    varchar(255),
  qq                        varchar(255),
  email                     varchar(255),
  address                   varchar(255),
  info                      varchar(255),
  logo                      varchar(255),
  register_time             bigint,
  register_ip               varchar(255),
  last_login_time           bigint,
  last_login_ip             varchar(255),
  status                    integer,
  role_id                   bigint,
  constraint pk_user primary key (id))
;


create table function_role (
  function_id                    bigint not null,
  role_id                        bigint not null,
  constraint pk_function_role primary key (function_id, role_id))
;

create table role_function (
  role_id                        bigint not null,
  function_id                    bigint not null,
  constraint pk_role_function primary key (role_id, function_id))
;

create table user_course_type (
  user_id                        bigint not null,
  course_type_id                 bigint not null,
  constraint pk_user_course_type primary key (user_id, course_type_id))
;

create table user_course (
  user_id                        bigint not null,
  course_id                      bigint not null,
  constraint pk_user_course primary key (user_id, course_id))
;
alter table domain add constraint fk_domain_user_1 foreign key (user_id) references user (id) on delete restrict on update restrict;
create index ix_domain_user_1 on domain (user_id);
alter table log_operation add constraint fk_log_operation_user_2 foreign key (user_id) references user (id) on delete restrict on update restrict;
create index ix_log_operation_user_2 on log_operation (user_id);
alter table log_operation add constraint fk_log_operation_function_3 foreign key (function_id) references function (id) on delete restrict on update restrict;
create index ix_log_operation_function_3 on log_operation (function_id);
alter table news add constraint fk_news_type_4 foreign key (type_id) references news_type (id) on delete restrict on update restrict;
create index ix_news_type_4 on news (type_id);
alter table user add constraint fk_user_role_5 foreign key (role_id) references role (id) on delete restrict on update restrict;
create index ix_user_role_5 on user (role_id);



alter table function_role add constraint fk_function_role_function_01 foreign key (function_id) references function (id) on delete restrict on update restrict;

alter table function_role add constraint fk_function_role_role_02 foreign key (role_id) references role (id) on delete restrict on update restrict;

alter table role_function add constraint fk_role_function_role_01 foreign key (role_id) references role (id) on delete restrict on update restrict;

alter table role_function add constraint fk_role_function_function_02 foreign key (function_id) references function (id) on delete restrict on update restrict;

alter table user_course_type add constraint fk_user_course_type_user_01 foreign key (user_id) references user (id) on delete restrict on update restrict;

alter table user_course_type add constraint fk_user_course_type_course_type_02 foreign key (course_type_id) references course_type (id) on delete restrict on update restrict;

alter table user_course add constraint fk_user_course_user_01 foreign key (user_id) references user (id) on delete restrict on update restrict;

alter table user_course add constraint fk_user_course_course_02 foreign key (course_id) references course (id) on delete restrict on update restrict;

# --- !Downs

SET FOREIGN_KEY_CHECKS=0;

drop table course;

drop table course_type;

drop table domain;

drop table enroll;

drop table function;

drop table function_role;

drop table log_login;

drop table log_operation;

drop table message;

drop table news;

drop table news_type;

drop table project;

drop table role;

drop table role_function;

drop table scholl_fellow;

drop table user;

drop table user_course_type;

drop table user_course;

SET FOREIGN_KEY_CHECKS=1;

