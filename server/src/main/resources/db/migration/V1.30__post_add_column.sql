alter table if exists post_tb
       add column member_id bigint;

alter table if exists post_tb
       add constraint FKpostmember
       foreign key (member_id)
       references member_tb