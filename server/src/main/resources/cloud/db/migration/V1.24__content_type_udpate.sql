ALTER TABLE content_tb
ALTER COLUMN content TYPE varchar(60);

ALTER TABLE content_tb
ALTER COLUMN goal_id SET NOT NULL;

ALTER TABLE content_tb
ALTER COLUMN post_id SET NOT NULL;