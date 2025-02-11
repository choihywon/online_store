-- auto-generated definition
create table deliveries_info
(
    deliveries_info_seq bigint auto_increment
        primary key,
    user_seq            bigint                                not null,
    dest_name           varchar(100)                          not null,
    road_address        varchar(255)                          not null,
    address_detail      varchar(255)                          not null,
    zipcode             varchar(10)                           not null,
    etc                 varchar(255)                          null,
    created_at          timestamp default current_timestamp() not null,
    last_modified_at    timestamp default current_timestamp() not null on update current_timestamp(),
    address_name        varchar(100)                          not null,
    detail_addr         varchar(255)                          not null,
    street_addr         varchar(255)                          not null,
    constraint deliveries_info_ibfk_1
        foreign key (user_seq) references users (user_seq)
);

create index user_seq
    on deliveries_info (user_seq);

