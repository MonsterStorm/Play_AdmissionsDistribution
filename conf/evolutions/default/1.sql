# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table agent (
  id                        bigint auto_increment not null,
  user_id                   bigint,
  constraint pk_agent primary key (id))
;

create table audit (
  id                        bigint auto_increment not null,
  type_id                   bigint,
  creator_id                bigint,
  create_tile               bigint,
  auditor_id                bigint,
  audit_time                bigint,
  constraint pk_audit primary key (id))
;

create table audit_type (
  id                        bigint auto_increment not null,
  name                      varchar(255),
  info                      varchar(255),
  constraint pk_audit_type primary key (id))
;

create table confirm_receipt (
  id                        bigint auto_increment not null,
  confirmer_id              bigint,
  time                      bigint,
  money                     double,
  info                      varchar(255),
  constraint pk_confirm_receipt primary key (id))
;

create table contract (
  id                        bigint auto_increment not null,
  name                      varchar(255),
  detail                    longtext,
  info                      longtext,
  type_id                   bigint,
  create_time               bigint,
  last_modified             bigint,
  constraint pk_contract primary key (id))
;

create table contract_type (
  id                        bigint auto_increment not null,
  name                      varchar(255),
  info                      varchar(255),
  constraint pk_contract_type primary key (id))
;

create table course (
  id                        bigint auto_increment not null,
  name                      varchar(255),
  money                     double,
  start_time                bigint,
  contact                   varchar(255),
  info                      longtext,
  detail                    longtext,
  edu_id                    bigint,
  instructor_id             bigint,
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
  agent_id                  bigint,
  constraint pk_domain primary key (id))
;

create table education_institution (
  id                        bigint auto_increment not null,
  creator_id                bigint,
  create_time               bigint,
  name                      varchar(255),
  constraint pk_education_institution primary key (id))
;

create table enroll (
  id                        bigint auto_increment not null,
  from_agent_id             bigint,
  edu_id                    bigint,
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

create table instructor (
  id                        bigint auto_increment not null,
  user_id                   bigint,
  constraint pk_instructor primary key (id))
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

create table module (
  id                        bigint auto_increment not null,
  name                      varchar(255),
  info                      longtext,
  constraint pk_module primary key (id))
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
  info                      varchar(255),
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

create table student (
  id                        bigint auto_increment not null,
  user_id                   bigint,
  constraint pk_student primary key (id))
;

create table user (
  id                        bigint auto_increment not null,
  username                  varchar(255),
  password                  varchar(255),
  nickname                  varchar(255),
  mobile                    varchar(255),
  email                     varchar(255),
  logo                      varchar(255),
  status                    integer,
  basic_info_id             bigint,
  instructor_id             bigint,
  agent_id                  bigint,
  student_id                bigint,
  constraint pk_user primary key (id))
;

create table user_info (
  id                        bigint auto_increment not null,
  user_id                   bigint,
  idcard                    varchar(255),
  birthday                  bigint,
  sex                       varchar(255),
  phone                     varchar(255),
  qq                        varchar(255),
  address                   varchar(255),
  info                      varchar(255),
  register_time             bigint,
  register_ip               varchar(255),
  last_login_time           bigint,
  last_login_ip             varchar(255),
  constraint pk_user_info primary key (id))
;


create table education_institution_user (
  education_institution_id       bigint not null,
  user_id                        bigint not null,
  constraint pk_education_institution_user primary key (education_institution_id, user_id))
;

create table function_module (
  function_id                    bigint not null,
  module_id                      bigint not null,
  constraint pk_function_module primary key (function_id, module_id))
;

create table module_role (
  module_id                      bigint not null,
  role_id                        bigint not null,
  constraint pk_module_role primary key (module_id, role_id))
;

create table module_function (
  module_id                      bigint not null,
  function_id                    bigint not null,
  constraint pk_module_function primary key (module_id, function_id))
;

create table role_user (
  role_id                        bigint not null,
  user_id                        bigint not null,
  constraint pk_role_user primary key (role_id, user_id))
;

create table role_module (
  role_id                        bigint not null,
  module_id                      bigint not null,
  constraint pk_role_module primary key (role_id, module_id))
;
alter table agent add constraint fk_agent_user_1 foreign key (user_id) references user (id) on delete restrict on update restrict;
create index ix_agent_user_1 on agent (user_id);
alter table audit add constraint fk_audit_type_2 foreign key (type_id) references audit_type (id) on delete restrict on update restrict;
create index ix_audit_type_2 on audit (type_id);
alter table audit add constraint fk_audit_creator_3 foreign key (creator_id) references user (id) on delete restrict on update restrict;
create index ix_audit_creator_3 on audit (creator_id);
alter table audit add constraint fk_audit_auditor_4 foreign key (auditor_id) references user (id) on delete restrict on update restrict;
create index ix_audit_auditor_4 on audit (auditor_id);
alter table confirm_receipt add constraint fk_confirm_receipt_confirmer_5 foreign key (confirmer_id) references user (id) on delete restrict on update restrict;
create index ix_confirm_receipt_confirmer_5 on confirm_receipt (confirmer_id);
alter table contract add constraint fk_contract_type_6 foreign key (type_id) references contract_type (id) on delete restrict on update restrict;
create index ix_contract_type_6 on contract (type_id);
alter table course add constraint fk_course_edu_7 foreign key (edu_id) references education_institution (id) on delete restrict on update restrict;
create index ix_course_edu_7 on course (edu_id);
alter table course add constraint fk_course_instructor_8 foreign key (instructor_id) references instructor (id) on delete restrict on update restrict;
create index ix_course_instructor_8 on course (instructor_id);
alter table domain add constraint fk_domain_agent_9 foreign key (agent_id) references agent (id) on delete restrict on update restrict;
create index ix_domain_agent_9 on domain (agent_id);
alter table education_institution add constraint fk_education_institution_creator_10 foreign key (creator_id) references user (id) on delete restrict on update restrict;
create index ix_education_institution_creator_10 on education_institution (creator_id);
alter table enroll add constraint fk_enroll_fromAgent_11 foreign key (from_agent_id) references user (id) on delete restrict on update restrict;
create index ix_enroll_fromAgent_11 on enroll (from_agent_id);
alter table enroll add constraint fk_enroll_edu_12 foreign key (edu_id) references education_institution (id) on delete restrict on update restrict;
create index ix_enroll_edu_12 on enroll (edu_id);
alter table instructor add constraint fk_instructor_user_13 foreign key (user_id) references user (id) on delete restrict on update restrict;
create index ix_instructor_user_13 on instructor (user_id);
alter table log_operation add constraint fk_log_operation_user_14 foreign key (user_id) references user (id) on delete restrict on update restrict;
create index ix_log_operation_user_14 on log_operation (user_id);
alter table log_operation add constraint fk_log_operation_function_15 foreign key (function_id) references function (id) on delete restrict on update restrict;
create index ix_log_operation_function_15 on log_operation (function_id);
alter table news add constraint fk_news_type_16 foreign key (type_id) references news_type (id) on delete restrict on update restrict;
create index ix_news_type_16 on news (type_id);
alter table student add constraint fk_student_user_17 foreign key (user_id) references user (id) on delete restrict on update restrict;
create index ix_student_user_17 on student (user_id);
alter table user add constraint fk_user_basicInfo_18 foreign key (basic_info_id) references user_info (id) on delete restrict on update restrict;
create index ix_user_basicInfo_18 on user (basic_info_id);
alter table user add constraint fk_user_instructor_19 foreign key (instructor_id) references instructor (id) on delete restrict on update restrict;
create index ix_user_instructor_19 on user (instructor_id);
alter table user add constraint fk_user_agent_20 foreign key (agent_id) references agent (id) on delete restrict on update restrict;
create index ix_user_agent_20 on user (agent_id);
alter table user add constraint fk_user_student_21 foreign key (student_id) references student (id) on delete restrict on update restrict;
create index ix_user_student_21 on user (student_id);
alter table user_info add constraint fk_user_info_user_22 foreign key (user_id) references user (id) on delete restrict on update restrict;
create index ix_user_info_user_22 on user_info (user_id);



alter table education_institution_user add constraint fk_education_institution_user_education_institution_01 foreign key (education_institution_id) references education_institution (id) on delete restrict on update restrict;

alter table education_institution_user add constraint fk_education_institution_user_user_02 foreign key (user_id) references user (id) on delete restrict on update restrict;

alter table function_module add constraint fk_function_module_function_01 foreign key (function_id) references function (id) on delete restrict on update restrict;

alter table function_module add constraint fk_function_module_module_02 foreign key (module_id) references module (id) on delete restrict on update restrict;

alter table module_role add constraint fk_module_role_module_01 foreign key (module_id) references module (id) on delete restrict on update restrict;

alter table module_role add constraint fk_module_role_role_02 foreign key (role_id) references role (id) on delete restrict on update restrict;

alter table module_function add constraint fk_module_function_module_01 foreign key (module_id) references module (id) on delete restrict on update restrict;

alter table module_function add constraint fk_module_function_function_02 foreign key (function_id) references function (id) on delete restrict on update restrict;

alter table role_user add constraint fk_role_user_role_01 foreign key (role_id) references role (id) on delete restrict on update restrict;

alter table role_user add constraint fk_role_user_user_02 foreign key (user_id) references user (id) on delete restrict on update restrict;

alter table role_module add constraint fk_role_module_role_01 foreign key (role_id) references role (id) on delete restrict on update restrict;

alter table role_module add constraint fk_role_module_module_02 foreign key (module_id) references module (id) on delete restrict on update restrict;

# --- !Downs

SET FOREIGN_KEY_CHECKS=0;

drop table agent;

drop table audit;

drop table audit_type;

drop table confirm_receipt;

drop table contract;

drop table contract_type;

drop table course;

drop table course_type;

drop table domain;

drop table education_institution;

drop table education_institution_user;

drop table enroll;

drop table function;

drop table function_module;

drop table instructor;

drop table log_login;

drop table log_operation;

drop table message;

drop table module;

drop table module_role;

drop table module_function;

drop table news;

drop table news_type;

drop table project;

drop table role;

drop table role_user;

drop table role_module;

drop table scholl_fellow;

drop table student;

drop table user;

drop table user_info;

SET FOREIGN_KEY_CHECKS=1;

