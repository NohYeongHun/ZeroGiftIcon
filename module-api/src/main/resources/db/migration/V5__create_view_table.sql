create table view_history (
                              id bigint not null auto_increment,
                              member_id bigint,
                              product_id bigint,
                              primary key (id)
) engine=InnoDB;

alter table view_history
    add constraint FKp2gqpr2jayc2s4jihoq552wwt
        foreign key (member_id)
            references member (id);

alter table view_history
    add constraint FK9oc442r80sauniv1f6sr494l
        foreign key (product_id)
            references product (id);