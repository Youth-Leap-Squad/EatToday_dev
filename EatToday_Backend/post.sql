-- alcohol
SELECT
    alcohol_explain,
    alcohol_picture
FROM
    alcohol;

-- bookmark
-- 특정 회원의 즐겨찾기 목록 조회
SELECT
    b.favorites,
    m.member_id,
    p.board_title,
    p.food_picture
FROM
    bookmark AS b
        INNER JOIN
    -- 즐겨찾기된 게시글 정보를 위해 food_post 테이블과 연결
        food_post AS p ON b.board_no = p.board_no
        INNER JOIN
    -- 즐겨찾기한 회원 정보를 위해 member 테이블과 연결
        member AS m ON b.member_no = m.member_no
WHERE
    b.member_no = 2; -- 조회할 회원 번호

-- food_post_likes
-- 특정 게시글의 반응 종류 및 개수 조회
SELECT
    likes_type,
    COUNT(*) AS reaction_count
FROM
    food_post_likes
WHERE
    board_no = 3 -- 집계할 게시글 번호
GROUP BY
    likes_type;

-- food_comment
-- 특정 게시글의 모든 댓글 조회
SELECT
    fc.food_comment_no,
    m.member_id, -- member 테이블에서 작성자 id 가져오기
    fc.fc_content,
    fc.fc_date
FROM
    food_comment fc
        INNER JOIN
    member m ON fc.member_no = m.member_no
WHERE
    fc.board_no = 2 -- 댓글을 볼 게시글 번호
ORDER BY
    fc.food_comment_no ASC;


-- 특정 회원이 작성한 모든 댓글 조회 (최신순)
-- 특정 회원이 작성한 모든 댓글 조회 (회원 ID 포함, 최신순)
SELECT
    m.member_id,             -- 회원 아이디 (추가)
    p.board_title,           -- 댓글이 달린 게시글 제목
    fc.fc_content,           -- 내가 쓴 댓글 내용
    fc.fc_date               -- 댓글 작성일
FROM
    food_comment AS fc
        INNER JOIN
    -- 회원 정보를 사용하기 위해 member 테이블과 연결
        member AS m ON fc.member_no = m.member_no
        INNER JOIN
    -- 게시글 제목을 가져오기 위해 food_post 테이블과 연결
        food_post AS p ON fc.board_no = p.board_no
WHERE
    fc.member_no = 3 -- 조회할 회원의 고유 번호(member_no)
ORDER BY
    fc.food_comment_no DESC;



-- food_post
-- 게시글 목록 기본 조회
-- 게시글 목록 기본 조회 (각 반응 개수 포함)
SELECT
    fp.board_no,
    fp.board_title,
    fp.food_picture,
    m.member_id,      -- 작성자 id
    fp.board_date,
    fp.board_seq,
    fp.likes_no_1,    -- '술술 들어가요' 반응 개수
    fp.likes_no_2,    -- '참신해요' 반응 개수
    fp.likes_no_3,    -- '맛없어요' 반응 개수
    fp.likes_no_4     -- '궁금해요' 반응 개수
FROM
    food_post AS fp
        INNER JOIN
    -- 작성자 id를 가져오기 위해 member 테이블과 연결
        member AS m ON fp.member_no = m.member_no
WHERE
    fp.confirmed_yn = TRUE -- 관리자가 승인한 게시글만 (MySQL에서는 TRUE 대신 'T' 사용)
ORDER BY
    fp.board_no DESC; -- board_no가 클수록 최신글이므로 내림차순 정렬


-- 인기 게시글 목록 조회
SELECT
    fp.board_no,
    fp.board_title,
    fp.food_picture,
    m.member_id,
    (fp.likes_no_1 + fp.likes_no_2 + fp.likes_no_3 + fp.likes_no_4) AS total_likes
FROM
    food_post AS fp
        INNER JOIN
    member AS m ON fp.member_no = m.member_no
WHERE
    fp.confirmed_yn = TRUE
ORDER BY
    total_likes DESC, -- 1. 총 반응 수가 높은 순으로 정렬
    fp.board_no DESC  -- 2. 반응 수가 같다면 최신순으로 정렬
    LIMIT 10;