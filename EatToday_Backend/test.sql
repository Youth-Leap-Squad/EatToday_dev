

-- 회원 부분 select 문 ------------------------------------------------------

-- 가입 시 입력한 전화번호를 통해 아이디를 확인.
SELECT m.member_name,
       m.member_id
FROM member m
WHERE member_phone = '010-9999-9999';  -- 010-9999-9999 부분 -> 나중에 바인딩


SELECT * FROM member;


-- 등록된 전화번호로 비밀번호 재설정 안내.


SELECT m.member_name,
       m.member_phone
FROM member m
WHERE member_phone = '010-9999-9999';  -- 010-9999-9999 부분 -> 나중에 바인딩


-- 본인의 프로필, 활동 기록 등을 확인할 수 있다.

SELECT m.member_name,
       m.member_birth,
       m.member_phone,
       m.member_level,
       CASE
           WHEN m.member_active = 1 THEN '활동회원'
           WHEN m.member_active = 0 THEN '비활성 회원'
           END member_status                            -- 1일때는 활동회원 0일때는 비활성 회원 출력
FROM member m
WHERE member_no =4   ;              -- 나중에 바인딩 부분 member_no


-- 받은 포인트에 따른 등급을 확인할 수 있다.

SELECT
    m.member_name,
    m.member_level,
    r.role_name,
    CASE
        WHEN m.member_level IS NULL THEN '관리자'
        WHEN m.member_level >= 500 THEN '골드'
        WHEN m.member_level >= 300 THEN '실버'
        ELSE '브론즈'
        END AS member_level_label
FROM member m
         LEFT JOIN role r ON r.role_no = m.member_role_no
WHERE m.member_no = 4;



-- 신고 내용 확인

SELECT
    m1.member_no   AS reporter_no,
    m1.member_name AS repoter_name,
    m2.member_no   AS target_no,
    m2.member_name AS taget_name,
    r.report_title,
    r.report_content
FROM report r
         JOIN member m1 ON r.member_no = m1.member_no  -- 신고자
         JOIN member m2 ON r.member_no2 = m2.member_no  -- 피신고자
         LEFT JOIN report_history rh
                   ON rh.report_no = r.report_no
                       AND rh.member_no = r.member_no2



-- 관리자가 별도 권한으로 로그인할 수 있다.


