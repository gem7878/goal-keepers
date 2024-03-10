ALTER TABLE member_tb
ALTER COLUMN role TYPE varchar(10);

ALTER TABLE member_tb
ALTER COLUMN sns TYPE varchar(6);

ALTER TABLE member_tb
ALTER COLUMN role TYPE varchar(10);

ALTER TABLE goal_tb
ALTER COLUMN title TYPE varchar(18);

ALTER TABLE goal_tb
ALTER COLUMN description TYPE varchar(60);

ALTER TABLE goal_tb
ALTER COLUMN image_url TYPE varchar(50);

ALTER TABLE goal_tb
ALTER COLUMN completed SET NOT NULL;
