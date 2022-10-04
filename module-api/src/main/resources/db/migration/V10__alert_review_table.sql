alter table review
    add column giftbox_id bigint;
    alter table review
    add constraint gawejogjwepgjkweopfkjwe
    foreign key (giftbox_id)
    references gift_box (id);