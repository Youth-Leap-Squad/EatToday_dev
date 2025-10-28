-- 0) 사전 점검 + 트랜잭션/백업
-- 꼭 전체 백업 또는 스냅샷 후 진행하세요.
START TRANSACTION;

-- 1) 바꿀 대상 미리 확인 (대상 판정은 UPPER/TRIM/CAST 기준으로)
SELECT board_no, confirmed_yn
FROM food_post
WHERE confirmed_yn IS NULL
   OR UPPER(TRIM(CAST(confirmed_yn AS CHAR))) NOT IN ('T','F')
    FOR UPDATE;  -- 잠글 행만 잠금 (InnoDB 필요)

-- 2) 표준화 업데이트
UPDATE food_post
SET confirmed_yn = CASE
                       WHEN UPPER(TRIM(CAST(confirmed_yn AS CHAR))) IN ('T','Y','1') THEN 'T'
                       ELSE 'F'
    END
WHERE confirmed_yn IS NULL
   OR UPPER(TRIM(CAST(confirmed_yn AS CHAR))) NOT IN ('T','F');  -- ← 대상 판정 일치

-- 3) 결과 확인
SELECT board_no, confirmed_yn
FROM food_post
ORDER BY board_no DESC;

-- 4) 이상 없으면 커밋 (문제시 ROLLBACK)
COMMIT;

-- 5) 스키마 고정 (버전 호환 주의)
ALTER TABLE food_post
    MODIFY COLUMN confirmed_yn CHAR(1) NOT NULL,
    ADD CONSTRAINT chk_food_post_confirmed_yn CHECK (confirmed_yn IN ('T','F'));





-- 프로필 사진 삽입 관련 sql 문들  --------------------

-- 기본 프사가 없으면 기본프사를 넣어준다 자동으로
UPDATE member
SET profile_image_url = 'http://localhost:5173/images/user_profile/basic_profile.jpg'
WHERE profile_image_url IS NULL;


-- 더미데이터들 프로필 사진 넣어주는 jpg (프론트에 있는 파일)
-- (member_no 기준으로 순차 할당)
UPDATE member m1
    INNER JOIN (
        SELECT
            member_no,
            ROW_NUMBER() OVER (ORDER BY member_no) as rn
        FROM member
    ) m2 ON m1.member_no = m2.member_no
SET m1.profile_image_url = CONCAT('http://localhost:5173/images/user_profile/member_', (m2.rn + 1), '.jpg')
WHERE m2.rn <= 27;  -- member_2부터 member_28까지 총 27개


UPDATE member m1
    INNER JOIN (
        SELECT
            member_no,
            ROW_NUMBER() OVER (ORDER BY member_no) as rn
        FROM member
    ) m2 ON m1.member_no = m2.member_no
SET m1.profile_image_url = CONCAT('http://localhost:5173/images/user_profile/member_', (m2.rn + 1), '.jpg')
WHERE m2.rn <= 27;  -- member_2부터 member_28까지 총 27개 (모든 회원 덮어쓰기)