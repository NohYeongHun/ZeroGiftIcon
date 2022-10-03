create table likes (
                       id bigint not null auto_increment,
                       reg_date datetime(6),
                       member_id bigint,
                       product_id bigint,
                       primary key (id)
) engine=InnoDB;

alter table likes
    add constraint FKa4vkf1skcfu5r6o5gfb5jf295
        foreign key (member_id)
            references member (id);

alter table likes
    add constraint FKbt06f6qrlpr663ng2qlb0gvu8
        foreign key (product_id)
            references product (id);