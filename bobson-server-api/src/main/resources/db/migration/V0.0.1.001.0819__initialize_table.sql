/**
  create table
 */
create table bobson_events
(
    id                   varchar(41)             not null primary key,
    event_name           varchar(100)            not null,
    event_data           json                    not null,
    status               varchar(20)             not null,
    created_module_name  varchar(100)            not null,
    created_ip_address   varchar(100)            not null,
    executed_module_name varchar(100),
    executed_ip_address  varchar(100),
    message              text,
    version              bigint    default 0     not null,
    created_date         timestamp default now() not null,
    last_modified_date   timestamp
);