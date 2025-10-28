-- 프로필 사진 삽입 관련 sql 문들  --------------------

-- 기본 프사가 없으면 기본프사를 넣어준다 자동으로
UPDATE member
SET profile_image_url = 'http://localhost:5173/images/user_profile/basic_profile.jpg'
WHERE profile_image_url IS NULL;



UPDATE member m1
    INNER JOIN (
        SELECT
            member_no,
            ROW_NUMBER() OVER (ORDER BY member_no) as rn
        FROM member
    ) m2 ON m1.member_no = m2.member_no
SET m1.profile_image_url = CONCAT('http://localhost:5173/images/user_profile/member_', (m2.rn + 1), '.jpg')
WHERE m2.rn <= 27;  -- member_2부터 member_28까지 총 27개 (모든 회원 덮어쓰기)