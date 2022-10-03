create table pay_history (
                             id bigint not null auto_increment,
                             created_date datetime(6) not null,
                             last_modified_date datetime(6) not null,
                             imp_uid varchar(255) not null,
                             merchat_uid varchar(255) not null,
                             name varchar(255) not null,
                             pay_date datetime(6),
                             pg_provider varchar(255) not null,
                             pg_tid varchar(255) not null,
                             price integer not null,
                             use_point integer not null,
                             member_id bigint,
                             product_id bigint,
                             to_member_id bigint,
                             primary key (id)
) engine=InnoDB;

alter table gift_box
    add constraint FK6frca0djmy2sh3765h2dd5dr9
        foreign key (payhistory_id)
            references pay_history (id);

alter table pay_history
    add constraint FKm72oa9a0q7753svnffeumeh9x
        foreign key (member_id)
            references member (id);

alter table pay_history
    add constraint FKlty55vy6nj5jrmfdibjofptp9
        foreign key (product_id)
            references product (id);

alter table pay_history
    add constraint FKsr1fhughrtfm6jtgarl701d2c
        foreign key (to_member_id)
            references member (id);