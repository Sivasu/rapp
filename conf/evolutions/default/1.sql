# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table candidate (
  id                        bigint auto_increment not null,
  candidate_id              varchar(255),
  name                      varchar(255),
  source                    varchar(255),
  experience                float,
  company                   varchar(255),
  review_id                 bigint,
  constraint pk_candidate primary key (id))
;

create table project_history (
  id                        bigint auto_increment not null,
  reviewer_id               bigint,
  project_name              varchar(255),
  start_date                datetime,
  end_date                  datetime,
  constraint pk_project_history primary key (id))
;

create table review (
  id                        bigint auto_increment not null,
  reviewer_id               bigint,
  candidate_id              bigint,
  technology                varchar(255),
  start_date                datetime,
  end_date                  datetime,
  result                    tinyint(1) default 0,
  constraint pk_review primary key (id))
;

create table reviewer (
  id                        bigint auto_increment not null,
  consultant_id             varchar(255),
  name                      varchar(255),
  constraint pk_reviewer primary key (id))
;

create table staging_candidate_record (
  id                        bigint auto_increment not null,
  full_name                 varchar(255),
  person_id                 varchar(255),
  source                    varchar(255),
  step                      varchar(255),
  years_of_experience       varchar(255),
  ta_in_review_date         varchar(255),
  reviewer_name1            varchar(255),
  recommendation1           varchar(255),
  date_received1            varchar(255),
  date_evaluated1           varchar(255),
  language1                 varchar(255),
  reviewer_name2            varchar(255),
  recommendation2           varchar(255),
  date_received2            varchar(255),
  date_evaluated2           varchar(255),
  language2                 varchar(255),
  tags                      varchar(255),
  processed                 tinyint(1) default 0,
  constraint pk_staging_candidate_record primary key (id))
;

alter table candidate add constraint fk_candidate_review_1 foreign key (review_id) references review (id) on delete restrict on update restrict;
create index ix_candidate_review_1 on candidate (review_id);
alter table project_history add constraint fk_project_history_reviewer_2 foreign key (reviewer_id) references reviewer (id) on delete restrict on update restrict;
create index ix_project_history_reviewer_2 on project_history (reviewer_id);
alter table review add constraint fk_review_reviewer_3 foreign key (reviewer_id) references reviewer (id) on delete restrict on update restrict;
create index ix_review_reviewer_3 on review (reviewer_id);
alter table review add constraint fk_review_candidate_4 foreign key (candidate_id) references candidate (id) on delete restrict on update restrict;
create index ix_review_candidate_4 on review (candidate_id);



# --- !Downs

SET FOREIGN_KEY_CHECKS=0;

drop table candidate;

drop table project_history;

drop table review;

drop table reviewer;

drop table staging_candidate_record;

SET FOREIGN_KEY_CHECKS=1;

