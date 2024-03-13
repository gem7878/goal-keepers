-- 새로운 setting_tb 생성
CREATE TABLE IF NOT EXISTS public.setting_tb
(
    setting_id SERIAL PRIMARY KEY,
    member_id BIGINT NOT NULL,
    commentAlarm BOOLEAN DEFAULT TRUE,
    contentLikeAlarm BOOLEAN DEFAULT TRUE,
    postCheerAlarm BOOLEAN DEFAULT TRUE,
    goalShareAlarm BOOLEAN DEFAULT TRUE,
    FOREIGN KEY (member_id) REFERENCES MEMBER_TB(member_id)
);

-- member 만큼 setting 만들기
INSERT INTO SETTING_TB (member_id)
SELECT DISTINCT m.member_id
FROM member_tb m;