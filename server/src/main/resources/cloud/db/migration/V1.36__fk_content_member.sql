DO $$ 
DECLARE
    con_name text;
BEGIN
    SELECT conname
    INTO con_name
    FROM pg_constraint
    WHERE conrelid = 'public.content_tb'::regclass  -- 테이블 이름
      AND confrelid = 'public.member_tb'::regclass; -- 참조하는 테이블 이름
      
    IF con_name IS NOT NULL THEN
        EXECUTE 'ALTER TABLE public.content_tb DROP CONSTRAINT ' || con_name;
    END IF;
END $$;

ALTER TABLE content_tb
ADD CONSTRAINT fk_content_member FOREIGN KEY (member_id)
REFERENCES member_tb (member_id)
ON DELETE CASCADE
ON UPDATE CASCADE;