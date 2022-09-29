create table review (
    id                  bigint generated by default as identity,
    created_date        timestamp not null,
    last_modified_date   timestamp not null,
    create_date         timestamp,
    description         clob not null,
    rank                integer not null,
    update_date         timestamp,
    member_id           bigint,
    product_id          bigint,
    primary key (id)
);

alter table review
    add constraint FKk0ccx5i4ci2wd70vegug074w1
        foreign key (member_id)
            references member;

alter table review
    add constraint FKiyof1sindb9qiqr9o8npj8klt
        foreign key (product_id)
            references product;