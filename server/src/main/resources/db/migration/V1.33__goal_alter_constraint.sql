DO $$ 
DECLARE
    con_name text;
BEGIN
    SELECT conname
    INTO con_name
    FROM pg_constraint
    WHERE conrelid = 'public.share_tb'::regclass  -- 테이블 이름
      AND confrelid = 'public.goal_tb'::regclass; -- 참조하는 테이블 이름
      
    IF con_name IS NOT NULL THEN
        EXECUTE 'ALTER TABLE public.share_tb DROP CONSTRAINT ' || con_name;
    END IF;
END $$;

ALTER TABLE share_tb
ADD CONSTRAINT fk_share_goal FOREIGN KEY (goal_id)
REFERENCES goal_tb (goal_id)
ON DELETE CASCADE
ON UPDATE CASCADE;

DO $$ 
DECLARE
    con_name text;
BEGIN
    SELECT conname
    INTO con_name
    FROM pg_constraint
    WHERE conrelid = 'public.post_tb'::regclass  -- 테이블 이름
      AND confrelid = 'public.goal_tb'::regclass; -- 참조하는 테이블 이름
      
    IF con_name IS NOT NULL THEN
        EXECUTE 'ALTER TABLE public.post_tb DROP CONSTRAINT ' || con_name;
    END IF;
END $$;

ALTER TABLE post_tb
ADD CONSTRAINT fk_post_goal FOREIGN KEY (goal_id)
REFERENCES goal_tb (goal_id)
ON DELETE CASCADE
ON UPDATE CASCADE;