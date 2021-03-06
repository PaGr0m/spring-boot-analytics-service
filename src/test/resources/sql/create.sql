create table if not exists message_template
(
    template_id varchar(255) not null,
    template    varchar(255) not null,
    primary key (template_id)
);
create table if not exists message_template_recipients
(
    message_template_template_id varchar(255) not null,
    recipients                   varchar(255) not null
);
alter table if EXISTS message_template_recipients
    add constraint FKptr0oxm6di5b1kf9bsmrr49gh foreign key (message_template_template_id) references message_template;