-- updated_at을 created_at으로 복사
update content_tb
set created_at = updated_at
where created_at is null;

update content_tb
set updated_at = null;

-- updated_at 컬럼 지우기
ALTER TABLE content_tb
DROP COLUMN updated_at;

-- like_cnt 컬럼 지우기
ALTER TABLE post_tb
DROP COLUMN like_cnt;