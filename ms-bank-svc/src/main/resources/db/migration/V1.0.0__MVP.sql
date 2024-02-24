drop table if exists public.t_cust;
create table public.t_cust (
 id uuid not null default gen_random_uuid(),
 nm varchar(200) not null,
 created_dt timestamp with time zone not null,
 created_by varchar(50) not null,
 updated_dt timestamp with time zone,
 updated_by varchar(50),
 ver integer not null
)
;
alter table public.t_cust add constraint pk_cust primary key (id);
