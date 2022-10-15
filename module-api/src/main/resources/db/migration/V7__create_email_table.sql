create table email_auth (
                            id bigint not null auto_increment,
                            created_date datetime(6) not null,
                            last_modified_date datetime(6) not null,
                            auth_token varchar(255),
                            email varchar(255),
                            expire_date datetime(6),
                            expired bit,
                            primary key (id)
) engine=InnoDB;

create table email_message (
                               id bigint not null auto_increment,
                               created_date datetime(6) not null,
                               last_modified_date datetime(6) not null,
                               email varchar(255) not null,
                               message longtext not null,
                               send bit not null,
                               status varchar(255),
                               primary key (id)
) engine=InnoDB;