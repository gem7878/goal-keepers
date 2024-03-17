ALTER TABLE post_tb DROP COLUMN member_id;

ALTER TABLE share_tb
ADD CONSTRAINT unique_member_goal UNIQUE (member_id, goal_id);
