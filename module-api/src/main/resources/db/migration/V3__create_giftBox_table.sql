create table gift_box (
                          id bigint not null auto_increment,
                          created_date datetime(6) not null,
                          last_modified_date datetime(6) not null,
                          answer bit not null,
                          barcode_url varchar(255),
                          code varchar(255) not null,
                          expire_date date not null,
                          is_use bit not null,
                          review bit not null,
                          payhistory_id bigint,
                          product_id bigint,
                          recipient_member_id bigint,
                          send_member_id bigint,
                          primary key (id)
) engine=InnoDB;

create table gift_message (
                              id bigint not null auto_increment,
                              created_date datetime(6) not null,
                              last_modified_date datetime(6) not null,
                              message varchar(255) not null,
                              from_member_id bigint,
                              gift_box_id bigint,
                              product_id bigint,
                              to_member_id bigint,
                              primary key (id)
) engine=InnoDB;


alter table gift_box
    add constraint FK3paiq3pymvjogvbi0q1emykuh
        foreign key (product_id)
            references product (id);

alter table gift_box
    add constraint FKgc1wf8gc6d0rgdwirili04e7x
        foreign key (recipient_member_id)
            references member (id);

alter table gift_box
    add constraint FKk22l3a39lj52ri5788t9f4dkq
        foreign key (send_member_id)
            references member (id);

alter table gift_message
    add constraint FKgw8secjg40uo2t02cds6hsoxa
        foreign key (from_member_id)
            references member (id);

alter table gift_message
    add constraint FKkut3dkxncb2oau5xe0mp19vma
        foreign key (gift_box_id)
            references gift_box (id);

alter table gift_message
    add constraint FK3q9udoa4pde54f1vyya17rnrn
        foreign key (product_id)
            references product (id);

alter table gift_message
    add constraint FKlg8mwba3gckjfa9727a9s04e9
        foreign key (to_member_id)
            references member (id);