create table member (
                        id bigint not null auto_increment,
                        created_date datetime(6) not null,
                        last_modified_date datetime(6) not null,
                        auth_id varchar(255),
                        auth_type varchar(255),
                        email varchar(255),
                        nickname varchar(255),
                        password varchar(255),
                        point integer,
                        profile_image_url varchar(255),
                        role varchar(255),
                        status varchar(255),
                        primary key (id)
) engine=InnoDB;

alter table member
    add constraint UK_slxrx0npbedl2g36lara0ere unique (auth_id);

alter table member
    add constraint UK_mbmcqelty0fbrvxp1q58dn57t unique (email);