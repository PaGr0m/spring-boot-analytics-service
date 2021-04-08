drop table if exists template CASCADE;
drop table if exists template_recipients CASCADE;
drop table if exists template_validation CASCADE;

create table template
(
    template_id varchar(255) not null,
    template    varchar(255) not null,
    primary key (template_id)
);
create table template_recipients
(
    template_template_id varchar(255) not null,
    recipients           varchar(255) not null
);
create table template_validation
(
    template_template_id varchar(255) not null,
    validation           varchar(255),
    validation_key       varchar(255) not null,
    primary key (template_template_id, validation_key)
);
alter table template_recipients
    add constraint FKrfb44ummv3hkq5n9h9pgd85ki foreign key (template_template_id) references template;
alter table template_validation
    add constraint FKdgpifyff79ju0bv9vx3r9bi1u foreign key (template_template_id) references template;
