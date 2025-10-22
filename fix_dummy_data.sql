-- 더미데이터 비밀번호 암호화 및 member_level 초기화 스크립트

-- 1. 비밀번호를 BCrypt로 암호화 (모든 비밀번호를 "password123!"로 통일)
-- BCrypt 해시: $2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDi

UPDATE member 
SET member_pw = '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDi',
    member_level = COALESCE(member_level, 0)
WHERE member_pw NOT LIKE '$2a$%';

-- 2. member_level이 null인 경우 0으로 설정
UPDATE member 
SET member_level = 0 
WHERE member_level IS NULL;

-- 3. 확인용 쿼리
SELECT member_no, member_email, member_pw, member_level 
FROM member 
WHERE member_email IN (
    'high.holic@example.com',
    'sake.trip@example.com', 
    'wine.note@example.com',
    'beer.runner@example.com',
    'soju.writer@example.com'
);



