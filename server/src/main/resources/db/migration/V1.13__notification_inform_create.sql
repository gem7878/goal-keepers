CREATE TABLE IF NOT EXISTS notification_tb
(
  notification_id bigserial PRIMARY KEY,
  type smallint NOT NULL CHECK (type >= 0 AND type <= 10),
  giver_id bigint,
  receiver_id bigint NOT NULL,
  target_id bigint,
  is_read boolean NOT NULL DEFAULT false,
  message character varying(50),
  comment_id bigint                
);

CREATE TABLE IF NOT EXISTS inform_tb
(
  inform_id bigserial PRIMARY KEY,
  content text NOT NULL,
  created_at timestamp(6) with time zone
);