create table notice (
                        id bigint not null auto_increment,
                        created_date datetime(6) not null,
                        last_modified_date datetime(6) not null,
                        is_view bit,
                        message varchar(255),
                        notice_type integer,
                        notice_type_id bigint,
                        from_member_id bigint,
                        to_member_id bigint,
                        primary key (id)
) engine=InnoDB;

alter table notice
    add constraint FKanbfkpjymwg49fdyb4q62e5w3
        foreign key (from_member_id)
            references member (id);

alter table notice
    add constraint FK475wfcm95si0x8rrt6a7ard72
        foreign key (to_member_id)
            references member (id);