create table point (
                        id bigint not null auto_increment,
                        member_id bigint,
                        point bigint,
                        point_type varchar(255),
                        point_type_id bigint,
                        created_date datetime(6) not null,
                        last_modified_date datetime(6) not null,
                        primary key (id)
) engine=InnoDB;

alter table point
    add constraint FKanbfkpjymwg49terfdyb4q262e5w3qaa
        foreign key (member_id)
            references member (id);
