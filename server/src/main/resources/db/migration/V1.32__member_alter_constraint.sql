DO $$ 
DECLARE
    con_name text;
BEGIN
    SELECT conname
    INTO con_name
    FROM pg_constraint
    WHERE conrelid = 'public.comment_tb'::regclass  -- 테이블 이름
      AND confrelid = 'public.member_tb'::regclass; -- 참조하는 테이블 이름
      
    IF con_name IS NOT NULL THEN
        EXECUTE 'ALTER TABLE public.comment_tb DROP CONSTRAINT ' || con_name;
    END IF;
END $$;

ALTER TABLE comment_tb
ADD CONSTRAINT fk_comment_member FOREIGN KEY (member_id)
REFERENCES member_tb (member_id)
ON DELETE CASCADE
ON UPDATE CASCADE;

DO $$ 
DECLARE
    con_name text;
BEGIN
    SELECT conname
    INTO con_name
    FROM pg_constraint
    WHERE conrelid = 'public.setting_tb'::regclass  -- 테이블 이름
      AND confrelid = 'public.member_tb'::regclass; -- 참조하는 테이블 이름
      
    IF con_name IS NOT NULL THEN
        EXECUTE 'ALTER TABLE public.setting_tb DROP CONSTRAINT ' || con_name;
    END IF;
END $$;

ALTER TABLE setting_tb
ADD CONSTRAINT fk_setting_member FOREIGN KEY (member_id)
REFERENCES member_tb (member_id)
ON DELETE CASCADE
ON UPDATE CASCADE;

DO $$ 
DECLARE
    con_name text;
BEGIN
    SELECT conname
    INTO con_name
    FROM pg_constraint
    WHERE conrelid = 'public.inquiry_tb'::regclass  -- 테이블 이름
      AND confrelid = 'public.member_tb'::regclass; -- 참조하는 테이블 이름
      
    IF con_name IS NOT NULL THEN
        EXECUTE 'ALTER TABLE public.inquiry_tb DROP CONSTRAINT ' || con_name;
    END IF;
END $$;

ALTER TABLE inquiry_tb
ADD CONSTRAINT fk_inquiry_member FOREIGN KEY (member_id)
REFERENCES member_tb (member_id)
ON DELETE CASCADE
ON UPDATE CASCADE;

DO $$ 
DECLARE
    con_name text;
BEGIN
    SELECT conname
    INTO con_name
    FROM pg_constraint
    WHERE conrelid = 'public.notification_tb'::regclass  -- 테이블 이름
      AND confrelid = 'public.member_tb'::regclass; -- 참조하는 테이블 이름
      
    IF con_name IS NOT NULL THEN
        EXECUTE 'ALTER TABLE public.notification_tb DROP CONSTRAINT ' || con_name;
    END IF;
END $$;

ALTER TABLE notification_tb
ADD CONSTRAINT fk_notification_member FOREIGN KEY (receiver_id)
REFERENCES member_tb (member_id)
ON DELETE CASCADE
ON UPDATE CASCADE;