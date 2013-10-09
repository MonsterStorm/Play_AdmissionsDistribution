# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table advertisment (
  id                        bigint auto_increment not null,
  title                     varchar(255),
  logo                      varchar(255),
  url                       varchar(255),
  create_time               bigint,
  last_modified             bigint,
  info                      longtext,
  advertisment_statistics_id bigint,
  constraint pk_advertisment primary key (id))
;

create table advertisment_statistics (
  id                        bigint auto_increment not null,
  advertisment_id           bigint,
  total_visit               bigint,
  start_time                bigint,
  last_visited              bigint,
  constraint pk_advertisment_statistics primary key (id))
;

create table advertisment_visit (
  id                        bigint auto_increment not null,
  ip                        varchar(255),
  domain                    varchar(255),
  browser                   varchar(255),
  time                      bigint,
  advertisment_id           bigint,
  constraint pk_advertisment_visit primary key (id))
;

create table agent (
  id                        bigint auto_increment not null,
  user_id                   bigint,
  template_id               bigint,
  audit_id                  bigint,
  name                      varchar(255),
  info                      varchar(255),
  contact                   varchar(255),
  constraint pk_agent primary key (id))
;

create table audit (
  id                        bigint auto_increment not null,
  type_id                   bigint,
  status                    integer,
  creator_id                bigint,
  course_id                 bigint,
  create_time               bigint,
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
  contract_type_id          bigint,
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
  audit_id                  bigint,
  course_type_id            bigint,
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
  audit_id                  bigint,
  template_id               bigint,
  name                      varchar(255),
  info                      longtext,
  constraint pk_education_institution primary key (id))
;

create table enroll (
  id                        bigint auto_increment not null,
  student_id                bigint,
  course_id                 bigint,
  from_agent_id             bigint,
  edu_id                    bigint,
  audit_of_agent_id         bigint,
  audit_of_edu_id           bigint,
  confirm_of_edu_id         bigint,
  confirm_of_platform_id    bigint,
  confirm_of_agent_id       bigint,
  enroll_time               bigint,
  enroll_ip                 varchar(255),
  enroll_domain             varchar(255),
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
  template_id               bigint,
  job_title                 varchar(255),
  audit_id                  bigint,
  create_time               bigint,
  info                      longtext,
  field                     varchar(255),
  constraint pk_instructor primary key (id))
;

create table log_login (
  id                        bigint auto_increment not null,
  user_id                   bigint,
  time                      bigint,
  log_type                  integer,
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
  title                     longtext,
  content                   longtext,
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
  news_type_id              bigint,
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

create table rebate (
  id                        bigint auto_increment not null,
  course_id                 bigint,
  num_of_students           integer,
  total_money               double,
  num_edu_admit             integer,
  money_edu_admit           double,
  last_receipt_of_edu_id    bigint,
  type_to_platform_id       bigint,
  rebate_to_platform        double,
  num_platform_admit        integer,
  money_platform_admit      double,
  last_receipt_of_platform_id bigint,
  type_to_agent_id          bigint,
  rebate_to_agent           double,
  num_agent_admit           integer,
  money_agent_admit         double,
  last_receipt_of_agent_id  bigint,
  constraint pk_rebate primary key (id))
;

create table rebate_type (
  id                        bigint auto_increment not null,
  ratio_of_total            double,
  ratio_of_per_student      double,
  rebate_id                 bigint,
  constraint pk_rebate_type primary key (id))
;

create table reply (
  id                        bigint auto_increment not null,
  user_id                   bigint,
  title                     varchar(255),
  content                   longtext,
  time                      bigint,
  message_id                bigint,
  constraint pk_reply primary key (id))
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
  company_name              varchar(255),
  position                  varchar(255),
  info                      varchar(255),
  constraint pk_student primary key (id))
;

create table studentwords (
  id                        bigint auto_increment not null,
  name                      varchar(255),
  company                   varchar(255),
  words                     varchar(255),
  constraint pk_studentwords primary key (id))
;

create table template (
  id                        bigint auto_increment not null,
  user_id                   bigint,
  edu_id                    bigint,
  instructor_id             bigint,
  agent_id                  bigint,
  index_path                varchar(255),
  template_type_id          bigint,
  constraint pk_template primary key (id))
;

create table template_type (
  id                        bigint auto_increment not null,
  name                      varchar(255),
  info                      longtext,
  last_modified             bigint,
  url                       varchar(255),
  logo                      varchar(255),
  constraint pk_template_type primary key (id))
;

create table user (
  id                        bigint auto_increment not null,
  username                  varchar(255),
  password                  varchar(255),
  nickname                  varchar(255),
  mobile                    varchar(255),
  email                     varchar(255),
  logo                      varchar(255),
  audit_id                  bigint,
  basic_info_id             bigint,
  parent_account_id         bigint,
  instructor_id             bigint,
  agent_id                  bigint,
  student_id                bigint,
  constraint pk_user primary key (id))
;

create table user_info (
  id                        bigint auto_increment not null,
  user_id                   bigint,
  realname                  varchar(255),
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


create table agent_course (
  agent_id                       bigint not null,
  course_id                      bigint not null,
  constraint pk_agent_course primary key (agent_id, course_id))
;

create table module_function (
  module_id                      bigint not null,
  function_id                    bigint not null,
  constraint pk_module_function primary key (module_id, function_id))
;

create table role_module (
  role_id                        bigint not null,
  module_id                      bigint not null,
  constraint pk_role_module primary key (role_id, module_id))
;

create table user_role (
  user_id                        bigint not null,
  role_id                        bigint not null,
  constraint pk_user_role primary key (user_id, role_id))
;
alter table advertisment add constraint fk_advertisment_advertismentStatistics_1 foreign key (advertisment_statistics_id) references advertisment_statistics (id) on delete restrict on update restrict;
create index ix_advertisment_advertismentStatistics_1 on advertisment (advertisment_statistics_id);
alter table advertisment_statistics add constraint fk_advertisment_statistics_advertisment_2 foreign key (advertisment_id) references advertisment (id) on delete restrict on update restrict;
create index ix_advertisment_statistics_advertisment_2 on advertisment_statistics (advertisment_id);
alter table advertisment_visit add constraint fk_advertisment_visit_advertisment_3 foreign key (advertisment_id) references advertisment (id) on delete restrict on update restrict;
create index ix_advertisment_visit_advertisment_3 on advertisment_visit (advertisment_id);
alter table agent add constraint fk_agent_user_4 foreign key (user_id) references user (id) on delete restrict on update restrict;
create index ix_agent_user_4 on agent (user_id);
alter table agent add constraint fk_agent_template_5 foreign key (template_id) references template (id) on delete restrict on update restrict;
create index ix_agent_template_5 on agent (template_id);
alter table agent add constraint fk_agent_audit_6 foreign key (audit_id) references audit (id) on delete restrict on update restrict;
create index ix_agent_audit_6 on agent (audit_id);
alter table audit add constraint fk_audit_type_7 foreign key (type_id) references audit_type (id) on delete restrict on update restrict;
create index ix_audit_type_7 on audit (type_id);
alter table audit add constraint fk_audit_creator_8 foreign key (creator_id) references user (id) on delete restrict on update restrict;
create index ix_audit_creator_8 on audit (creator_id);
alter table audit add constraint fk_audit_course_9 foreign key (course_id) references course (id) on delete restrict on update restrict;
create index ix_audit_course_9 on audit (course_id);
alter table audit add constraint fk_audit_auditor_10 foreign key (auditor_id) references user (id) on delete restrict on update restrict;
create index ix_audit_auditor_10 on audit (auditor_id);
alter table confirm_receipt add constraint fk_confirm_receipt_confirmer_11 foreign key (confirmer_id) references user (id) on delete restrict on update restrict;
create index ix_confirm_receipt_confirmer_11 on confirm_receipt (confirmer_id);
alter table contract add constraint fk_contract_contractType_12 foreign key (contract_type_id) references contract_type (id) on delete restrict on update restrict;
create index ix_contract_contractType_12 on contract (contract_type_id);
alter table course add constraint fk_course_audit_13 foreign key (audit_id) references audit (id) on delete restrict on update restrict;
create index ix_course_audit_13 on course (audit_id);
alter table course add constraint fk_course_courseType_14 foreign key (course_type_id) references course_type (id) on delete restrict on update restrict;
create index ix_course_courseType_14 on course (course_type_id);
alter table course add constraint fk_course_edu_15 foreign key (edu_id) references education_institution (id) on delete restrict on update restrict;
create index ix_course_edu_15 on course (edu_id);
alter table course add constraint fk_course_instructor_16 foreign key (instructor_id) references instructor (id) on delete restrict on update restrict;
create index ix_course_instructor_16 on course (instructor_id);
alter table domain add constraint fk_domain_agent_17 foreign key (agent_id) references agent (id) on delete restrict on update restrict;
create index ix_domain_agent_17 on domain (agent_id);
alter table education_institution add constraint fk_education_institution_creator_18 foreign key (creator_id) references user (id) on delete restrict on update restrict;
create index ix_education_institution_creator_18 on education_institution (creator_id);
alter table education_institution add constraint fk_education_institution_audit_19 foreign key (audit_id) references audit (id) on delete restrict on update restrict;
create index ix_education_institution_audit_19 on education_institution (audit_id);
alter table education_institution add constraint fk_education_institution_template_20 foreign key (template_id) references template (id) on delete restrict on update restrict;
create index ix_education_institution_template_20 on education_institution (template_id);
alter table enroll add constraint fk_enroll_student_21 foreign key (student_id) references student (id) on delete restrict on update restrict;
create index ix_enroll_student_21 on enroll (student_id);
alter table enroll add constraint fk_enroll_course_22 foreign key (course_id) references course (id) on delete restrict on update restrict;
create index ix_enroll_course_22 on enroll (course_id);
alter table enroll add constraint fk_enroll_fromAgent_23 foreign key (from_agent_id) references agent (id) on delete restrict on update restrict;
create index ix_enroll_fromAgent_23 on enroll (from_agent_id);
alter table enroll add constraint fk_enroll_edu_24 foreign key (edu_id) references education_institution (id) on delete restrict on update restrict;
create index ix_enroll_edu_24 on enroll (edu_id);
alter table enroll add constraint fk_enroll_auditOfAgent_25 foreign key (audit_of_agent_id) references audit (id) on delete restrict on update restrict;
create index ix_enroll_auditOfAgent_25 on enroll (audit_of_agent_id);
alter table enroll add constraint fk_enroll_auditOfEdu_26 foreign key (audit_of_edu_id) references audit (id) on delete restrict on update restrict;
create index ix_enroll_auditOfEdu_26 on enroll (audit_of_edu_id);
alter table enroll add constraint fk_enroll_confirmOfEdu_27 foreign key (confirm_of_edu_id) references confirm_receipt (id) on delete restrict on update restrict;
create index ix_enroll_confirmOfEdu_27 on enroll (confirm_of_edu_id);
alter table enroll add constraint fk_enroll_confirmOfPlatform_28 foreign key (confirm_of_platform_id) references confirm_receipt (id) on delete restrict on update restrict;
create index ix_enroll_confirmOfPlatform_28 on enroll (confirm_of_platform_id);
alter table enroll add constraint fk_enroll_confirmOfAgent_29 foreign key (confirm_of_agent_id) references confirm_receipt (id) on delete restrict on update restrict;
create index ix_enroll_confirmOfAgent_29 on enroll (confirm_of_agent_id);
alter table instructor add constraint fk_instructor_user_30 foreign key (user_id) references user (id) on delete restrict on update restrict;
create index ix_instructor_user_30 on instructor (user_id);
alter table instructor add constraint fk_instructor_template_31 foreign key (template_id) references template (id) on delete restrict on update restrict;
create index ix_instructor_template_31 on instructor (template_id);
alter table instructor add constraint fk_instructor_audit_32 foreign key (audit_id) references audit (id) on delete restrict on update restrict;
create index ix_instructor_audit_32 on instructor (audit_id);
alter table log_login add constraint fk_log_login_user_33 foreign key (user_id) references user (id) on delete restrict on update restrict;
create index ix_log_login_user_33 on log_login (user_id);
alter table log_operation add constraint fk_log_operation_user_34 foreign key (user_id) references user (id) on delete restrict on update restrict;
create index ix_log_operation_user_34 on log_operation (user_id);
alter table log_operation add constraint fk_log_operation_function_35 foreign key (function_id) references function (id) on delete restrict on update restrict;
create index ix_log_operation_function_35 on log_operation (function_id);
alter table news add constraint fk_news_newsType_36 foreign key (news_type_id) references news_type (id) on delete restrict on update restrict;
create index ix_news_newsType_36 on news (news_type_id);
alter table rebate add constraint fk_rebate_course_37 foreign key (course_id) references course (id) on delete restrict on update restrict;
create index ix_rebate_course_37 on rebate (course_id);
alter table rebate add constraint fk_rebate_lastReceiptOfEdu_38 foreign key (last_receipt_of_edu_id) references confirm_receipt (id) on delete restrict on update restrict;
create index ix_rebate_lastReceiptOfEdu_38 on rebate (last_receipt_of_edu_id);
alter table rebate add constraint fk_rebate_typeToPlatform_39 foreign key (type_to_platform_id) references rebate_type (id) on delete restrict on update restrict;
create index ix_rebate_typeToPlatform_39 on rebate (type_to_platform_id);
alter table rebate add constraint fk_rebate_lastReceiptOfPlatform_40 foreign key (last_receipt_of_platform_id) references confirm_receipt (id) on delete restrict on update restrict;
create index ix_rebate_lastReceiptOfPlatform_40 on rebate (last_receipt_of_platform_id);
alter table rebate add constraint fk_rebate_typeToAgent_41 foreign key (type_to_agent_id) references rebate_type (id) on delete restrict on update restrict;
create index ix_rebate_typeToAgent_41 on rebate (type_to_agent_id);
alter table rebate add constraint fk_rebate_lastReceiptOfAgent_42 foreign key (last_receipt_of_agent_id) references confirm_receipt (id) on delete restrict on update restrict;
create index ix_rebate_lastReceiptOfAgent_42 on rebate (last_receipt_of_agent_id);
alter table rebate_type add constraint fk_rebate_type_rebate_43 foreign key (rebate_id) references rebate (id) on delete restrict on update restrict;
create index ix_rebate_type_rebate_43 on rebate_type (rebate_id);
alter table reply add constraint fk_reply_user_44 foreign key (user_id) references user (id) on delete restrict on update restrict;
create index ix_reply_user_44 on reply (user_id);
alter table reply add constraint fk_reply_message_45 foreign key (message_id) references message (id) on delete restrict on update restrict;
create index ix_reply_message_45 on reply (message_id);
alter table student add constraint fk_student_user_46 foreign key (user_id) references user (id) on delete restrict on update restrict;
create index ix_student_user_46 on student (user_id);
alter table template add constraint fk_template_user_47 foreign key (user_id) references user (id) on delete restrict on update restrict;
create index ix_template_user_47 on template (user_id);
alter table template add constraint fk_template_edu_48 foreign key (edu_id) references education_institution (id) on delete restrict on update restrict;
create index ix_template_edu_48 on template (edu_id);
alter table template add constraint fk_template_instructor_49 foreign key (instructor_id) references instructor (id) on delete restrict on update restrict;
create index ix_template_instructor_49 on template (instructor_id);
alter table template add constraint fk_template_agent_50 foreign key (agent_id) references agent (id) on delete restrict on update restrict;
create index ix_template_agent_50 on template (agent_id);
alter table template add constraint fk_template_templateType_51 foreign key (template_type_id) references template_type (id) on delete restrict on update restrict;
create index ix_template_templateType_51 on template (template_type_id);
alter table user add constraint fk_user_audit_52 foreign key (audit_id) references audit (id) on delete restrict on update restrict;
create index ix_user_audit_52 on user (audit_id);
alter table user add constraint fk_user_basicInfo_53 foreign key (basic_info_id) references user_info (id) on delete restrict on update restrict;
create index ix_user_basicInfo_53 on user (basic_info_id);
alter table user add constraint fk_user_parentAccount_54 foreign key (parent_account_id) references user (id) on delete restrict on update restrict;
create index ix_user_parentAccount_54 on user (parent_account_id);
alter table user add constraint fk_user_instructor_55 foreign key (instructor_id) references instructor (id) on delete restrict on update restrict;
create index ix_user_instructor_55 on user (instructor_id);
alter table user add constraint fk_user_agent_56 foreign key (agent_id) references agent (id) on delete restrict on update restrict;
create index ix_user_agent_56 on user (agent_id);
alter table user add constraint fk_user_student_57 foreign key (student_id) references student (id) on delete restrict on update restrict;
create index ix_user_student_57 on user (student_id);
alter table user_info add constraint fk_user_info_user_58 foreign key (user_id) references user (id) on delete restrict on update restrict;
create index ix_user_info_user_58 on user_info (user_id);



alter table agent_course add constraint fk_agent_course_agent_01 foreign key (agent_id) references agent (id) on delete restrict on update restrict;

alter table agent_course add constraint fk_agent_course_course_02 foreign key (course_id) references course (id) on delete restrict on update restrict;

alter table module_function add constraint fk_module_function_module_01 foreign key (module_id) references module (id) on delete restrict on update restrict;

alter table module_function add constraint fk_module_function_function_02 foreign key (function_id) references function (id) on delete restrict on update restrict;

alter table role_module add constraint fk_role_module_role_01 foreign key (role_id) references role (id) on delete restrict on update restrict;

alter table role_module add constraint fk_role_module_module_02 foreign key (module_id) references module (id) on delete restrict on update restrict;

alter table user_role add constraint fk_user_role_user_01 foreign key (user_id) references user (id) on delete restrict on update restrict;

alter table user_role add constraint fk_user_role_role_02 foreign key (role_id) references role (id) on delete restrict on update restrict;

# --- !Downs

SET FOREIGN_KEY_CHECKS=0;

drop table advertisment;

drop table advertisment_statistics;

drop table advertisment_visit;

drop table agent;

drop table agent_course;

drop table audit;

drop table audit_type;

drop table confirm_receipt;

drop table contract;

drop table contract_type;

drop table course;

drop table course_type;

drop table domain;

drop table education_institution;

drop table enroll;

drop table function;

drop table module_function;

drop table instructor;

drop table log_login;

drop table log_operation;

drop table message;

drop table module;

drop table role_module;

drop table news;

drop table news_type;

drop table project;

drop table rebate;

drop table rebate_type;

drop table reply;

drop table role;

drop table user_role;

drop table scholl_fellow;

drop table student;

drop table studentwords;

drop table template;

drop table template_type;

drop table user;

drop table user_info;

SET FOREIGN_KEY_CHECKS=1;

