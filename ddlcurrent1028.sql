SET FOREIGN_KEY_CHECKS = 1;
-- memberPhone을 아이디 역할에서 memberEmail로 변경
SET FOREIGN_KEY_CHECKS = 0;
DROP TABLE IF EXISTS `albti_answer`;
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
DROP TABLE IF EXISTS `dm_message_read`;
DROP TABLE IF EXISTS `dm_message`;
DROP TABLE IF EXISTS `dm_room_member`;
DROP TABLE IF EXISTS `note_message`;
DROP TABLE IF EXISTS `dm_room`;
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
                          profile_image_url VARCHAR(500) NULL COMMENT '프로필 이미지 URL',
                          CONSTRAINT PK_member PRIMARY KEY (member_no),
                          INDEX idx_member_email (member_email)
) ENGINE=INNODB COMMENT '회원 정보';

CREATE TABLE IF NOT EXISTS `email_verification` (
                                                    `id`            BIGINT       NOT NULL AUTO_INCREMENT,
                                                    `email`         VARCHAR(255) NOT NULL,
    `token`         VARCHAR(255) NOT NULL,
    `expires_at`    DATETIME     NOT NULL,
    `used`          TINYINT(1)   NOT NULL DEFAULT 0,
    `created_at`    DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_email_verification_token` (`token`),
    KEY `idx_email_verification_email` (`email`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS `profile_image` (
                                               `id`                BIGINT       NOT NULL AUTO_INCREMENT,
                                               `member_email`      VARCHAR(255) NOT NULL,
    `original_file_name` VARCHAR(255) NOT NULL,
    `stored_file_name`  VARCHAR(255) NOT NULL,
    `file_path`        VARCHAR(500) NOT NULL,
    `file_size`        BIGINT       NOT NULL,
    `content_type`     VARCHAR(100) NOT NULL,
    `uploaded_at`       DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `is_active`         TINYINT(1)   NOT NULL DEFAULT 1,
    `is_default`        TINYINT(1)   NOT NULL DEFAULT 0,
    PRIMARY KEY (`id`),
    KEY `idx_profile_image_email` (`member_email`),
    KEY `idx_profile_image_active` (`is_active`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='프로필 사진 정보';


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
                            inquiry_title   VARCHAR(255) NOT NULL COMMENT '문의 제목',
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
                                  pr_file_url VARCHAR(255) NOT NULL,
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
                                        `alcohol_no` INT NOT NULL,
                                        CONSTRAINT PK_WORLDCUP_JOIN_MEMBER_NO PRIMARY KEY(worldcup_join_member_no),
                                        CONSTRAINT FK_MESSAGE_NO_MEMBER_NO FOREIGN KEY(worldcup_no) REFERENCES worldcup(worldcup_no),
                                        CONSTRAINT FK_MESSAGE_NO_MEMBER_NO_2 FOREIGN KEY(member_no) REFERENCES member(member_no),
                                        CONSTRAINT FK_WORLDCUP_JOINMEMBER_ALCOHOL FOREIGN KEY (alcohol_no) REFERENCES alcohol(alcohol_no)
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
                                albti_survey_no INT AUTO_INCREMENT PRIMARY KEY,
                                question VARCHAR(255) NOT NULL,
                                type_a INT NOT NULL,  -- A 선택 시 점수 올라가는 alBTI_no
                                type_b INT NOT NULL,  -- B 선택 시 점수 올라가는 alBTI_no
                                CONSTRAINT FK_albti_type_a FOREIGN KEY (type_a) REFERENCES albti(alBTI_no),
                                CONSTRAINT FK_albti_type_b FOREIGN KEY (type_b) REFERENCES albti(alBTI_no)
)ENGINE=INNODB COMMENT '술BTI 설문지';


CREATE TABLE `albti_join_member` (
                                     alBTI_member_no        INT NOT NULL AUTO_INCREMENT,
                                     member_no              INT NOT NULL,
                                     participated_at        date null,
                                     CONSTRAINT PK_albti_join_member PRIMARY KEY (alBTI_member_no),
                                     CONSTRAINT FK_ajm_member FOREIGN KEY (member_no) REFERENCES member(member_no)
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

-- 1) 방
CREATE TABLE dm_room (
                         room_no     INT AUTO_INCREMENT PRIMARY KEY,
                         room_name   VARCHAR(255) NOT NULL,
                         is_group    BOOLEAN NOT NULL DEFAULT 0,
                         private_key VARCHAR(255) UNIQUE,
                         created_at  VARCHAR(255) NOT NULL
) ENGINE=InnoDB COMMENT='DM 방';

-- 2) 방-멤버
CREATE TABLE dm_room_member (
                                room_no   INT NOT NULL,
                                member_no INT NOT NULL,
                                role      ENUM('OWNER','MEMBER') DEFAULT 'MEMBER',
                                joined_at VARCHAR(255) NOT NULL,
                                PRIMARY KEY (room_no, member_no),
                                CONSTRAINT FK_room_member_room   FOREIGN KEY (room_no)   REFERENCES dm_room(room_no)   ON DELETE CASCADE,
                                CONSTRAINT FK_room_member_member FOREIGN KEY (member_no) REFERENCES member(member_no)  ON DELETE CASCADE
) ENGINE=InnoDB COMMENT='방-멤버';

-- 3) 메시지
CREATE TABLE dm_message (
                            message_no INT AUTO_INCREMENT PRIMARY KEY,
                            room_no    INT NOT NULL,
                            sender_no  INT NOT NULL,
                            content    VARCHAR(255) NOT NULL,
                            sent_at    VARCHAR(255) NOT NULL,
                            edited     BOOLEAN NOT NULL DEFAULT 0,
                            edited_at  VARCHAR(255) NULL,
                            deleted    BOOLEAN NOT NULL DEFAULT 0,
                            deleted_at VARCHAR(255) NULL,
                            deleted_by INT NULL,
                            CONSTRAINT FK_msg_room   FOREIGN KEY (room_no)   REFERENCES dm_room(room_no)  ON DELETE CASCADE,
                            CONSTRAINT FK_msg_sender FOREIGN KEY (sender_no) REFERENCES member(member_no)
) ENGINE=InnoDB COMMENT='메시지';

-- 4) 메시지 읽음 (수신자별)
CREATE TABLE dm_message_read (
                                 message_no INT NOT NULL,
                                 member_no  INT NOT NULL,
                                 read_at    VARCHAR(255) NOT NULL,
                                 PRIMARY KEY (message_no, member_no),
                                 CONSTRAINT FK_read_msg    FOREIGN KEY (message_no) REFERENCES dm_message(message_no) ON DELETE CASCADE,
                                 CONSTRAINT FK_read_member FOREIGN KEY (member_no)  REFERENCES member(member_no)     ON DELETE CASCADE
) ENGINE=InnoDB COMMENT='읽음 상태';



DROP TABLE IF EXISTS dm_file_upload;

CREATE TABLE note_message (
                              note_id         INT AUTO_INCREMENT PRIMARY KEY,
                              sender_no       INT NOT NULL,
                              receiver_no     INT NOT NULL,
                              subject         VARCHAR(255) NULL,
                              content         VARCHAR(255) NOT NULL,
                              sent_at_txt     VARCHAR(255) NOT NULL,
                              is_read         BOOLEAN NOT NULL DEFAULT 0,
                              read_at_txt     VARCHAR(255) NULL,
                              reply_to_id     INT NULL,
                              sender_deleted  BOOLEAN NOT NULL DEFAULT 0,
                              receiver_deleted BOOLEAN NOT NULL DEFAULT 0,
                              CONSTRAINT FK_note_sender   FOREIGN KEY (sender_no)   REFERENCES member(member_no),
                              CONSTRAINT FK_note_receiver FOREIGN KEY (receiver_no) REFERENCES member(member_no),
                              CONSTRAINT FK_note_reply    FOREIGN KEY (reply_to_id) REFERENCES note_message(note_id)
) ENGINE=InnoDB COMMENT='쪽지';

CREATE TABLE dm_file_upload (
                                dm_file_no     INT NOT NULL AUTO_INCREMENT,
                                note_id     INT NOT NULL,
                                dm_file_name   VARCHAR(255) NOT NULL,
                                dm_file_type   VARCHAR(255)  NOT NULL,
                                dm_file_rename VARCHAR(255) NOT NULL,
                                dm_file_path   VARCHAR(255) NOT NULL,
                                dm_file_at     VARCHAR(255)  NOT NULL,
                                dm_file_size   LONG NOT NULL,
                                dm_file_hash   VARCHAR(255)  NULL,
                                PRIMARY KEY (dm_file_no),
                                CONSTRAINT FK_DM_FILE__MESSAGE
                                    FOREIGN KEY (note_id) REFERENCES note_message(note_id)
                                        ON DELETE CASCADE
) ENGINE=InnoDB COMMENT='DM 파일 업로드';

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


-- (추가 테이블) 설문 응답 테이블
CREATE TABLE albti_answer (
                              albti_answer_no INT AUTO_INCREMENT PRIMARY KEY,
                              member_no INT NOT NULL,
                              albti_survey_no INT NOT NULL,
                              choice CHAR(1) NOT NULL CHECK (choice IN ('A','B')),
                              CONSTRAINT FK_aa_member FOREIGN KEY (member_no) REFERENCES member(member_no),
                              CONSTRAINT FK_aa_survey FOREIGN KEY (albti_survey_no) REFERENCES albti_survey(albti_survey_no)
)ENGINE=INNODB COMMENT '술BTI 회원별 설문 응답 테이블';



-- 더미데이터 삽입 (memberEmail 포함, 비밀번호 암호화)
INSERT INTO `member`
(member_role, member_id, member_pw, member_name, member_birth, member_phone, member_email, member_status, member_active, member_at, member_level, report_count)
VALUES
    ('ADMIN', 'admin01', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDi', '관리자', '1988-10-10', '010-1111-1111', 'admin@eattoday.com', 'normal', TRUE, '2025-01-05', 0, 0),
    ('USER', 'soju_love', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDi', '박철수', '1995-07-07', '010-2222-2222', 'soju.love@example.com', 'normal', TRUE, '2025-03-11', 200, 0),
    ('USER', 'beer_queen', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDi', '김민지', '1998-11-23', '010-3333-3333', 'beer.queen@example.com', 'suspended', FALSE, '2025-04-02', 300, 0),
    ('USER', 'wine_master', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDi', '최영희', '1990-02-17', '010-4444-4444', 'wine.master@example.com', 'normal', TRUE, '2025-05-20', 220, 0),
    ('USER', 'makgeolli', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDi', '정우성', '1993-12-30', '010-5555-5555', 'makgeolli@example.com', 'normal', TRUE, '2025-06-01', 60, 0),
    ('USER', 'cocktail_girl', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDi', '한지민', '2000-09-09', '010-6666-6666', 'cocktail.girl@example.com', 'normal', TRUE, '2025-07-18', 50, 0),
    ('USER', 'sake_lover', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDi', '오다유키', '1994-01-25', '010-7777-7777', 'sake.lover@example.com', 'normal', TRUE, '2025-08-03', 340, 0),
    ('USER', 'champagne_boy', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDi', '박상혁', '1992-04-19', '010-8888-8888', 'champagne.boy@example.com', 'normal', TRUE, '2025-08-25', 200, 0),
    ('USER', 'highballer', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDi', '이진우', '1999-07-15', '010-9999-9999', 'highballer@example.com', 'normal', TRUE, '2025-09-01', 50, 0),
    ('USER', 'vodka_star', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDi', '안지수', '1997-12-12', '010-1010-1010', 'vodka.star@example.com', 'normal', TRUE, '2025-09-05', 50, 0),
    ('USER', 'gin_tonic', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDi', '서민호', '1996-06-30', '010-1111-2222', 'gin.tonic@example.com', 'normal', TRUE, '2025-09-07', 50, 0),
    ('USER', 'whisky_time', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDi', '김성훈', '1989-08-21', '010-2222-3333', 'whisky.time@example.com', 'withdrawn', FALSE, '2025-09-10', 500, 0),
    ('USER', 'rum_rider', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDi', '홍길동', '1991-09-15', '010-3333-4444', 'rum.rider@example.com', 'normal', TRUE, '2025-09-11', 50, 0),
    ('USER', 'tequila99', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDi', '최다혜', '1998-05-22', '010-4444-5555', 'tequila99@example.com', 'normal', TRUE, '2025-09-12', 60, 0),
    ('USER', 'soju_kim', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDi', '김철민', '1995-11-30', '010-5555-6666', 'soju.kim@example.com', 'normal', TRUE, '2025-09-13',340, 0),
    ('USER', 'beer_lee', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDi', '이수진', '1993-03-18', '010-6666-7777', 'beer.lee@example.com', 'suspended', FALSE, '2025-09-14', 200, 0),
    ('USER', 'wine_park', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDi', '박지영', '1990-01-07', '010-7777-8888', 'wine.park@example.com', 'normal', TRUE, '2025-09-15', 200, 0),
    ('ADMIN', 'admin02', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDi', '서관리', '1985-06-05', '010-8888-9999', 'admin2@eattoday.com', 'normal', TRUE, '2025-09-16', 0, 0),
    ('USER', 'bbq_master', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDi', '이서준', '1994-02-14', '010-1212-1212', 'bbq.master@example.com', 'normal', TRUE, '2025-09-16', 150, 0),
    ('USER', 'sool_scholar', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDi', '김하늘', '1997-03-01', '010-1313-1313', 'sool.scholar@example.com', 'normal', TRUE, '2025-09-16', 140, 0),
    ('USER', 'wine_beginner', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDi', '최다연', '2001-09-09', '010-1414-1414', 'wine.beginner@example.com', 'normal', TRUE, '2025-09-17', 200, 0),
    ('USER', 'beer_coder', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDi', '박도윤', '1996-05-22', '010-1515-1515', 'beer.coder@example.com', 'normal', TRUE, '2025-09-17', 340, 0),
    ('USER', 'mak_fan', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDi', '정윤아', '1999-08-30', '010-1616-1616', 'mak.fan@example.com', 'normal', TRUE, '2025-09-17', 340, 0),
    ('USER', 'high_holic', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDi', '오세준', '1995-12-02', '010-1717-1717', 'high.holic@example.com', 'normal', TRUE, '2025-09-18', 200, 0),
    ('USER', 'sake_trip', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDi', '장유나', '1998-01-28', '010-1818-1818', 'sake.trip@example.com', 'normal', TRUE, '2025-09-18', 340, 0),
    ('USER', 'wine_note', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDi', '신태훈', '1992-06-06', '010-1919-1919', 'wine.note@example.com', 'normal', TRUE, '2025-09-18', 700, 0),
    ('USER', 'beer_runner', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDi', '한유진', '1997-11-11', '010-2020-2020', 'beer.runner@example.com', 'normal', TRUE, '2025-09-19', 200, 0),
    ('USER', 'soju_writer', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDi', '노수현', '1993-07-17', '010-2121-2121', 'soju.writer@example.com', 'normal', TRUE, '2025-09-19', 800, 0);

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
-- 1. 치즈닭발 (조회수 많음) -> 소주(2)
(2, 1, '치즈닭발과 소주', '매운 닭발에 치즈가 듬뿍 올라간 조합!', '매운맛을 잡아주는 부드러운 치즈', '/images/food/chicken.jpg',
 '2025-10-20 20:00:00', 245, 'T', 12, 5, 3, 1),
-- 2. 감바스 (댓글 많은 인기 안주) -> 와인(8)
(8, 2, '감바스 알 아히요', '올리브오일 향과 새우의 조합이 완벽한 스페인 안주', '맥주보단 와인에 잘 어울림', '/images/food/gambas.jpg',
 '2025-10-18 18:10:00', 180, 'T', 8, 9, 2, 1),
-- 3. 오돌뼈 (소주 안주) -> 소주(2)
(2, 3, '매운 오돌뼈볶음', '불향이 나는 매운맛이 일품인 소주 안주', '식감이 살아있는 오돌뼈의 매력', '/images/food/odol.jpg',
 '2025-10-21 19:35:00', 205, 'T', 7, 4, 2, 0),
-- 4. 육회비빔밥 (조회수 중간) -> 라거맥주(1)
(1, 2, '육회비빔밥', '고소한 참기름 향과 신선한 육회', '라거맥주와 의외의 조합', '/images/food/yukhoe.jpg',
 '2025-10-15 12:00:00', 110, 'T', 9, 6, 1, 2),
-- 5. 골뱅이무침 (반응 많음) -> 라거(1)
(1, 1, '골뱅이무침과 소면', '새콤달콤한 양념과 골뱅이의 조합', '청량한 라거와 환상 궁합', '/images/food/golbaeng.jpg',
 '2025-10-16 19:00:00', 190, 'T', 15, 12, 3, 2),
-- 6. 불족발 (승인 대기 중) -> 소주(2)
(2, 3, '불족발', '매운 족발에 소주 한 잔!', '단짠 매운맛의 삼박자', '/images/food/jokbal.jpg',
 '2025-10-19 21:20:00', 50, 'F', 3, 2, 1, 0),
-- 7. 피자와 라거 (조회수 높음) -> 맥주(1)
(1, 2, '피자와 라거', '치즈피자에 시원한 라거맥주 한잔', '기름진 음식과 라거의 궁합', '/images/food/pizza.jpg',
 '2025-10-17 22:30:00', 310, 'T', 20, 18, 4, 2),
-- 8. 회오리감자 (테스트용 낮은 조회수) -> 맥주(1)
(1, 1, '회오리감자', '간단한 길거리 간식', '맥주와 간단히 즐길 수 있는 안주', '/images/food/potato.jpg',
 '2025-10-14 15:40:00', 42, 'T', 2, 1, 0, 0),
-- 9. 떡볶이와 막걸리
(3, 1, '떡볶이와 막걸리', '비 오는 날 생각나는 조합', '고추장 양념과 막걸리의 단짠조합', '/images/food/tteokbokki.jpg',
 '2025-10-13 18:30:00', 88, 'T', 10, 7, 3, 1),
-- 10. 삼겹살과 소주
(2, 2, '삼겹살엔 역시 소주', '고소한 삼겹살에 시원한 소주 한잔', '기본이지만 완벽한 조합', '/images/food/samgyeopsal.jpg',
 '2025-10-12 19:10:00', 230, 'T', 18, 9, 5, 2),
-- 11. 치킨과 맥주
(1, 3, '치맥은 진리', '바삭한 치킨과 시원한 맥주', '국민 안주', '/images/food/chickenbeer.jpg',
 '2025-10-09 21:00:00', 400, 'T', 25, 20, 6, 3),
-- 12. 문어숙회와 소주
(2, 1, '문어숙회와 소주 한잔', '쫄깃한 문어에 고추냉이 간장', '심플하지만 깊은 맛', '/images/food/octopus.jpg',
 '2025-10-10 18:45:00', 190, 'T', 9, 4, 2, 1),
-- 13. 감자전과 막걸리
(3, 2, '감자전엔 막걸리지', '바삭한 전과 부드러운 막걸리의 조합', '비오는 날 최고', '/images/food/potatopancake.jpg',
 '2025-10-08 17:40:00', 145, 'T', 8, 6, 2, 1),
-- 14. 오이무침과 막걸리
(3, 3, '가벼운 안주 오이무침', '상큼한 오이무침으로 입가심', '저알콜 안주로 딱', '/images/food/cucumber.jpg',
 '2025-10-07 14:10:00', 50, 'F', 2, 1, 0, 0),
-- 15. 새우튀김과 맥주
(1, 2, '새우튀김엔 맥주 한잔', '바삭한 튀김옷에 시원한 라거', '짭짤한 안주로 최고', '/images/food/shrimp.jpg',
 '2025-10-09 20:20:00', 250, 'T', 14, 10, 3, 2),
-- 16. 두부김치와 소주
(2, 1, '두부김치에 소주 한잔', '기본 안주 중의 기본', '매콤한 김치가 포인트', '/images/food/dubukimchi.jpg',
 '2025-10-11 18:15:00', 180, 'T', 11, 8, 2, 1),
-- 17. 나쵸와 맥주
(1, 3, '나쵸와 맥주', '영화볼 때 딱 좋은 간단 안주', '살짝 짠맛이 맥주를 부른다', '/images/food/nacho.jpg',
 '2025-10-09 22:00:00', 200, 'T', 13, 9, 4, 2),
-- 18. 회와 와인
(8, 2, '숙성회와 와인', '신선한 회에 화이트와인의 깔끔한 맛', '일식 안주 느낌', '/images/food/sashimi.jpg',
 '2025-10-06 19:00:00', 300, 'T', 15, 12, 5, 3),
-- 19. 족발과 맥주
(1, 1, '족발엔 맥주도 괜찮다', '달짝지근한 족발에 시원한 맥주', '탄산이 기름기를 잡는다', '/images/food/jokbalbeer.jpg',
 '2025-10-05 20:30:00', 170, 'F', 6, 5, 1, 0),
-- 20. 순대볶음과 소주
(2, 2, '순대볶음의 매력', '매콤한 순대볶음에 소주 한 잔', '술이 술술 들어간다', '/images/food/soondae.jpg',
 '2025-10-04 19:10:00', 195, 'T', 9, 8, 2, 1),
-- 21. 치즈스틱과 맥주
(1, 3, '치즈스틱과 맥주', '치즈의 고소함과 맥주의 시원함', '단짠의 정석', '/images/food/cheesestick.jpg',
 '2025-10-04 22:30:00', 120, 'F', 4, 3, 1, 0),
-- 22. 낙지볶음과 소주
(2, 1, '낙지볶음엔 소주지', '매운 낙지볶음의 감칠맛', '매운맛 중독', '/images/food/nakji.jpg',
 '2025-10-03 18:50:00', 210, 'T', 10, 7, 3, 1),
-- 23. 김치전과 막걸리
(3, 2, '김치전엔 막걸리', '김치의 매콤함과 막걸리의 단맛', '전골 안주로도 가능', '/images/food/kimchipancake.jpg',
 '2025-10-02 17:00:00', 230, 'T', 11, 9, 3, 2),
-- 24. 오징어볶음과 소주
(2, 3, '오징어볶음과 소주', '불맛 나는 오징어볶음', '매콤한 안주로 인기', '/images/food/squid.jpg',
 '2025-10-01 20:00:00', 190, 'T', 12, 8, 3, 1),
-- 25. 감자튀김과 맥주
(1, 2, '감자튀김엔 맥주', '시원한 맥주와 감자튀김의 궁합', '세계 어디서나 통하는 조합', '/images/food/fries.jpg',
 '2025-09-30 22:00:00', 275, 'T', 16, 13, 4, 2),
-- 26. 소시지야채볶음과 소주
(2, 1, '소시지야채볶음 안주', '단짠단짠 소시지 볶음', '가정식 안주 느낌', '/images/food/sausage.jpg',
 '2025-09-29 18:30:00', 100, 'F', 5, 4, 1, 0),
-- 27. 홍합탕과 소주
(2, 3, '시원한 홍합탕', '국물 안주로 딱 좋은 메뉴', '해장에도 좋음', '/images/food/mussel.jpg',
 '2025-09-28 19:40:00', 155, 'T', 8, 6, 2, 1),
-- 28. 육포와 맥주
(1, 2, '육포에 맥주 한잔', '씹는 맛이 좋은 간단 안주', '가볍게 한잔용', '/images/food/jerky.jpg',
 '2025-09-27 21:00:00', 85, 'F', 3, 2, 0, 0),
-- 29. 계란말이와 소주
(2, 1, '계란말이 안주', '부드럽고 단백한 안주', '순한 맛을 좋아하는 분에게 추천', '/images/food/eggroll.jpg',
 '2025-09-26 18:20:00', 90, 'F', 4, 2, 1, 0),
-- 30. 김치찌개와 소주
(2, 3, '김치찌개에 소주 한잔', '뜨끈한 국물에 한잔하면 천국', '국밥 대신 좋은 안주', '/images/food/kimchistew.jpg',
 '2025-09-25 19:30:00', 270, 'T', 14, 10, 4, 2),
-- 31. 해물파전과 막걸리
(3, 2, '해물파전엔 막걸리', '비 오는 날의 완벽한 세트', '식감이 살아있다', '/images/food/pajeon.jpg',
 '2025-09-24 18:00:00', 320, 'T', 17, 13, 4, 2),
-- 32. 치즈감자볼과 맥주
(1, 1, '치즈감자볼과 맥주', '치즈가 쭉 늘어나는 간단 안주', '편의점 안주 느낌', '/images/food/potatoball.jpg',
 '2025-09-23 22:10:00', 95, 'F', 3, 2, 1, 0),
-- 33. 매운닭꼬치와 소주
(2, 2, '매운닭꼬치 안주', '불향 나는 닭꼬치와 소주 궁합', '길거리 안주 느낌', '/images/food/chickenskewer.jpg',
 '2025-09-22 19:50:00', 180, 'T', 10, 8, 3, 1),
-- 34. 회무침과 와인
(8, 3, '회무침과 와인', '매콤달콤한 회무침에 화이트와인', '의외로 잘 어울림', '/images/food/hoemuchim.jpg',
 '2025-09-21 20:10:00', 190, 'T', 9, 7, 3, 1),
-- 35. 멘치까스와 맥주
(1, 1, '멘치까스 안주', '기름진 고기와 라거의 환상 궁합', '일식풍 안주', '/images/food/menchi.jpg',
 '2025-09-20 21:00:00', 160, 'T', 11, 9, 3, 1),
-- 36. 치즈감바스와 와인
(8, 2, '치즈감바스와 와인', '감바스에 치즈 추가한 풍미', '레드와인에도 어울림', '/images/food/cheesegambas.jpg',
 '2025-09-19 19:10:00', 210, 'F', 6, 5, 1, 0),
-- 37. 꼬치어묵탕과 소주
(2, 3, '어묵탕 안주', '따뜻한 국물이 일품', '겨울철 인기 메뉴', '/images/food/fishtofu.jpg',
 '2025-09-18 18:40:00', 195, 'T', 9, 7, 3, 1),
-- 38. 브루스케타와 와인
(8, 1, '브루스케타와 와인', '토마토와 바질의 조화', '와인 안주로 인기', '/images/food/bruschetta.jpg',
 '2025-09-17 20:00:00', 250, 'T', 13, 10, 4, 2),
-- 39. 닭강정과 맥주
(1, 2, '닭강정과 맥주', '달콤짭짤한 닭강정', '술이 절로 들어간다', '/images/food/dakgangjeong.jpg',
 '2025-09-16 21:00:00', 280, 'T', 16, 12, 4, 2),
-- 40. 오돌뼈덮밥과 소주
(2, 1, '오돌뼈덮밥 안주', '매운 오돌뼈에 밥까지 더한 메뉴', '한 끼로도 충분', '/images/food/odoldon.jpg',
 '2025-09-15 19:30:00', 130, 'F', 5, 3, 1, 0);

INSERT INTO photo_review (board_no, member_no, review_title, review_date, review_content, review_like)
VALUES
    (1, 1,'치맥 인증샷', '2025-09-05', '정말 시원하고 맛있었어요!', 12),
    (2, 2,'삼겹살엔 역시 소주', '2025-09-06', '소주 없으면 섭섭하죠.', 8),
    (3, 4,'스테이크와 와인', '2025-09-07', '레드 와인이 고기랑 너무 잘 어울려요.', 15),
    (5, 10,'피맥 말고 피-라', '2025-09-06', '라거도 잘 어울리네', 6),
    (6, 11,'화이트와인엔 치즈', '2025-09-07', '산도/지방 밸런스 굿', 10),
    (7, 12,'골소 소주 국룰', '2025-09-08', '매콤상큼 술 진도 쭉', 4),
    (8, 17,'감튀엔 하이볼', '2025-09-09', '탄산감이 느끼함 컷', 7),
    (9, 1,'스모키 스모키', '2025-09-10', '고급진 안주 매칭', 9),
    (10, 2,'막걸리엔 파전', '2025-09-11', '전/막 조합은 진리', 8),
    (1, 3,'치맥 2차 인증', '2025-09-12', '이번엔 양념치킨', 5);

INSERT INTO follow (follower_no, following_no) VALUES
                                                   (2, 3),   -- soju_love  -> beer_queen
                                                   (2, 4),   -- soju_love  -> wine_master
                                                   (2, 5),   -- soju_love  -> makgeolli
                                                   (3, 2),   -- beer_queen -> soju_love
                                                   (3, 4),   -- beer_queen -> wine_master
                                                   (4, 2),   -- wine_master -> soju_love
                                                   (4, 5),   -- wine_master -> makgeolli
                                                   (5, 2),   -- makgeolli  -> soju_love
                                                   (6, 2),   -- cocktail_girl -> soju_love
                                                   (6, 3),   -- cocktail_girl -> beer_queen
                                                   (7, 8),   -- sake_lover -> champagne_boy
                                                   (7, 5),   -- sake_lover -> makgeolli
                                                   (8, 9),   -- champagne_boy -> highballer
                                                   (8, 4),   -- champagne_boy -> wine_master
                                                   (9, 6),   -- highballer -> cocktail_girl
                                                   (9, 2),   -- highballer -> soju_love
                                                   (10, 2),  -- vodka_star  -> soju_love
                                                   (10, 4),  -- vodka_star  -> wine_master
                                                   (10, 8);  -- vodka_star  -> champagne_boy
INSERT INTO bookmark (member_no, board_no)
VALUES
    (2, 1),
    (3, 2),
    (4, 3),
    (5, 4);

INSERT INTO report (member_no, member_no2, report_title, report_content, report_yn, report_date, report_source)
VALUES
    (3, 2, '부적절한 리뷰', '욕설이 포함된 리뷰 발견', FALSE, '2025-09-08', 'photo_review'),
    (5, 4, '스팸 게시글', '같은 내용 반복 등록', FALSE, '2025-09-09', 'food_post'),
    (2,3,'과도한 홍보','댓글에 광고 링크', FALSE,'2025-09-12','food_comment'),
    (4,5,'도배 의심','반복 리뷰 업로드', FALSE,'2025-09-13','photo_review'),
    (5,2,'비방 표현','상대 비하 표현', FALSE,'2025-09-14','direct_message'),
    (9,5,'허위 정보','사실과 다른 정보', FALSE,'2025-09-14','food_post'),
    (3,4,'부적절 이미지','정책 위반 추정', FALSE,'2025-09-15','photo_review'),
    (11,2,'욕설 신고','DM에서 욕설', FALSE,'2025-09-15','direct_message'),
    (6,7,'스팸 링크','외부 스팸 유도', FALSE,'2025-09-16','qnd_post'),
    (8,9,'저작권 의심','이미지 출처 불명', FALSE,'2025-09-16','photo_review');

INSERT INTO worldcup (worldcup_start_date, worldcup_finish_date)
VALUES
    ('2025-10-27', '2025-11-02'),
    ('2025-11-03', '2025-11-09'),
    ('2025-11-10', '2025-11-16'),
    ('2025-11-17', '2025-11-23');

INSERT INTO eventFood (board_no, food_content, num_of_wins,worldcup_winning_food)
VALUES
    (1, '바삭하게 튀겨낸 치킨', 3,'치킨'),
    (2, '불판 위에서 지글지글 구워낸 삼겹살 한 점의 행복', 5,'삼겹살'),
    (3, '육즙 가득한 스테이크', 2,'스테이크'),
    (4, '빗소리와 함께 먹는 바삭한 파전', 4,'파전');



INSERT INTO albti (alBTI_category, alBTI_detail, alcohol_no)
VALUES
    ('활발함', '파티에서 분위기를 띄우는 타입', 1),
    ('차분함', '조용히 술을 즐기는 타입', 8),
    ('전통파', '한국 술을 선호하는 타입', 2),
    ('도전적', '새로운 술을 시도하는 타입', 7),
    ('감성파','잔잔한 음악과 함께하는 타입', 9),
    ('테이스팅러버','향미 분석을 즐기는 타입', 4),
    ('로맨틱','분위기와 조화를 중시', 8),
    ('정열파','매운 안주와 강한 술 선호', 2),
    ('힐링러','편안한 한잔', 3),
    ('클래식','전통 조합 선호', 1);

-- 1) 사진 리뷰 댓글
INSERT INTO photo_review_comment
(member_no, prc_detail, prc_at, review_no, prc_deleted)
VALUES
-- 1번 리뷰에 2번 회원이 댓글 작성
(2, '와 이 사진 진짜 맛있어 보이네요!', '2025-09-10 12:30:00', 1, FALSE),
-- 1번 리뷰에 3번 회원이 댓글 작성
(3, '저도 이거 먹어봤는데 완전 강추합니다.', '2025-09-10 13:15:00', 1, FALSE),
-- 2번 리뷰에 4번 회원이 댓글 작성
(4, '사진 각도가 너무 예쁘네요.', '2025-09-11 09:05:00', 2, FALSE),
-- 3번 리뷰에 5번 회원이 댓글 작성
(5, '리뷰 덕분에 가게 찾아가봤습니다. 최고!', '2025-09-12 18:45:00', 3, FALSE),
-- 3번 리뷰에 6번 회원이 댓글 작성 후 삭제됨
(6, '음식이 별로였어요..', '2025-09-12 19:00:00', 3, TRUE);



-- 3) 라운지(리뷰-술 매핑)
--   리뷰1=맥주(1), 리뷰2=소주(2), 리뷰3=와인(8)
INSERT INTO lounge (review_no, alcohol_no)
VALUES
    (1, 1),
    (2, 2),
    (3, 8);


-- 5) 안주 게시글 반응(좋아요/싫어요 등 타입 가정)
INSERT INTO food_post_likes (member_no, board_no, likes_type)
VALUES
    (3, 1, '궁금해요'),
    (4, 2, '참신해요'),
    (5, 3, '술술 들어가요'),
    (2, 4, '맛없어요');


-- 6) 안주 게시글 댓글
INSERT INTO food_comment (member_no, board_no, fc_content, fc_date)
VALUES
    (3, 1, '치맥 조합 최고죠!', '2025-09-05'),
    (4, 2, '삼겹살엔 소주 국룰', '2025-09-06');

-- 7) 로그인 내역 (최근 로그인 가정)
INSERT INTO login (member_no, last_login_time)
VALUES
    (2, '2025-09-15'),
    (3, '2025-09-15'),
    (4, '2025-09-15'),
    (5, '2025-09-15'),
    (6,'2025-09-16'),
    (7,'2025-09-16'),
    (8,'2025-09-16'),
    (9,'2025-09-17'),
    (10,'2025-09-17'),
    (11,'2025-09-17');

-- 8) 탈퇴 이력 (withdrawn 상태인 12번 사용자)
INSERT INTO secession (member_no, secession_at)
VALUES
    (12, '2025-09-10');

-- 9) 문의(QnA) 게시글 (테이블명이 qnd_post로 정의되어 있으므로 그 이름 사용)
INSERT INTO qna_post (member_no, inquiry_title, inquiry_content, inquiry_at)
VALUES
    (2, '사진 업로드 오류', '사진 리뷰가 안 올라가요', '2025-09-12'),
    (3, '월드컵 일정 문의', '월드컵 일정이 궁금합니다', '2025-09-13');


-- 문의 게시글 답변
INSERT INTO qna_comment (qna_post_no, comment_member_no, comment_content, comment_at)
VALUES
    (1, 1, '안녕하세요. 파일 업로드 오류는 금일 중 조치하겠습니다.', '2025-09-12 10:30:00'),
    (2, 1, '월드컵 일정은 매주 월요일에 공지됩니다.', '2025-09-13 09:15:00'),
    (1, 1, '추가 문의 있으시면 언제든 연락주세요.', '2025-09-14 15:00:00');


-- 10) 월드컵-술 매핑 (worldcup_alcohol)
--     worldcup 1: 맥주(1), 와인(8)
--     worldcup 2: 소주(2), 하이볼(7)
--     일관 참조를 위해 worldcup_alcohol_no를 명시적으로 고정
INSERT INTO worldcup_alcohol (worldcup_alcohol_no, alcohol_no, worldcup_no)
VALUES
    (1, 1, 1),
    (2, 2, 1),
    (3, 3, 1),
    (4, 1, 2),
    (5, 2, 2),
    (6, 3, 2),
    (7, 1, 3),
    (8, 2, 3),
    (9, 3, 3);

-- 11) 월드컵 참여 회원 (worldcup_join_member)
--     ID를 고정적으로 지정해 아래 picks에서 참조
INSERT INTO worldcup_join_member (worldcup_join_member_no, worldcup_no, member_no,alcohol_no)
VALUES
    (1, 1, 2,1),
    (2, 1, 3,2),
    (3, 1, 4,1),
    (4, 2, 5,2),
    (5, 2, 2,3);

-- 12) 개인별 월드컵 후보 안주 (Individual_world_cup_food)
--     worldcup 1: eventFood(1=치킨, 3=스테이크)
--     worldcup 2: eventFood(2=삼겹살, 4=파전)
INSERT INTO Individual_world_cup_food (Individual_food, worldcup_no, food_no)
VALUES
    (1, 1, 1),
    (2, 1, 3),
    (3, 2, 2),
    (4, 2, 4);

-- 13) 월드컵 선택 (world_cup_picks)
--     (world_cup_picks_no, worldcup_join_member_no, worldcup_alcohol_no, Individual_food)
INSERT INTO world_cup_picks (world_cup_picks_no, worldcup_join_member_no, worldcup_alcohol_no, Individual_food)
VALUES
    (1, 1, 1, 1),  -- (wc1, member 2) 맥주 + 치킨
    (2, 2, 2, 2),  -- (wc1, member 3) 와인 + 스테이크
    (3, 3, 1, 1),  -- (wc1, member 4) 맥주 + 치킨
    (4, 4, 3, 3),  -- (wc2, member 5) 소주 + 삼겹살
    (5, 5, 4, 4);  -- (wc2, member 2) 하이볼 + 파전

-- 14) 추가한 더미데이터 ##### 술BTI 설문지(albti_survey)
INSERT INTO albti_survey (question, type_a, type_b)
VALUES
    ('술자리는 시끄러운 게 좋다', 1, 2),   -- 1=활발함, 2=차분함 (지금은 yes or no인데 프론트에서 문자열로 바꿀수있는지)
    ('익숙한 술이 좋다', 3, 4),        -- 전통파 vs 도전적
    ('음악과 함께 마신다', 5, 6),      -- 감성파 vs 정열파
    ('분위기와 조화를 중요하게 생각한다', 8, 7), -- 로맨틱 vs 테이스팅러버
    ('편안하게 한잔하는 걸 좋아한다', 9, 2),     -- 힐링러 vs 차분함
    ('전통 조합을 선호한다', 10, 4),   -- 클래식 vs 도전적
    ('술 마실 때 게임을 즐긴다', 1, 9),        -- 활발함 vs 힐링러
    ('새로운 안주 조합을 시도해본다', 4, 3),    -- 도전적 vs 전통파
    ('불꽃 튀는 토론 분위기를 좋아한다', 6, 5),  -- 정열파 vs 감성파
    ('잔잔한 분위기에서 깊은 대화를 나누는 걸 선호한다', 2, 8); -- 차분함 vs 로맨틱

-- 15) 술BTI 참여 (albti_join_member)
INSERT INTO albti_join_member (member_no,participated_at)
VALUES
    ( 1,'2025-10-24'),
    ( 3,'2025-10-25'),
    ( 2,'2025-10-26'),
    ( 4,'2025-10-27');

-- 16) 회원별 술BTI 설문 결과 (albti_output)
INSERT INTO albti_output (alBTI_no, alBTI_alcohol_explain, board_no, alBTI_member_no)
VALUES
    (1, '활발한 파티러버 스타일에 어울리는 맥주', 1,1),
    (2, '차분히 즐길 수 있는 와인 추천', 3,2),
    (3, '전통 한식과 어울리는 소주 추천', 2,3),
    (4, '새로운 조합에 도전하는 하이볼 추천', 4,4);

-- 17) 술BTI 응답 테이블(albti_answer)
INSERT INTO albti_answer (member_no, albti_survey_no, choice)
VALUES
-- 회원 1
(1, 1, 'A'), -- 활발함
(1, 2, 'B'), -- 도전적
(1, 3, 'A'), -- 감성파
(1, 4, 'B'), -- 테이스팅러버
(1, 5, 'A'), -- 힐링러
(1, 6, 'A'), -- 클래식
(1, 7, 'A'), -- 활발함
(1, 8, 'B'), -- 전통파
(1, 9, 'A'), -- 정열파
(1, 10, 'B'), -- 로맨틱
-- 회원 2
(2, 1, 'B'), -- 차분함
(2, 2, 'A'), -- 전통파
(2, 3, 'B'), -- 정열파
(2, 4, 'A'), -- 로맨틱
(2, 5, 'B'), -- 차분함
(2, 6, 'B'), -- 도전적
(2, 7, 'B'), -- 힐링러
(2, 8, 'A'), -- 도전적
(2, 9, 'B'), -- 감성파
(2, 10, 'A'); -- 차분함

-- =========================
-- PATCH: schema alignment
-- =========================
# USE mydb;

-- 1) email_verification 정합화
--    - verified 컬럼 없으면 추가
--    - used -> verified 값 이관 후 used 삭제(있을 때만)
--    - type 컬럼 없으면 추가
--    - token 유니크/ email 인덱스 없으면 추가

-- 1-1) verified 컬럼 추가 (없을 때만)
SET @has_verified := (
    SELECT COUNT(*) FROM INFORMATION_SCHEMA.COLUMNS
    WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'email_verification' AND COLUMN_NAME = 'verified'
);
SET @sql := IF(@has_verified = 0,
               'ALTER TABLE `email_verification` ADD COLUMN `verified` TINYINT(1) NOT NULL DEFAULT 0 AFTER `expires_at`',
               'DO 0');
PREPARE stmt FROM @sql; EXECUTE stmt; DEALLOCATE PREPARE stmt;

-- 1-2) used -> verified 값 이관 (used 있을 때만)
SET @has_used := (
    SELECT COUNT(*) FROM INFORMATION_SCHEMA.COLUMNS
    WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'email_verification' AND COLUMN_NAME = 'used'
);
SET @sql := IF(@has_used = 1,
               'UPDATE `email_verification` SET `verified` = `used` WHERE `verified` <> `used`',
               'DO 0');
PREPARE stmt FROM @sql; EXECUTE stmt; DEALLOCATE PREPARE stmt;

-- 1-3) used 컬럼 삭제 (있을 때만)
SET @sql := IF(@has_used = 1,
               'ALTER TABLE `email_verification` DROP COLUMN `used`',
               'DO 0');
PREPARE stmt FROM @sql; EXECUTE stmt; DEALLOCATE PREPARE stmt;

-- 1-4) type 컬럼 추가 (없을 때만)
SET @has_type := (
    SELECT COUNT(*) FROM INFORMATION_SCHEMA.COLUMNS
    WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'email_verification' AND COLUMN_NAME = 'type'
);
SET @sql := IF(@has_type = 0,
               'ALTER TABLE `email_verification` ADD COLUMN `type` VARCHAR(31) NOT NULL DEFAULT ''EMAIL_VERIFICATION'' AFTER `verified`',
               'DO 0');
PREPARE stmt FROM @sql; EXECUTE stmt; DEALLOCATE PREPARE stmt;

-- 1-5) token 유니크 키 보강 (없을 때만)
SET @has_token_uk := (
    SELECT COUNT(*) FROM INFORMATION_SCHEMA.STATISTICS
    WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'email_verification' AND INDEX_NAME = 'uk_email_verification_token'
);
SET @sql := IF(@has_token_uk = 0,
               'ALTER TABLE `email_verification` ADD UNIQUE KEY `uk_email_verification_token` (`token`)',
               'DO 0');
PREPARE stmt FROM @sql; EXECUTE stmt; DEALLOCATE PREPARE stmt;

-- 1-6) email 인덱스 보강 (없을 때만)
SET @has_email_idx := (
    SELECT COUNT(*) FROM INFORMATION_SCHEMA.STATISTICS
    WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'email_verification' AND INDEX_NAME = 'idx_email_verification_email'
);
SET @sql := IF(@has_email_idx = 0,
               'CREATE INDEX `idx_email_verification_email` ON `email_verification` (`email`)',
               'DO 0');
PREPARE stmt FROM @sql; EXECUTE stmt; DEALLOCATE PREPARE stmt;

-- 1-7) verification_code 컬럼 추가 (없을 때만)
SET @has_verification_code := (
    SELECT COUNT(*) FROM INFORMATION_SCHEMA.COLUMNS
    WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'email_verification' AND COLUMN_NAME = 'verification_code'
);
SET @sql := IF(@has_verification_code = 0,
               'ALTER TABLE `email_verification` ADD COLUMN `verification_code` VARCHAR(6) NOT NULL DEFAULT ''000000'' AFTER `email`',
               'DO 0');
PREPARE stmt FROM @sql; EXECUTE stmt; DEALLOCATE PREPARE stmt;


-- 2) Individual_world_cup_food → eventFood FK 대소문자/테이블명 정합화
--    (만약 잘못된 참조가 있으면 제거 후 올바르게 생성)
SET @bad_fk := (
    SELECT CONSTRAINT_NAME
    FROM INFORMATION_SCHEMA.REFERENTIAL_CONSTRAINTS
    WHERE CONSTRAINT_SCHEMA = DATABASE()
      AND TABLE_NAME = 'Individual_world_cup_food'
      AND REFERENCED_TABLE_NAME = 'eventfood'           -- 잘못된 소문자 참조
    LIMIT 1
);
SET @sql := IF(@bad_fk IS NOT NULL,
               CONCAT('ALTER TABLE `Individual_world_cup_food` DROP FOREIGN KEY `', @bad_fk, '`'),
               'DO 0');
PREPARE stmt FROM @sql; EXECUTE stmt; DEALLOCATE PREPARE stmt;

-- 올바른 FK가 없으면 생성
SET @good_fk_exists := (
    SELECT COUNT(*)
    FROM INFORMATION_SCHEMA.REFERENTIAL_CONSTRAINTS
    WHERE CONSTRAINT_SCHEMA = DATABASE()
      AND TABLE_NAME = 'Individual_world_cup_food'
      AND REFERENCED_TABLE_NAME = 'eventFood'
);
SET @sql := IF(@good_fk_exists = 0,
               'ALTER TABLE `Individual_world_cup_food` ADD CONSTRAINT `FK_INDIVIDUAL_FOOD_EVENTFOOD` FOREIGN KEY (`food_no`) REFERENCES `eventFood`(`food_no`)',
               'DO 0');
PREPARE stmt FROM @sql; EXECUTE stmt; DEALLOCATE PREPARE stmt;


-- 3) report 더미데이터 오타 보정: qnd_post -> qna_post
UPDATE `report`
SET `report_source` = 'qna_post'
WHERE `report_source` = 'qnd_post';

-- 4) 비밀번호 암호화 및 member_level 초기화 (기존 데이터 업데이트용)
-- 모든 비밀번호를 "password123!"로 통일하여 암호화
UPDATE member
SET member_pw = '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDi',
    member_level = COALESCE(member_level, 0)
WHERE member_pw NOT LIKE '$2a$%';

-- member_level이 null인 경우 0으로 설정
UPDATE member
SET member_level = 0
WHERE member_level IS NULL;

-- 5) 확인용 쿼리 (실행 후 확인)
-- SELECT member_no, member_email, member_pw, member_level
-- FROM member
-- WHERE member_email IN (
--     'high.holic@example.com',
--     'sake.trip@example.com',
--     'wine.note@example.com',
--     'beer.runner@example.com',
--     'soju.writer@example.com'
-- );

-- 술BTI 뷰테이블 생성(자동계산)
CREATE OR REPLACE VIEW albti_final_result AS
WITH score_union AS (
    SELECT a.member_no, s.type_a AS alBTI_no, COUNT(*) AS score
    FROM albti_answer a
             JOIN albti_survey s ON a.albti_survey_no = s.albti_survey_no
    WHERE a.choice = 'A'
    GROUP BY a.member_no, s.type_a
    UNION ALL
    SELECT a.member_no, s.type_b AS alBTI_no, COUNT(*) AS score
    FROM albti_answer a
             JOIN albti_survey s ON a.albti_survey_no = s.albti_survey_no
    WHERE a.choice = 'B'
    GROUP BY a.member_no, s.type_b
),
     score_sum AS (
         SELECT member_no, alBTI_no, SUM(score) AS total_score
         FROM score_union
         GROUP BY member_no, alBTI_no
     ),
     max_score AS (
         SELECT member_no, MAX(total_score) AS top_score
         FROM score_sum
         GROUP BY member_no
     )
SELECT s.member_no,
       GROUP_CONCAT(a.alBTI_category ORDER BY s.total_score DESC SEPARATOR '/') AS final_type,
       GROUP_CONCAT(a.alBTI_detail ORDER BY s.total_score DESC SEPARATOR '/') AS final_detail,
       s.total_score
FROM score_sum s
         JOIN albti a ON s.alBTI_no = a.alBTI_no
         JOIN max_score m ON s.member_no = m.member_no AND s.total_score = m.top_score
GROUP BY s.member_no;


UPDATE food_post SET confirmed_yn = 'T'
WHERE confirmed_yn IN ('1', 'T', 't', 'Y', 'y', 'TRUE', 'true') OR confirmed_yn = 1;

UPDATE food_post SET confirmed_yn = 'F'
WHERE confirmed_yn IS NULL OR confirmed_yn IN ('0', 'F', 'f', 'N', 'n', 'FALSE', 'false') OR confirmed_yn = 0;

SELECT board_no, confirmed_yn FROM food_post WHERE alcohol_no = 2 ORDER BY board_no DESC;

START TRANSACTION;

-- 0/1, '0'/'1', 'Y'/'N' 등을 모두 TF로 통일

ALTER TABLE food_post
    MODIFY COLUMN confirmed_yn CHAR(1) NOT NULL,
    ADD CONSTRAINT chk_food_post_confirmed_yn CHECK (confirmed_yn IN ('T','F'));