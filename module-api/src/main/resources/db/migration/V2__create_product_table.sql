create table product (
                         id bigint not null auto_increment,
                         category varchar(255),
                         count integer,
                         created_at datetime(6),
                         description varchar(255),
                         like_count bigint,
                         liked longblob,
                         main_image_url varchar(255),
                         name varchar(255),
                         price integer,
                         status varchar(255),
                         updated_at datetime(6),
                         view_count bigint,
                         member_id bigint,
                         primary key (id)
) engine=InnoDB;

create table product_image (
                               id bigint not null auto_increment,
                               created_at datetime(6),
                               is_main_image bit,
                               updated_at datetime(6),
                               url varchar(255),
                               product_id bigint,
                               primary key (id)
) engine=InnoDB;

alter table product
    add constraint FKnlsnjukov4vm4dv1dw0l438hi
        foreign key (member_id)
            references member (id);

alter table product_image
    add constraint FK6oo0cvcdtb6qmwsga468uuukk
        foreign key (product_id)
            references product (id);
