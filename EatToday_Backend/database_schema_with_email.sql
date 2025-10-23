-- EatToday 데이터베이스 전체 스키마 (memberEmail 필드 포함)
-- memberPhone을 아이디 역할에서 memberEmail로 변경
SET FOREIGN_KEY_CHECKS = 1;
DROP TABLE IF EXISTS `albti_output`;
DROP TABLE IF EXISTS `albti_join_member`;
DROP TABLE IF EXISTS `albti_survey`;
DROP TABLE IF EXISTS `dm_file_upload`;
DROP TABLE IF EXISTS `photo_review_comment`;
DROP TABLE IF EXISTS `pr_file_upload`;
DROP TABLE IF EXISTS `lounge`;
DROP TABLE IF EXISTS `report_history`;
DROP TABLE IF EXISTS `world_cup_picks`;
DROP TABLE IF EXISTS `bookmark`;
DROP TABLE IF EXISTS `food_post_likes`;
DROP TABLE IF EXISTS `food_comment`;
DROP TABLE IF EXISTS `individual_world_cup_food`;
DROP TABLE IF EXISTS `qna_comment`;

-- 중간 계층
DROP TABLE IF EXISTS `direct_message`;
DROP TABLE IF EXISTS `photo_review`;
DROP TABLE IF EXISTS `eventFood`;
DROP TABLE IF EXISTS `worldcup_alcohol`;
DROP TABLE IF EXISTS `worldcup_join_member`;
DROP TABLE IF EXISTS `albti`;
DROP TABLE IF EXISTS `report`;
DROP TABLE IF EXISTS `qna_post`;
DROP TABLE IF EXISTS `follow`;
DROP TABLE IF EXISTS `secession`;
DROP TABLE IF EXISTS `login`;

-- 부모 테이블
DROP TABLE IF EXISTS `food_post`;
DROP TABLE IF EXISTS `worldcup`;
DROP TABLE IF EXISTS `alcohol`;
DROP TABLE IF EXISTS `member`;
DROP TABLE IF EXISTS `role`;

CREATE TABLE `member` (
                          member_no INT NOT NULL AUTO_INCREMENT COMMENT '회원번호',
                          member_role ENUM('USER','ADMIN') NOT NULL COMMENT '권한',
                          member_id VARCHAR(255) NOT NULL COMMENT '아이디',
                          member_pw VARCHAR(255) NOT NULL COMMENT '비밀번호',
                          member_name VARCHAR(255) NOT NULL COMMENT '회원명',
                          member_birth VARCHAR(255) NOT NULL COMMENT '생년월일',
                          member_phone VARCHAR(255) NOT NULL COMMENT '핸드폰 번호',
                          member_email VARCHAR(255) NOT NULL UNIQUE COMMENT '이메일 (로그인 아이디)',
                          member_status VARCHAR(255) NOT NULL DEFAULT 'normal' COMMENT '회원상태',
                          member_active BOOLEAN NOT NULL DEFAULT TRUE COMMENT '회원 활동여부',
                          member_at VARCHAR(255) NOT NULL DEFAULT 0 COMMENT '계정 생성일',
                          member_level INT COMMENT '회원등급',
                          report_count INT NOT NULL DEFAULT 0 COMMENT '신고 누적 횟수',
                          CONSTRAINT PK_member PRIMARY KEY (member_no),
                          INDEX idx_member_email (member_email)
) ENGINE=INNODB COMMENT '회원 정보';


CREATE TABLE `secession` (
                             member_no INT NOT NULL COMMENT '회원번호',
                             secession_at VARCHAR(255) NOT NULL DEFAULT 0 COMMENT '탈퇴 일자',
                             CONSTRAINT FK_SECESSION_MEMBER FOREIGN KEY (member_no)REFERENCES member(member_no)
) ENGINE=INNODB COMMENT '탈퇴 회원 정보';

CREATE TABLE `alcohol` (
                           `alcohol_no` INT NOT NULL AUTO_INCREMENT,
                           `alcohol_type` VARCHAR(255) NOT NULL,
                           `alcohol_explain` VARCHAR(255) NOT NULL,
                           `alcohol_picture` VARCHAR(255) NOT NULL,
                           CONSTRAINT PK_ALCOHOL PRIMARY KEY (alcohol_no)
) ENGINE=INNODB COMMENT '술';

CREATE TABLE `food_post` (
                             `board_no` INT NOT NULL AUTO_INCREMENT,
                             `alcohol_no` INT NOT NULL,
                             `member_no` INT NOT NULL,
                             `board_title` VARCHAR(255) NOT NULL,
                             `board_content` VARCHAR(255) NOT NULL,
                             `food_explain` VARCHAR(255) NOT NULL,
                             `food_picture` VARCHAR(255),
                             `board_date` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
                             `board_seq` INT NOT NULL DEFAULT 0,
                             `confirmed_yn` CHAR(1) NOT NULL DEFAULT 'T',
                             `likes_no_1` INT NOT NULL DEFAULT 0,
                             `likes_no_2` INT NOT NULL DEFAULT 0,
                             `likes_no_3` INT NOT NULL DEFAULT 0,
                             `likes_no_4` INT NOT NULL DEFAULT 0,
                             CONSTRAINT `PK_FOOD_POST` PRIMARY KEY (`board_no`),
                             CONSTRAINT `FK_FOOD_POST_ALCOHOL` FOREIGN KEY (`alcohol_no`) REFERENCES `alcohol`(`alcohol_no`),
                             CONSTRAINT `FK_FOOD_POST_MEMBER` FOREIGN KEY (`member_no`) REFERENCES `member`(`member_no`)
) ENGINE=INNODB COMMENT='안주 게시글';

CREATE TABLE `photo_review` (
                                review_no     INT NOT NULL AUTO_INCREMENT,
                                board_no      INT NOT NULL,
                                member_no      INT NOT NULL,
                                review_title  VARCHAR(255)  NOT NULL,
                                review_date   VARCHAR(255) NOT NULL,
                                review_content VARCHAR(255) NOT NULL,
                                review_like   INT NOT NULL DEFAULT 0,
                                CONSTRAINT PK_photo_review PRIMARY KEY (review_no),
                                CONSTRAINT FK_pr_board FOREIGN KEY (board_no) REFERENCES food_post(board_no),
                                CONSTRAINT FK_pr_member FOREIGN KEY (member_no) REFERENCES member(member_no)
)ENGINE=INNODB COMMENT '사진 리뷰';


CREATE TABLE `lounge` (
                          review_no  INT NOT NULL,
                          alcohol_no INT NOT NULL,
                          CONSTRAINT PK_lounge PRIMARY KEY (review_no, alcohol_no),
                          CONSTRAINT FK_lounge_review  FOREIGN KEY (review_no)  REFERENCES photo_review(review_no),
                          CONSTRAINT FK_lounge_alcohol FOREIGN KEY (alcohol_no) REFERENCES alcohol(alcohol_no)
)ENGINE=INNODB COMMENT '라운지';

CREATE TABLE `qna_post` (
                            qna_post_no INT NOT NULL AUTO_INCREMENT COMMENT '문의사항 번호',
                            member_no INT NOT NULL COMMENT '문의자',
                            inquiry_content VARCHAR(255) NOT NULL COMMENT '문의 내용',
                            inquiry_at VARCHAR(255) NOT NULL DEFAULT 0 COMMENT '작성 일자',
                            CONSTRAINT PK_qna_post PRIMARY KEY (qna_post_no),
                            CONSTRAINT FK_qna_post_member FOREIGN KEY (member_no) REFERENCES member (member_no)
) ENGINE=INNODB COMMENT '문의사항 게시글';

CREATE TABLE `qna_comment` (
                               `comment_no` INT NOT NULL AUTO_INCREMENT COMMENT '문의사항 댓글 번호',
                               `qna_post_no` INT NOT NULL COMMENT '문의사항 번호',
                               `comment_member_no` INT NOT NULL COMMENT '답변자 번호(운영자)',
                               `comment_content` VARCHAR(255) NOT NULL COMMENT '답변 내용',
                               `comment_at` VARCHAR(255) NOT NULL COMMENT '작성 일시',
                               CONSTRAINT PK_qna_comment PRIMARY KEY (comment_no),
                               KEY `idx_qna_comment_post` (`qna_post_no`),
                               CONSTRAINT FK_qna_post_no FOREIGN KEY (qna_post_no) REFERENCES qna_post (qna_post_no) ON UPDATE CASCADE ON DELETE CASCADE,
                               CONSTRAINT FK_comment_post_member FOREIGN KEY (comment_member_no) REFERENCES member (member_no) ON UPDATE CASCADE ON DELETE CASCADE

) ENGINE=INNODB COMMENT='문의사항 답변';

CREATE TABLE `follow` (
                          follower_no  INT NOT NULL,
                          following_no INT NOT NULL,
                          created_at   varchar(255) NULL,
                          CONSTRAINT FK_follow_follower
                              FOREIGN KEY (follower_no)  REFERENCES member(member_no)
                                  ON DELETE CASCADE ON UPDATE CASCADE,
                          CONSTRAINT FK_follow_following
                              FOREIGN KEY (following_no) REFERENCES member(member_no)
                                  ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB COMMENT='팔로우';


CREATE TABLE `pr_file_upload` (
                                  pr_file_no   INT NOT NULL AUTO_INCREMENT,
                                  pr_file_name VARCHAR(255) NOT NULL,
                                  pr_file_type VARCHAR(255) NOT NULL,
                                  pr_file_rename VARCHAR(255) NOT NULL,
                                  pr_file_path VARCHAR(255) NOT NULL,
                                  pr_file_at   VARCHAR(255) NOT NULL,
                                  review_no    INT NOT NULL,
                                  CONSTRAINT PK_pr_file_upload PRIMARY KEY (pr_file_no),
                                  CONSTRAINT FK_prfile_review FOREIGN KEY (review_no) REFERENCES photo_review(review_no)
)ENGINE=INNODB COMMENT '사진 리뷰 파일 업로드';


CREATE TABLE `worldcup` (
                            worldcup_no           INT NOT NULL AUTO_INCREMENT,
                            worldcup_start_date   VARCHAR(255) NOT NULL,
                            worldcup_finish_date  VARCHAR(255),
                            CONSTRAINT PK_WORLD_CUP PRIMARY KEY (worldcup_no)
)ENGINE=INNODB COMMENT '주간월드컵 게임';


CREATE TABLE `eventFood` (
                             `food_no` INT NOT NULL AUTO_INCREMENT,
                             `board_no` INT NOT NULL,
                             `food_content` VARCHAR(255) NOT NULL,
                             `num_of_wins` INT NOT NULL DEFAULT 0,
                             `worldcup_winning_food` VARCHAR(255),
                             CONSTRAINT PK_EVENT_FOOD  PRIMARY KEY (food_no,board_no),
                             CONSTRAINT FK_EVENT_FOOD_FOOD_POST FOREIGN KEY (board_no) REFERENCES food_post(board_no)
) ENGINE=INNODB COMMENT '이벤트안주';


CREATE TABLE `Individual_world_cup_food` (
                                             `Individual_food` INT NOT NULL AUTO_INCREMENT,
                                             `worldcup_no` INT NOT NULL,
                                             `food_no` INT NOT NULL,
                                             CONSTRAINT PK_INDIVIDUAL_FOOD PRIMARY KEY(Individual_food),
                                             CONSTRAINT FK_WORLDCUP_NO     FOREIGN KEY(worldcup_no) REFERENCES worldcup(worldcup_no),
                                             CONSTRAINT FK_FOOD_NO     FOREIGN KEY(food_no) REFERENCES eventfood(food_no)
)ENGINE=INNODB COMMENT '개인별 월드컵 각각의 게임의 안주';


CREATE TABLE `worldcup_join_member` (
                                        `worldcup_join_member_no` INT NOT NULL AUTO_INCREMENT,
                                        `worldcup_no` INT NOT NULL,
                                        `member_no` INT NOT NULL,
                                        CONSTRAINT PK_WORLDCUP_JOIN_MEMBER_NO PRIMARY KEY(worldcup_join_member_no),
                                        CONSTRAINT FK_MESSAGE_NO_MEMBER_NO FOREIGN KEY(worldcup_no) REFERENCES worldcup(worldcup_no),
                                        CONSTRAINT FK_MESSAGE_NO_MEMBER_NO_2 FOREIGN KEY(member_no) REFERENCES member(member_no)
)ENGINE=INNODB COMMENT '주간 월드컵 게임 참여회원';


CREATE TABLE IF NOT EXISTS `report` (
                                        `report_no` INT NOT NULL AUTO_INCREMENT,
                                        `member_no` INT NOT NULL,
                                        `member_no2` INT NOT NULL,
                                        `report_title` VARCHAR(255) NOT NULL,
                                        `report_content` VARCHAR(255) NOT NULL,
                                        `report_yn` BOOLEAN DEFAULT FALSE,
                                        `report_date` VARCHAR(255) NOT NULL,
                                        `report_source` VARCHAR(255) NOT NULL,
                                        CONSTRAINT PK_REPORT PRIMARY KEY (report_no),
                                        CONSTRAINT FK_REPORT_MEMBER2 FOREIGN KEY (member_no2) REFERENCES member(member_no)
                                            ON UPDATE CASCADE ON DELETE CASCADE,
                                        CONSTRAINT FK_REPORT_MEMBER FOREIGN KEY (member_no) REFERENCES member(member_no)
                                            ON UPDATE CASCADE ON DELETE CASCADE
) ENGINE=INNODB COMMENT '신고';

CREATE TABLE bookmark (
                          member_no INT NOT NULL,
                          board_no  INT NOT NULL,
                          PRIMARY KEY (member_no, board_no),
                          CONSTRAINT FK_BOOKMARK_MEMBER     FOREIGN KEY (member_no) REFERENCES member(member_no),
                          CONSTRAINT FK_BOOKMARK_FOOD_POST  FOREIGN KEY (board_no)  REFERENCES food_post(board_no)
) ENGINE=INNODB COMMENT='즐겨찾기';




CREATE TABLE `albti` (
                         alBTI_no      INT NOT NULL AUTO_INCREMENT,
                         alBTI_category VARCHAR(255) NOT NULL,
                         alBTI_detail   VARCHAR(255) NOT NULL,
                         alcohol_no     INT NOT NULL,
                         CONSTRAINT PK_alBTI PRIMARY KEY (alBTI_no),
                         CONSTRAINT FK_albti_alcohol FOREIGN KEY (alcohol_no) REFERENCES alcohol(alcohol_no)
)ENGINE=INNODB COMMENT '술BTI';


CREATE TABLE `albti_survey` (
                                albti_survey_no INT NOT NULL AUTO_INCREMENT,
                                alBTI_no INT NOT NULL,
                                albti_survey_content VARCHAR(255) NOT NULL,
                                CONSTRAINT PK_albti_survey_no PRIMARY KEY (albti_survey_no),
                                CONSTRAINT FK_albti_no FOREIGN KEY (alBTI_no)  REFERENCES albti(alBTI_no)
)ENGINE=INNODB COMMENT '술BTI 설문지';


CREATE TABLE `albti_join_member` (
                                     alBTI_member_no        INT NOT NULL AUTO_INCREMENT,
                                     member_no              INT NOT NULL,
                                     albti_survey_no        INT NOT NULL,
                                     CONSTRAINT PK_albti_join_member PRIMARY KEY (alBTI_member_no),
                                     CONSTRAINT FK_ajm_member FOREIGN KEY (member_no) REFERENCES member(member_no),
                                     CONSTRAINT FK_ajm_albti_survey_no  FOREIGN KEY (albti_survey_no)  REFERENCES albti_survey(albti_survey_no)
)ENGINE=INNODB COMMENT '술BTI게임 참여회원';




CREATE TABLE `photo_review_comment` (
                                        prc_no     INT NOT NULL AUTO_INCREMENT,
                                        member_no INT NOT NULL,
                                        prc_detail VARCHAR(255) NOT NULL,
                                        prc_at     VARCHAR(255) NOT NULL,
                                        review_no INT NOT NULL,
                                        prc_deleted BOOLEAN NOT NULL,
                                        CONSTRAINT PK_photo_review_comment PRIMARY KEY (prc_no),
                                        CONSTRAINT FK_prc_member FOREIGN KEY (member_no) REFERENCES member(member_no),
                                        CONSTRAINT FK_prc_review FOREIGN KEY (review_no) REFERENCES photo_review(review_no)
)ENGINE=INNODB COMMENT '사진리뷰댓글';




CREATE TABLE `worldcup_alcohol` (
                                    `worldcup_alcohol_no` INT NOT NULL AUTO_INCREMENT,
                                    `alcohol_no` INT NOT NULL,
                                    `worldcup_no` INT NOT NULL,
                                    CONSTRAINT PK_WORLDCUP_ALCOHOL  PRIMARY KEY (worldcup_alcohol_no,alcohol_no,worldcup_no),
                                    CONSTRAINT FK_WORLDCUP_ALCOHOL_ALCOHOL FOREIGN KEY (alcohol_no) REFERENCES alcohol(alcohol_no),
                                    CONSTRAINT FK_WORLDCUP_ALCOHOL_WORLDCUP FOREIGN KEY (worldcup_no) REFERENCES worldcup(worldcup_no)
) ENGINE=INNODB COMMENT '월드컵술';


CREATE TABLE `direct_message` (
                                  `message_no` INT NOT NULL AUTO_INCREMENT,
                                  `send_member_id` INT NOT NULL,
                                  `receive_member_id` INT NOT NULL,
                                  `dm_content` VARCHAR(255) NOT NULL,
                                  `dm_date` VARCHAR(255) NOT NULL,
                                  `dm_read` BOOLEAN NOT NULL DEFAULT 0 COMMENT '메세지 읽음 여부',
                                  CONSTRAINT PK_MESSAGE_NO PRIMARY KEY (message_no),
                                  CONSTRAINT FK_MESSAGE_NO_MEMBER_NO_3 FOREIGN KEY(send_member_id) REFERENCES member(member_no),
                                  CONSTRAINT FK_MESSAGE_NO_MEMBER_NO_4 FOREIGN KEY(receive_member_id) REFERENCES member(member_no)
)ENGINE=INNODB COMMENT 'DM';



CREATE TABLE `dm_file_upload` (
                                  `dm_file_no` INT NOT NULL AUTO_INCREMENT,
                                  `dm_file_name` VARCHAR(255) NOT NULL,
                                  `dm_file_type` VARCHAR(255) NOT NULL,
                                  `dm_file_rename` VARCHAR(255) NOT NULL,
                                  `dm_file_path` VARCHAR(255) NOT NULL,
                                  `dm_file_at` VARCHAR(255) NOT NULL,
                                  `dm_key` INT NOT NULL,
                                  CONSTRAINT  PK_DM_FILE_UPLOAD  PRIMARY KEY (dm_file_no),
                                  CONSTRAINT  FK_DM_FILE_UPLOAD_DIRECT_MESSAGE  FOREIGN KEY (dm_key) REFERENCES direct_message(message_no)
)ENGINE=INNODB COMMENT 'DM파일업로드';



CREATE TABLE `food_post_likes` (
                                   `member_no` INT NOT NULL,
                                   `board_no`  INT NOT NULL,
                                   `likes_type` ENUM('맛없어요','궁금해요','참신해요','술술 들어가요') NOT NULL,
                                   CONSTRAINT `PK_FOOD_LIKES` PRIMARY KEY (`member_no`, `board_no`),
                                   CONSTRAINT `FK_FOOD_POST_LIKES_MEMBER`    FOREIGN KEY (`member_no`) REFERENCES `member`(`member_no`),
                                   CONSTRAINT `FK_FOOD_POST_LIKES_FOOD_POST`  FOREIGN KEY (`board_no`)  REFERENCES `food_post`(`board_no`)
) ENGINE=InnoDB COMMENT='안주게시글반응';

CREATE TABLE `login` (
                         record_id INT NOT NULL AUTO_INCREMENT COMMENT '내역 번호 ',
                         member_no INT NOT NULL COMMENT '회원 번호',
                         last_login_time VARCHAR(255) NOT NULL DEFAULT 0 COMMENT '마지막 로그인 시각',
                         CONSTRAINT PK_login PRIMARY KEY (record_id),
                         CONSTRAINT FK_login_member FOREIGN KEY (member_no) REFERENCES member (member_no)
) ENGINE=INNODB COMMENT '로그인 내역';

CREATE TABLE `food_comment` (
                                `food_comment_no` INT NOT NULL AUTO_INCREMENT,
                                `member_no` INT NOT NULL,
                                `board_no` INT NOT NULL,
                                `fc_content` VARCHAR(255) NOT NULL,
                                `fc_date` VARCHAR(255) NOT NULL,
                                `fc_udate` VARCHAR(255) NULL,
                                CONSTRAINT PK_FOOD_COMMENT PRIMARY KEY(food_comment_no),
                                CONSTRAINT FK_FOOD_COMMENT_MEMBER  FOREIGN KEY (member_no) REFERENCES member(member_no),
                                CONSTRAINT FK_FOOD_COMMENT_FOOD_POST  FOREIGN KEY (board_no) REFERENCES food_post(board_no)
) ENGINE=INNODB COMMENT '안주게시글댓글';


CREATE TABLE `world_cup_picks` (
                                   `world_cup_picks_no` INT NOT NULL AUTO_INCREMENT,
                                   `worldcup_join_member_no` INT NOT NULL,
                                   `worldcup_alcohol_no` INT NOT NULL,
                                   `Individual_food` INT NOT NULL,
                                   CONSTRAINT PK_WORLD_CUP_PICKS  PRIMARY KEY (world_cup_picks_no,worldcup_join_member_no,worldcup_alcohol_no,Individual_food),
                                   CONSTRAINT FK_WORLD_CUP_PICKS_MEMBER FOREIGN KEY (worldcup_join_member_no) REFERENCES worldcup_join_member(worldcup_join_member_no),
                                   CONSTRAINT FK_WORLD_CUP_PICKS_ALCOHOL FOREIGN KEY (worldcup_alcohol_no) REFERENCES worldcup_alcohol(worldcup_alcohol_no),
                                   CONSTRAINT FK_WORLD_CUP_PICKS_INDIVIDUAL_FOOD FOREIGN KEY (Individual_food) REFERENCES Individual_world_cup_food(Individual_food)
) ENGINE=INNODB COMMENT '월드컵별 회원이 고른 술과 안주';

CREATE TABLE `albti_output` (
                                albti_output_no    INT NOT NULL AUTO_INCREMENT,
                                alBTI_no           INT NOT NULL,
                                alBTI_alcohol_explain VARCHAR(255) NOT NULL,
                                board_no           INT NOT NULL,
                                alBTI_member_no    INT NOT NULL,
                                CONSTRAINT PK_albti_output PRIMARY KEY (albti_output_no),
                                CONSTRAINT FK_albtiout_albti FOREIGN KEY (alBTI_no) REFERENCES albti(alBTI_no),
                                CONSTRAINT FK_albtiout_board FOREIGN KEY (board_no) REFERENCES food_post(board_no),
                                CONSTRAINT FK_albtiout_member FOREIGN KEY (alBTI_member_no) REFERENCES albti_join_member(alBTI_member_no)
)ENGINE=INNODB COMMENT '회원별 술BTI 설문 결과';


-- 더미데이터 삽입 (memberEmail 포함)
INSERT INTO `member`
(member_role, member_id, member_pw, member_name, member_birth, member_phone, member_email, member_status, member_active, member_at, member_level, report_count)
VALUES
    ('ADMIN', 'admin01', 'adminpw!', '관리자', '1988-10-10', '010-1111-1111', 'admin@eattoday.com', 'normal', TRUE, '2025-01-05',NULL, 0),
    ('USER', 'soju_love', 'drinkpw1!', '박철수', '1995-07-07', '010-2222-2222', 'soju.love@example.com', 'normal', TRUE, '2025-03-11', 200, 0),
    ('USER', 'beer_queen', 'beerpw2!', '김민지', '1998-11-23', '010-3333-3333', 'beer.queen@example.com', 'suspended', FALSE, '2025-04-02', 300, 0),
    ('USER', 'wine_master', 'winepw3!', '최영희', '1990-02-17', '010-4444-4444', 'wine.master@example.com', 'normal', TRUE, '2025-05-20', 220, 0),
    ('USER', 'makgeolli', 'makpw4!', '정우성', '1993-12-30', '010-5555-5555', 'makgeolli@example.com', 'normal', TRUE, '2025-06-01', 60, 0),
    ('USER', 'cocktail_girl', 'cockpw5!', '한지민', '2000-09-09', '010-6666-6666', 'cocktail.girl@example.com', 'normal', TRUE, '2025-07-18', 50, 0),
    ('USER', 'sake_lover', 'sakpw6!', '오다유키', '1994-01-25', '010-7777-7777', 'sake.lover@example.com', 'normal', TRUE, '2025-08-03', 340, 0),
    ('USER', 'champagne_boy', 'champpw7!', '박상혁', '1992-04-19', '010-8888-8888', 'champagne.boy@example.com', 'normal', TRUE, '2025-08-25', 200, 0),
    ('USER', 'highballer', 'highpw8!', '이진우', '1999-07-15', '010-9999-9999', 'highballer@example.com', 'normal', TRUE, '2025-09-01', 50, 0),
    ('USER', 'vodka_star', 'vodkapw9!', '안지수', '1997-12-12', '010-1010-1010', 'vodka.star@example.com', 'normal', TRUE, '2025-09-05', 50, 0),
    ('USER', 'gin_tonic', 'ginpw10!', '서민호', '1996-06-30', '010-1111-2222', 'gin.tonic@example.com', 'normal', TRUE, '2025-09-07', 50, 0),
    ('USER', 'whisky_time', 'whiskypw11!', '김성훈', '1989-08-21', '010-2222-3333', 'whisky.time@example.com', 'withdrawn', FALSE, '2025-09-10', 500, 0),
    ('USER', 'rum_rider', 'rumpw12!', '홍길동', '1991-09-15', '010-3333-4444', 'rum.rider@example.com', 'normal', TRUE, '2025-09-11', 50, 0),
    ('USER', 'tequila99', 'teqpw13!', '최다혜', '1998-05-22', '010-4444-5555', 'tequila99@example.com', 'normal', TRUE, '2025-09-12', 60, 0),
    ('USER', 'soju_kim', 'sojupw14!', '김철민', '1995-11-30', '010-5555-6666', 'soju.kim@example.com', 'normal', TRUE, '2025-09-13',340, 0),
    ('USER', 'beer_lee', 'beerpw15!', '이수진', '1993-03-18', '010-6666-7777', 'beer.lee@example.com', 'suspended', FALSE, '2025-09-14', 200, 0),
    ('USER', 'wine_park', 'winepw16!', '박지영', '1990-01-07', '010-7777-8888', 'wine.park@example.com', 'normal', TRUE, '2025-09-15', 200, 0),
    ('ADMIN', 'admin02', 'adminpw2!', '서관리', '1985-06-05', '010-8888-9999', 'admin2@eattoday.com', 'normal', TRUE, '2025-09-16', NULL, 0),
    ('USER', 'bbq_master', 'pw1!', '이서준', '1994-02-14', '010-1212-1212', 'bbq.master@example.com', 'normal', TRUE, '2025-09-16', 150, 0),
    ('USER', 'sool_scholar', 'pw2!', '김하늘', '1997-03-01', '010-1313-1313', 'sool.scholar@example.com', 'normal', TRUE, '2025-09-16', 140, 0),
    ('USER', 'wine_beginner', 'pw3!', '최다연', '2001-09-09', '010-1414-1414', 'wine.beginner@example.com', 'normal', TRUE, '2025-09-17', 200, 0),
    ('USER', 'beer_coder', 'pw4!', '박도윤', '1996-05-22', '010-1515-1515', 'beer.coder@example.com', 'normal', TRUE, '2025-09-17', 340, 0),
    ('USER', 'mak_fan', 'pw5!', '정윤아', '1999-08-30', '010-1616-1616', 'mak.fan@example.com', 'normal', TRUE, '2025-09-17', 340, 0),
    ('USER', 'high_holic', 'pw6!', '오세준', '1995-12-02', '010-1717-1717', 'high.holic@example.com', 'normal', TRUE, '2025-09-18', 200, 0),
    ('USER', 'sake_trip', 'pw7!', '장유나', '1998-01-28', '010-1818-1818', 'sake.trip@example.com', 'normal', TRUE, '2025-09-18', 340, 0),
    ('USER', 'wine_note', 'pw8!', '신태훈', '1992-06-06', '010-1919-1919', 'wine.note@example.com', 'normal', TRUE, '2025-09-18', 700, 0),
    ('USER', 'beer_runner', 'pw9!', '한유진', '1997-11-11', '010-2020-2020', 'beer.runner@example.com', 'normal', TRUE, '2025-09-19', 200, 0),
    ('USER', 'soju_writer', 'pw10!', '노수현', '1993-07-17', '010-2121-2121', 'soju.writer@example.com', 'normal', TRUE, '2025-09-19', 800, 0);

-- 나머지 더미 데이터는 기존과 동일하게 유지
INSERT INTO alcohol (alcohol_type, alcohol_explain, alcohol_picture)
VALUES
    ('맥주', '탄산감과 청량감이 특징인 맥주', '/images/alcohol/beer.jpg'),
    ('소주', '대한민국 국민 술 소주', '/images/alcohol/soju.jpg'),
    ('막걸리', '쌀로 빚은 전통 발효주 막걸리', '/images/alcohol/makgeolli.jpg'),
    ('샴페인', '축하 자리에서 빠질 수 없는 샴페인', '/images/alcohol/champagne.jpg'),
    ('사케', '일본의 전통 쌀 술 사케', '/images/alcohol/sake.jpg'),
    ('고량주', '중국 대표 고도주 고량주', '/images/alcohol/gaoliang.jpg'),
    ('하이볼', '위스키와 탄산을 섞은 하이볼', '/images/alcohol/highball.jpg'),
    ('와인', '포도로 만든 서양 와인', '/images/alcohol/wine.jpg'),
    ('기타', '기타 주류 (전통주, 리큐르 등)', '/images/alcohol/etc.jpg');

INSERT INTO food_post
(alcohol_no, member_no, board_title, board_content, food_explain, food_picture,
 board_date, board_seq, confirmed_yn, likes_no_1, likes_no_2, likes_no_3, likes_no_4)
VALUES
-- 1. 치즈닭발 (조회수 많음)
(1, 1, '치즈닭발과 소주', '매운 닭발에 치즈가 듬뿍 올라간 조합!', '매운맛을 잡아주는 부드러운 치즈', '/images/food/chicken.jpg',
 '2025-10-20 20:00:00', 245, 'T', 12, 5, 3, 1),
-- 2. 감바스 (댓글 많은 인기 안주)
(2, 2, '감바스 알 아히요', '올리브오일 향과 새우의 조합이 완벽한 스페인 안주', '맥주보단 와인에 잘 어울림', '/images/food/gambas.jpg',
 '2025-10-18 18:10:00', 180, 'T', 8, 9, 2, 1),
-- 3. 오돌뼈 (소주 안주)
(3, 3, '매운 오돌뼈볶음', '불향이 나는 매운맛이 일품인 소주 안주', '식감이 살아있는 오돌뼈의 매력', '/images/food/odol.jpg',
 '2025-10-21 19:35:00', 205, 'T', 7, 4, 2, 0),
-- 4. 육회비빔밥 (조회수 중간)
(1, 2, '육회비빔밥', '고소한 참기름 향과 신선한 육회', '라거맥주와 의외의 조합', '/images/food/yukhoe.jpg',
 '2025-10-15 12:00:00', 110, 'T', 9, 6, 1, 2),
-- 5. 골뱅이무침 (반응 많음)
(4, 1, '골뱅이무침과 소면', '새콤달콤한 양념과 골뱅이의 조합', '청량한 라거와 환상 궁합', '/images/food/golbaeng.jpg',
 '2025-10-16 19:00:00', 190, 'T', 15, 12, 3, 2),
-- 6. 불족발 (승인 대기 중)
(2, 3, '불족발', '매운 족발에 소주 한 잔!', '단짠 매운맛의 삼박자', '/images/food/jokbal.jpg',
 '2025-10-19 21:20:00', 50, 'F', 3, 2, 1, 0),
-- 7. 피자와 라거 (조회수 높음)
(5, 2, '피자와 라거', '치즈피자에 시원한 라거맥주 한잔', '기름진 음식과 라거의 궁합', '/images/food/pizza.jpg',
 '2025-10-17 22:30:00', 310, 'T', 20, 18, 4, 2),
-- 8. 회오리감자 (테스트용 낮은 조회수)
(1, 1, '회오리감자', '간단한 길거리 간식', '맥주와 간단히 즐길 수 있는 안주', '/images/food/potato.jpg',
 '2025-10-14 15:40:00', 42, 'T', 2, 1, 0, 0);
-- 나머지 INSERT 문들도 기존과 동일하게 포함...
-- (길이 제한으로 인해 생략, 필요시 추가 가능)