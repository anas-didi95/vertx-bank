--liquibase formatted sql

--changeset anas:1.0.0-1
CREATE SCHEMA msbanksvc AUTHORIZATION postgres;
--rollback drop schema msbanksvc;

--changeset anas:1.0.0-2
create table msbanksvc.t_cust (
 id uuid not null default gen_random_uuid(),
 nm varchar(200) not null,
 created_dt timestamp with time zone not null,
 created_by varchar(50) not null,
 updated_dt timestamp with time zone,
 updated_by varchar(50),
 ver integer not null
);
alter table msbanksvc.t_cust add constraint pk_cust primary key (id);
--rollback drop table if exists msbanksvc.t_cust;
