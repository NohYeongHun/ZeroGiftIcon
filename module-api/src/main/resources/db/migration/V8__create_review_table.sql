create table review (
                        id bigint not null auto_increment,
                        created_date datetime(6) not null,
                        last_modified_date datetime(6) not null,
                        description longtext not null,
                        rank integer not null,
                        member_id bigint,
                        product_id bigint,
                        primary key (id)
) engine=InnoDB;

alter table review
    add constraint FKk0ccx5i4ci2wd70vegug074w1
        foreign key (member_id)
            references member (id);

alter table review
    add constraint FKiyof1sindb9qiqr9o8npj8klt
        foreign key (product_id)
            references product (id);