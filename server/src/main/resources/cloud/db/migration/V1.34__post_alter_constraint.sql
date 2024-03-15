DO $$ 
DECLARE
    con_name text;
BEGIN
    SELECT conname
    INTO con_name
    FROM pg_constraint
    WHERE conrelid = 'public.content_tb'::regclass  -- 테이블 이름
      AND confrelid = 'public.post_tb'::regclass; -- 참조하는 테이블 이름
      
    IF con_name IS NOT NULL THEN
        EXECUTE 'ALTER TABLE public.content_tb DROP CONSTRAINT ' || con_name;
    END IF;
END $$;

ALTER TABLE content_tb
ADD CONSTRAINT fk_content_post FOREIGN KEY (post_id)
REFERENCES post_tb (post_id)
ON DELETE CASCADE
ON UPDATE CASCADE;

DO $$ 
DECLARE
    con_name text;
BEGIN
    SELECT conname
    INTO con_name
    FROM pg_constraint
    WHERE conrelid = 'public.comment_tb'::regclass  -- 테이블 이름
      AND confrelid = 'public.post_tb'::regclass; -- 참조하는 테이블 이름
      
    IF con_name IS NOT NULL THEN
        EXECUTE 'ALTER TABLE public.comment_tb DROP CONSTRAINT ' || con_name;
    END IF;
END $$;

ALTER TABLE comment_tb
ADD CONSTRAINT fk_comment_post FOREIGN KEY (post_id)
REFERENCES post_tb (post_id)
ON DELETE CASCADE
ON UPDATE CASCADE;

DO $$ 
DECLARE
    con_name text;
BEGIN
    SELECT conname
    INTO con_name
    FROM pg_constraint
    WHERE conrelid = 'public.cheer_tb'::regclass  -- 테이블 이름
      AND confrelid = 'public.post_tb'::regclass; -- 참조하는 테이블 이름
      
    IF con_name IS NOT NULL THEN
        EXECUTE 'ALTER TABLE public.cheer_tb DROP CONSTRAINT ' || con_name;
    END IF;
END $$;

ALTER TABLE cheer_tb
ADD CONSTRAINT fk_cheer_post FOREIGN KEY (post_id)
REFERENCES post_tb (post_id)
ON DELETE CASCADE
ON UPDATE CASCADE;