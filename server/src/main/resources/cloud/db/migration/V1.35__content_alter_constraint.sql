DO $$ 
DECLARE
    con_name text;
BEGIN
    SELECT conname
    INTO con_name
    FROM pg_constraint
    WHERE conrelid = 'public.like_tb'::regclass  -- 테이블 이름
      AND confrelid = 'public.content_tb'::regclass; -- 참조하는 테이블 이름
      
    IF con_name IS NOT NULL THEN
        EXECUTE 'ALTER TABLE public.like_tb DROP CONSTRAINT ' || con_name;
    END IF;
END $$;

ALTER TABLE like_tb
ADD CONSTRAINT fk_like_content FOREIGN KEY (content_id)
REFERENCES content_tb (content_id)
ON DELETE CASCADE
ON UPDATE CASCADE;