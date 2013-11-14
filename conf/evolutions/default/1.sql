# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table advertisment (
  id                        bigint not null,
  title                     varchar(255),
  logo                      varchar(255),
  url                       varchar(255),
  create_time               bigint,
  last_modified             bigint,
  info                      clob,
  advertisment_statistics_id bigint,
  constraint pk_advertisment primary key (id))
;

create table advertisment_statistics (
  id                        bigint not null,
  advertisment_id           bigint,
  total_visit               bigint,
  start_time                bigint,
  last_visited              bigint,
  constraint pk_advertisment_statistics primary key (id))
;

create table advertisment_visit (
  id                        bigint not null,
  ip                        varchar(255),
  domain                    varchar(255),
  browser                   varchar(255),
  time                      bigint,
  advertisment_id           bigint,
  constraint pk_advertisment_visit primary key (id))
;

create table agent (
  id                        bigint not null,
  user_id                   bigint,
  template_id               bigint,
  audit_id                  bigint,
  name                      varchar(255),
  info                      varchar(255),
  contact                   varchar(255),
  constraint pk_agent primary key (id))
;

create table audit (
  id                        bigint not null,
  type_id                   bigint,
  status                    integer,
  creator_id                bigint,
  distributon_id            bigint,
  create_time               bigint,
  auditor_id                bigint,
  audit_time                bigint,
  constraint pk_audit primary key (id))
;

create table audit_type (
  id                        bigint not null,
  name                      varchar(255),
  info                      varchar(255),
  constraint pk_audit_type primary key (id))
;

create table confirm_receipt (
  id                        bigint not null,
  confirmer_id              bigint,
  time                      bigint,
  money                     double,
  info                      varchar(255),
  constraint pk_confirm_receipt primary key (id))
;

create table contract (
  id                        bigint not null,
  name                      varchar(255),
  detail                    clob,
  info                      clob,
  contract_type_id          bigint,
  create_time               bigint,
  last_modified             bigint,
  constraint pk_contract primary key (id))
;

create table contract_type (
  id                        bigint not null,
  name                      varchar(255),
  info                      varchar(255),
  constraint pk_contract_type primary key (id))
;

create table course (
  id                        bigint not null,
  name                      varchar(255),
  money                     double,
  start_time                bigint,
  contact                   varchar(255),
  audit_id                  bigint,
  course_type_id            bigint,
  course_class_id           bigint,
  info                      clob,
  detail                    clob,
  edu_id                    bigint,
  edu_rebate_type_id        bigint,
  agent_rebate_type_id      bigint,
  instructor_id             bigint,
  constraint pk_course primary key (id))
;

create table course_class (
  id                        bigint not null,
  name                      varchar(255),
  constraint pk_course_class primary key (id))
;

create table course_distribution (
  id                        bigint not null,
  course_id                 bigint,
  agent_id                  bigint,
  audit_id                  bigint,
  rebate_id                 bigint,
  constraint pk_course_distribution primary key (id))
;

create table course_type (
  id                        bigint not null,
  name                      varchar(255),
  constraint pk_course_type primary key (id))
;

create table domain (
  id                        bigint not null,
  domain                    varchar(255),
  agent_id                  bigint,
  edu_id                    bigint,
  instructor_id             bigint,
  constraint pk_domain primary key (id))
;

create table education_institution (
  id                        bigint not null,
  creator_id                bigint,
  create_time               bigint,
  audit_id                  bigint,
  template_id               bigint,
  name                      varchar(255),
  info                      clob,
  constraint pk_education_institution primary key (id))
;

create table enroll (
  id                        bigint not null,
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
  id                        bigint not null,
  name                      varchar(255),
  info                      varchar(255),
  constraint pk_function primary key (id))
;

create table instructor (
  id                        bigint not null,
  user_id                   bigint,
  template_id               bigint,
  name                      varchar(255),
  job_title                 varchar(255),
  audit_id                  bigint,
  create_time               bigint,
  info                      clob,
  field                     varchar(255),
  constraint pk_instructor primary key (id))
;

create table log_login (
  id                        bigint not null,
  user_id                   bigint,
  time                      bigint,
  log_type                  integer,
  constraint pk_log_login primary key (id))
;

create table log_operation (
  id                        bigint not null,
  user_id                   bigint,
  time                      bigint,
  function_id               bigint,
  constraint pk_log_operation primary key (id))
;

create table message (
  id                        bigint not null,
  name                      varchar(255),
  phone                     varchar(255),
  qq                        varchar(255),
  email                     varchar(255),
  address                   varchar(255),
  time                      bigint,
  title                     clob,
  content                   clob,
  constraint pk_message primary key (id))
;

create table module (
  id                        bigint not null,
  name                      varchar(255),
  info                      clob,
  constraint pk_module primary key (id))
;

create table news (
  id                        bigint not null,
  title                     varchar(255),
  detail                    clob,
  news_type_id              bigint,
  time                      bigint,
  constraint pk_news primary key (id))
;

create table news_type (
  id                        bigint not null,
  name                      varchar(255),
  info                      varchar(255),
  constraint pk_news_type primary key (id))
;

create table project (
  id                        bigint not null,
  name                      varchar(255),
  info                      varchar(255),
  status                    integer,
  constraint pk_project primary key (id))
;

create table rebate (
  id                        bigint not null,
  distribution_id           bigint,
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
  id                        bigint not null,
  ratio_of_total            double,
  ratio_of_per_student      double,
  rebate_id                 bigint,
  constraint pk_rebate_type primary key (id))
;

create table reply (
  id                        bigint not null,
  user_id                   bigint,
  title                     varchar(255),
  content                   clob,
  time                      bigint,
  message_id                bigint,
  constraint pk_reply primary key (id))
;

create table role (
  id                        bigint not null,
  name                      varchar(255),
  info                      varchar(255),
  constraint pk_role primary key (id))
;

create table scholl_fellow (
  id                        bigint not null,
  name                      varchar(255),
  company                   varchar(255),
  position                  varchar(255),
  constraint pk_scholl_fellow primary key (id))
;

create table student (
  id                        bigint not null,
  user_id                   bigint,
  name                      varchar(255),
  company_name              varchar(255),
  position                  varchar(255),
  info                      varchar(255),
  constraint pk_student primary key (id))
;

create table studentwords (
  id                        bigint not null,
  name                      varchar(255),
  company                   varchar(255),
  words                     varchar(255),
  constraint pk_studentwords primary key (id))
;

create table template (
  id                        bigint not null,
  user_id                   bigint,
  edu_id                    bigint,
  instructor_id             bigint,
  agent_id                  bigint,
  index_path                varchar(255),
  template_type_id          bigint,
  constraint pk_template primary key (id))
;

create table template_type (
  id                        bigint not null,
  name                      varchar(255),
  info                      clob,
  last_modified             bigint,
  url                       varchar(255),
  logo                      varchar(255),
  constraint pk_template_type primary key (id))
;

create table user (
  id                        bigint not null,
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
  id                        bigint not null,
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
create sequence advertisment_seq;

create sequence advertisment_statistics_seq;

create sequence advertisment_visit_seq;

create sequence agent_seq;

create sequence audit_seq;

create sequence audit_type_seq;

create sequence confirm_receipt_seq;

create sequence contract_seq;

create sequence contract_type_seq;

create sequence course_seq;

create sequence course_class_seq;

create sequence course_distribution_seq;

create sequence course_type_seq;

create sequence domain_seq;

create sequence education_institution_seq;

create sequence enroll_seq;

create sequence function_seq;

create sequence instructor_seq;

create sequence log_login_seq;

create sequence log_operation_seq;

create sequence message_seq;

create sequence module_seq;

create sequence news_seq;

create sequence news_type_seq;

create sequence project_seq;

create sequence rebate_seq;

create sequence rebate_type_seq;

create sequence reply_seq;

create sequence role_seq;

create sequence scholl_fellow_seq;

create sequence student_seq;

create sequence studentwords_seq;

create sequence template_seq;

create sequence template_type_seq;

create sequence user_seq;

create sequence user_info_seq;

alter table advertisment add constraint fk_advertisment_advertismentSt_1 foreign key (advertisment_statistics_id) references advertisment_statistics (id) on delete restrict on update restrict;
create index ix_advertisment_advertismentSt_1 on advertisment (advertisment_statistics_id);
alter table advertisment_statistics add constraint fk_advertisment_statistics_adv_2 foreign key (advertisment_id) references advertisment (id) on delete restrict on update restrict;
create index ix_advertisment_statistics_adv_2 on advertisment_statistics (advertisment_id);
alter table advertisment_visit add constraint fk_advertisment_visit_advertis_3 foreign key (advertisment_id) references advertisment (id) on delete restrict on update restrict;
create index ix_advertisment_visit_advertis_3 on advertisment_visit (advertisment_id);
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
alter table audit add constraint fk_audit_distributon_9 foreign key (distributon_id) references course_distribution (id) on delete restrict on update restrict;
create index ix_audit_distributon_9 on audit (distributon_id);
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
alter table course add constraint fk_course_courseClass_15 foreign key (course_class_id) references course_class (id) on delete restrict on update restrict;
create index ix_course_courseClass_15 on course (course_class_id);
alter table course add constraint fk_course_edu_16 foreign key (edu_id) references education_institution (id) on delete restrict on update restrict;
create index ix_course_edu_16 on course (edu_id);
alter table course add constraint fk_course_eduRebateType_17 foreign key (edu_rebate_type_id) references rebate_type (id) on delete restrict on update restrict;
create index ix_course_eduRebateType_17 on course (edu_rebate_type_id);
alter table course add constraint fk_course_agentRebateType_18 foreign key (agent_rebate_type_id) references rebate_type (id) on delete restrict on update restrict;
create index ix_course_agentRebateType_18 on course (agent_rebate_type_id);
alter table course add constraint fk_course_instructor_19 foreign key (instructor_id) references instructor (id) on delete restrict on update restrict;
create index ix_course_instructor_19 on course (instructor_id);
alter table course_distribution add constraint fk_course_distribution_course_20 foreign key (course_id) references course (id) on delete restrict on update restrict;
create index ix_course_distribution_course_20 on course_distribution (course_id);
alter table course_distribution add constraint fk_course_distribution_agent_21 foreign key (agent_id) references agent (id) on delete restrict on update restrict;
create index ix_course_distribution_agent_21 on course_distribution (agent_id);
alter table course_distribution add constraint fk_course_distribution_audit_22 foreign key (audit_id) references audit (id) on delete restrict on update restrict;
create index ix_course_distribution_audit_22 on course_distribution (audit_id);
alter table course_distribution add constraint fk_course_distribution_rebate_23 foreign key (rebate_id) references rebate (id) on delete restrict on update restrict;
create index ix_course_distribution_rebate_23 on course_distribution (rebate_id);
alter table domain add constraint fk_domain_agent_24 foreign key (agent_id) references agent (id) on delete restrict on update restrict;
create index ix_domain_agent_24 on domain (agent_id);
alter table domain add constraint fk_domain_edu_25 foreign key (edu_id) references education_institution (id) on delete restrict on update restrict;
create index ix_domain_edu_25 on domain (edu_id);
alter table domain add constraint fk_domain_instructor_26 foreign key (instructor_id) references instructor (id) on delete restrict on update restrict;
create index ix_domain_instructor_26 on domain (instructor_id);
alter table education_institution add constraint fk_education_institution_crea_27 foreign key (creator_id) references user (id) on delete restrict on update restrict;
create index ix_education_institution_crea_27 on education_institution (creator_id);
alter table education_institution add constraint fk_education_institution_audi_28 foreign key (audit_id) references audit (id) on delete restrict on update restrict;
create index ix_education_institution_audi_28 on education_institution (audit_id);
alter table education_institution add constraint fk_education_institution_temp_29 foreign key (template_id) references template (id) on delete restrict on update restrict;
create index ix_education_institution_temp_29 on education_institution (template_id);
alter table enroll add constraint fk_enroll_student_30 foreign key (student_id) references student (id) on delete restrict on update restrict;
create index ix_enroll_student_30 on enroll (student_id);
alter table enroll add constraint fk_enroll_course_31 foreign key (course_id) references course (id) on delete restrict on update restrict;
create index ix_enroll_course_31 on enroll (course_id);
alter table enroll add constraint fk_enroll_fromAgent_32 foreign key (from_agent_id) references agent (id) on delete restrict on update restrict;
create index ix_enroll_fromAgent_32 on enroll (from_agent_id);
alter table enroll add constraint fk_enroll_edu_33 foreign key (edu_id) references education_institution (id) on delete restrict on update restrict;
create index ix_enroll_edu_33 on enroll (edu_id);
alter table enroll add constraint fk_enroll_auditOfAgent_34 foreign key (audit_of_agent_id) references audit (id) on delete restrict on update restrict;
create index ix_enroll_auditOfAgent_34 on enroll (audit_of_agent_id);
alter table enroll add constraint fk_enroll_auditOfEdu_35 foreign key (audit_of_edu_id) references audit (id) on delete restrict on update restrict;
create index ix_enroll_auditOfEdu_35 on enroll (audit_of_edu_id);
alter table enroll add constraint fk_enroll_confirmOfEdu_36 foreign key (confirm_of_edu_id) references confirm_receipt (id) on delete restrict on update restrict;
create index ix_enroll_confirmOfEdu_36 on enroll (confirm_of_edu_id);
alter table enroll add constraint fk_enroll_confirmOfPlatform_37 foreign key (confirm_of_platform_id) references confirm_receipt (id) on delete restrict on update restrict;
create index ix_enroll_confirmOfPlatform_37 on enroll (confirm_of_platform_id);
alter table enroll add constraint fk_enroll_confirmOfAgent_38 foreign key (confirm_of_agent_id) references confirm_receipt (id) on delete restrict on update restrict;
create index ix_enroll_confirmOfAgent_38 on enroll (confirm_of_agent_id);
alter table instructor add constraint fk_instructor_user_39 foreign key (user_id) references user (id) on delete restrict on update restrict;
create index ix_instructor_user_39 on instructor (user_id);
alter table instructor add constraint fk_instructor_template_40 foreign key (template_id) references template (id) on delete restrict on update restrict;
create index ix_instructor_template_40 on instructor (template_id);
alter table instructor add constraint fk_instructor_audit_41 foreign key (audit_id) references audit (id) on delete restrict on update restrict;
create index ix_instructor_audit_41 on instructor (audit_id);
alter table log_login add constraint fk_log_login_user_42 foreign key (user_id) references user (id) on delete restrict on update restrict;
create index ix_log_login_user_42 on log_login (user_id);
alter table log_operation add constraint fk_log_operation_user_43 foreign key (user_id) references user (id) on delete restrict on update restrict;
create index ix_log_operation_user_43 on log_operation (user_id);
alter table log_operation add constraint fk_log_operation_function_44 foreign key (function_id) references function (id) on delete restrict on update restrict;
create index ix_log_operation_function_44 on log_operation (function_id);
alter table news add constraint fk_news_newsType_45 foreign key (news_type_id) references news_type (id) on delete restrict on update restrict;
create index ix_news_newsType_45 on news (news_type_id);
alter table rebate add constraint fk_rebate_distribution_46 foreign key (distribution_id) references course_distribution (id) on delete restrict on update restrict;
create index ix_rebate_distribution_46 on rebate (distribution_id);
alter table rebate add constraint fk_rebate_lastReceiptOfEdu_47 foreign key (last_receipt_of_edu_id) references confirm_receipt (id) on delete restrict on update restrict;
create index ix_rebate_lastReceiptOfEdu_47 on rebate (last_receipt_of_edu_id);
alter table rebate add constraint fk_rebate_typeToPlatform_48 foreign key (type_to_platform_id) references rebate_type (id) on delete restrict on update restrict;
create index ix_rebate_typeToPlatform_48 on rebate (type_to_platform_id);
alter table rebate add constraint fk_rebate_lastReceiptOfPlatfo_49 foreign key (last_receipt_of_platform_id) references confirm_receipt (id) on delete restrict on update restrict;
create index ix_rebate_lastReceiptOfPlatfo_49 on rebate (last_receipt_of_platform_id);
alter table rebate add constraint fk_rebate_typeToAgent_50 foreign key (type_to_agent_id) references rebate_type (id) on delete restrict on update restrict;
create index ix_rebate_typeToAgent_50 on rebate (type_to_agent_id);
alter table rebate add constraint fk_rebate_lastReceiptOfAgent_51 foreign key (last_receipt_of_agent_id) references confirm_receipt (id) on delete restrict on update restrict;
create index ix_rebate_lastReceiptOfAgent_51 on rebate (last_receipt_of_agent_id);
alter table rebate_type add constraint fk_rebate_type_rebate_52 foreign key (rebate_id) references rebate (id) on delete restrict on update restrict;
create index ix_rebate_type_rebate_52 on rebate_type (rebate_id);
alter table reply add constraint fk_reply_user_53 foreign key (user_id) references user (id) on delete restrict on update restrict;
create index ix_reply_user_53 on reply (user_id);
alter table reply add constraint fk_reply_message_54 foreign key (message_id) references message (id) on delete restrict on update restrict;
create index ix_reply_message_54 on reply (message_id);
alter table student add constraint fk_student_user_55 foreign key (user_id) references user (id) on delete restrict on update restrict;
create index ix_student_user_55 on student (user_id);
alter table template add constraint fk_template_user_56 foreign key (user_id) references user (id) on delete restrict on update restrict;
create index ix_template_user_56 on template (user_id);
alter table template add constraint fk_template_edu_57 foreign key (edu_id) references education_institution (id) on delete restrict on update restrict;
create index ix_template_edu_57 on template (edu_id);
alter table template add constraint fk_template_instructor_58 foreign key (instructor_id) references instructor (id) on delete restrict on update restrict;
create index ix_template_instructor_58 on template (instructor_id);
alter table template add constraint fk_template_agent_59 foreign key (agent_id) references agent (id) on delete restrict on update restrict;
create index ix_template_agent_59 on template (agent_id);
alter table template add constraint fk_template_templateType_60 foreign key (template_type_id) references template_type (id) on delete restrict on update restrict;
create index ix_template_templateType_60 on template (template_type_id);
alter table user add constraint fk_user_audit_61 foreign key (audit_id) references audit (id) on delete restrict on update restrict;
create index ix_user_audit_61 on user (audit_id);
alter table user add constraint fk_user_basicInfo_62 foreign key (basic_info_id) references user_info (id) on delete restrict on update restrict;
create index ix_user_basicInfo_62 on user (basic_info_id);
alter table user add constraint fk_user_parentAccount_63 foreign key (parent_account_id) references user (id) on delete restrict on update restrict;
create index ix_user_parentAccount_63 on user (parent_account_id);
alter table user add constraint fk_user_instructor_64 foreign key (instructor_id) references instructor (id) on delete restrict on update restrict;
create index ix_user_instructor_64 on user (instructor_id);
alter table user add constraint fk_user_agent_65 foreign key (agent_id) references agent (id) on delete restrict on update restrict;
create index ix_user_agent_65 on user (agent_id);
alter table user add constraint fk_user_student_66 foreign key (student_id) references student (id) on delete restrict on update restrict;
create index ix_user_student_66 on user (student_id);
alter table user_info add constraint fk_user_info_user_67 foreign key (user_id) references user (id) on delete restrict on update restrict;
create index ix_user_info_user_67 on user_info (user_id);



alter table agent_course add constraint fk_agent_course_agent_01 foreign key (agent_id) references agent (id) on delete restrict on update restrict;

alter table agent_course add constraint fk_agent_course_course_02 foreign key (course_id) references course (id) on delete restrict on update restrict;

alter table module_function add constraint fk_module_function_module_01 foreign key (module_id) references module (id) on delete restrict on update restrict;

alter table module_function add constraint fk_module_function_function_02 foreign key (function_id) references function (id) on delete restrict on update restrict;

alter table role_module add constraint fk_role_module_role_01 foreign key (role_id) references role (id) on delete restrict on update restrict;

alter table role_module add constraint fk_role_module_module_02 foreign key (module_id) references module (id) on delete restrict on update restrict;

alter table user_role add constraint fk_user_role_user_01 foreign key (user_id) references user (id) on delete restrict on update restrict;

alter table user_role add constraint fk_user_role_role_02 foreign key (role_id) references role (id) on delete restrict on update restrict;

# --- !Downs

SET REFERENTIAL_INTEGRITY FALSE;

drop table if exists advertisment;

drop table if exists advertisment_statistics;

drop table if exists advertisment_visit;

drop table if exists agent;

drop table if exists agent_course;

drop table if exists audit;

drop table if exists audit_type;

drop table if exists confirm_receipt;

drop table if exists contract;

drop table if exists contract_type;

drop table if exists course;

drop table if exists course_class;

drop table if exists course_distribution;

drop table if exists course_type;

drop table if exists domain;

drop table if exists education_institution;

drop table if exists enroll;

drop table if exists function;

drop table if exists module_function;

drop table if exists instructor;

drop table if exists log_login;

drop table if exists log_operation;

drop table if exists message;

drop table if exists module;

drop table if exists role_module;

drop table if exists news;

drop table if exists news_type;

drop table if exists project;

drop table if exists rebate;

drop table if exists rebate_type;

drop table if exists reply;

drop table if exists role;

drop table if exists user_role;

drop table if exists scholl_fellow;

drop table if exists student;

drop table if exists studentwords;

drop table if exists template;

drop table if exists template_type;

drop table if exists user;

drop table if exists user_info;

SET REFERENTIAL_INTEGRITY TRUE;

drop sequence if exists advertisment_seq;

drop sequence if exists advertisment_statistics_seq;

drop sequence if exists advertisment_visit_seq;

drop sequence if exists agent_seq;

drop sequence if exists audit_seq;

drop sequence if exists audit_type_seq;

drop sequence if exists confirm_receipt_seq;

drop sequence if exists contract_seq;

drop sequence if exists contract_type_seq;

drop sequence if exists course_seq;

drop sequence if exists course_class_seq;

drop sequence if exists course_distribution_seq;

drop sequence if exists course_type_seq;

drop sequence if exists domain_seq;

drop sequence if exists education_institution_seq;

drop sequence if exists enroll_seq;

drop sequence if exists function_seq;

drop sequence if exists instructor_seq;

drop sequence if exists log_login_seq;

drop sequence if exists log_operation_seq;

drop sequence if exists message_seq;

drop sequence if exists module_seq;

drop sequence if exists news_seq;

drop sequence if exists news_type_seq;

drop sequence if exists project_seq;

drop sequence if exists rebate_seq;

drop sequence if exists rebate_type_seq;

drop sequence if exists reply_seq;

drop sequence if exists role_seq;

drop sequence if exists scholl_fellow_seq;

drop sequence if exists student_seq;

drop sequence if exists studentwords_seq;

drop sequence if exists template_seq;

drop sequence if exists template_type_seq;

drop sequence if exists user_seq;

drop sequence if exists user_info_seq;

