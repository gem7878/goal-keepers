CREATE TABLE IF NOT EXISTS inquiry_tb
(
  inquiry_id bigserial not null,
  content TEXT,
  created_at timestamp,
  is_answered boolean default false,
  title varchar(50),
  member_id bigint,
  primary key (inquiry_id)
);

CREATE TABLE IF NOT EXISTS answer_tb
(
  answer_id bigserial not null,
  content TEXT,
  created_at timestamp,
  inquiry_id bigint,
  admin_id bigint,
  primary key (answer_id)
);

CREATE TABLE IF NOT EXISTS faq_tb
(
  faq_id  bigserial             PRIMARY KEY,
  title   character varying(50) NOT NULL,
  content text                  NOT NULL
);

alter table if exists answer_tb
  add constraint answer_inquiry_uq unique (inquiry_id);

alter table if exists answer_tb
  add constraint answer_inquiry_fk
  foreign key (inquiry_id)
  references inquiry_tb;

alter table if exists answer_tb
  add constraint answer_member_fk
  foreign key (admin_id)
  references member_tb;

alter table if exists inquiry_tb
  add constraint inquiry_member_fk
  foreign key (member_id)
  references member_tb;