-- 제일 깊은 자식부터 제거
use mydb;
DROP TABLE IF EXISTS `dm_file_upload`;
DROP TABLE IF EXISTS `photo_review_comment`;
DROP TABLE IF EXISTS `pr_file_upload`;
DROP TABLE IF EXISTS `lounge`;
DROP TABLE IF EXISTS `report_history`;
DROP TABLE IF EXISTS `world_cup_picks`;
DROP TABLE IF EXISTS `bookmark`;
DROP TABLE IF EXISTS `food_post_likes`;
DROP TABLE IF EXISTS `food_comment`;
DROP TABLE IF EXISTS `albti_output`;
DROP TABLE IF EXISTS `albti_join_member`;
DROP TABLE IF EXISTS `individual_world_cup_food`;

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

CREATE TABLE `role` (
                        role_no INT NOT NULL AUTO_INCREMENT COMMENT '권한번호',
                        role_name VARCHAR(255) NOT NULL COMMENT '권한명',
                        CONSTRAINT `PK_role` PRIMARY KEY (role_no)
) ENGINE=INNODB COMMENT '권한 정보';

CREATE TABLE `member` (
                          member_no INT NOT NULL AUTO_INCREMENT COMMENT '회원번호',
                          member_role_no INT NOT NULL COMMENT '권한번호',
                          member_id VARCHAR(255) NOT NULL COMMENT '아이디',
                          member_pw VARCHAR(255) NOT NULL COMMENT '비밀번호',
                          member_name VARCHAR(255) NOT NULL COMMENT '회원명',
                          member_birth VARCHAR(255) NOT NULL COMMENT '생년월일',
                          member_phone VARCHAR(255) NOT NULL COMMENT '핸드폰 번호',
                          member_status VARCHAR(255) NOT NULL DEFAULT 'normal' COMMENT '회원상태',
                          member_active BOOLEAN NOT NULL DEFAULT TRUE COMMENT '회원 활동여부',
                          member_at VARCHAR(255) NOT NULL DEFAULT 0 COMMENT '계정 생성일',
                          member_level INT COMMENT '회원등급',
                          CONSTRAINT PK_member PRIMARY KEY (member_no),
                          CONSTRAINT FK_member_role FOREIGN KEY (member_role_no) REFERENCES role(role_no)
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
                             `food_picture` VARCHAR(255) NULL,
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
                                review_like   INT NOT NULL,
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


CREATE TABLE `follow` (
                          follower_no  INT NOT NULL,
                          following_no INT NOT NULL,
                          created_at   DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
                          CONSTRAINT FK_follow_follower
                              FOREIGN KEY (follower_no)  REFERENCES member(member_no)
                                  ON DELETE CASCADE ON UPDATE CASCADE,
                          CONSTRAINT FK_follow_following
                              FOREIGN KEY (following_no) REFERENCES member(member_no)
                                  ON DELETE CASCADE ON UPDATE CASCADE,
                          KEY idx_follow_following_no (following_no)
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
                            worldcup_finish_date  VARCHAR(255) NOT NULL,
                            worldcup_winning_food VARCHAR(255) NOT NULL,
                            CONSTRAINT PK_WORLD_CUP PRIMARY KEY (worldcup_no)
)ENGINE=INNODB COMMENT '주간월드컵 게임';


CREATE TABLE `eventFood` (
                             `food_no` INT NOT NULL AUTO_INCREMENT,
                             `board_no` INT NOT NULL,
                             `food_content` VARCHAR(255) NOT NULL,
                             `num_of_wins` INT NOT NULL DEFAULT 0,
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


CREATE TABLE IF NOT EXISTS `report_history` (
                                                `report_no` INT NOT NULL,
                                                `member_no` INT NOT NULL,
                                                `report_count` INT DEFAULT 0,
                                                CONSTRAINT PK_report_history PRIMARY KEY (report_no, member_no),
    CONSTRAINT FK_report_history_REPORT FOREIGN KEY (report_no) REFERENCES report(report_no)
    ON UPDATE CASCADE ON DELETE CASCADE,
    CONSTRAINT FK_report_history_MEMBER FOREIGN KEY (member_no) REFERENCES member(member_no)
    ON UPDATE CASCADE ON DELETE CASCADE
    ) ENGINE=INNODB COMMENT '게시글 신고이력';



CREATE TABLE bookmark (
                          member_no INT NOT NULL,
                          board_no  INT NOT NULL,
                          PRIMARY KEY (member_no, board_no),
                          CONSTRAINT FK_BOOKMARK_MEMBER     FOREIGN KEY (member_no) REFERENCES member(member_no),
                          CONSTRAINT FK_BOOKMARK_FOOD_POST  FOREIGN KEY (board_no)  REFERENCES food_post(board_no)
) ENGINE=InnoDB COMMENT='즐겨찾기';



CREATE TABLE `albti` (
                         alBTI_no      INT NOT NULL AUTO_INCREMENT,
                         alBTI_category VARCHAR(255) NOT NULL,
                         alBTI_detail   VARCHAR(255) NOT NULL,
                         alcohol_no     INT NOT NULL,
                         CONSTRAINT PK_alBTI PRIMARY KEY (alBTI_no),
                         CONSTRAINT FK_albti_alcohol FOREIGN KEY (alcohol_no) REFERENCES alcohol(alcohol_no)
)ENGINE=INNODB COMMENT '술BTI';



CREATE TABLE `albti_join_member` (
                                     alBIT_member_no INT NOT NULL AUTO_INCREMENT,
                                     member_no       INT NOT NULL,
                                     alBTI_no        INT NOT NULL,
                                     CONSTRAINT PK_albti_join_member PRIMARY KEY (alBIT_member_no),
                                     CONSTRAINT FK_ajm_member FOREIGN KEY (member_no) REFERENCES member(member_no),
                                     CONSTRAINT FK_ajm_albti  FOREIGN KEY (alBTI_no)  REFERENCES albti(alBTI_no)
)ENGINE=INNODB COMMENT '술BTI게임 참여회원';




CREATE TABLE `photo_review_comment` (
                                        pr_no     INT NOT NULL AUTO_INCREMENT,
                                        pr_detail VARCHAR(255) NOT NULL,
                                        pr_at     VARCHAR(255) NOT NULL,
                                        review_no INT NOT NULL,
                                        pr_deleted BOOLEAN NOT NULL,
                                        CONSTRAINT PK_photo_review_comment PRIMARY KEY (pr_no),
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
                                  `recive_member_id` INT NOT NULL,
                                  `dm_content` VARCHAR(255) NOT NULL,
                                  `dm_date` VARCHAR(255) NOT NULL,
                                  `dm_read` BOOLEAN NOT NULL DEFAULT 0 COMMENT '메세지 읽음 여부',
                                  CONSTRAINT PK_MESSAGE_NO PRIMARY KEY (message_no),
                                  CONSTRAINT FK_MESSAGE_NO_MEMBER_NO_3 FOREIGN KEY(send_member_id) REFERENCES member(member_no),
                                  CONSTRAINT FK_MESSAGE_NO_MEMBER_NO_4 FOREIGN KEY(recive_member_id) REFERENCES member(member_no)
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
                                alBTI_no           INT NOT NULL,
                                alBTI_alcohol_explain VARCHAR(255) NOT NULL,
                                board_no           INT NOT NULL,
                                CONSTRAINT PK_albti_output PRIMARY KEY (alBTI_no, board_no),
                                CONSTRAINT FK_albtiout_albti FOREIGN KEY (alBTI_no) REFERENCES albti(alBTI_no),
                                CONSTRAINT FK_albtiout_board FOREIGN KEY (board_no) REFERENCES food_post(board_no)
)ENGINE=INNODB COMMENT '개인별 술BTI 설문 결과(술 출력)';





-- 더미데이터 삽입

-- 권한: ID를 명시적으로 고정
INSERT INTO role (role_no, role_name) VALUES
                                          (1, '관리자'),
                                          (2, '일반회원');

-- 회원: 관리자 계정은 role_no=1, 일반 회원은 role_no=2로 교정 삽입
INSERT INTO `member`
(member_role_no, member_id, member_pw, member_name, member_birth, member_phone, member_status, member_active, member_at, member_level)
VALUES
    (1, 'admin01', 'adminpw!', '관리자', '1988-10-10', '010-1111-1111', 'normal', TRUE, '2025-01-05',NULL),
    (2, 'soju_love', 'drinkpw1!', '박철수', '1995-07-07', '010-2222-2222', 'normal', TRUE, '2025-03-11', 200),
    (2, 'beer_queen', 'beerpw2!', '김민지', '1998-11-23', '010-3333-3333', 'suspended', FALSE, '2025-04-02', 300),
    (2, 'wine_master', 'winepw3!', '최영희', '1990-02-17', '010-4444-4444', 'normal', TRUE, '2025-05-20', 220),
    (2, 'makgeolli', 'makpw4!', '정우성', '1993-12-30', '010-5555-5555', 'normal', TRUE, '2025-06-01', 60),
    (2, 'cocktail_girl', 'cockpw5!', '한지민', '2000-09-09', '010-6666-6666', 'normal', TRUE, '2025-07-18', 50),
    (2, 'sake_lover', 'sakpw6!', '오다유키', '1994-01-25', '010-7777-7777', 'normal', TRUE, '2025-08-03', 340),
    (2, 'champagne_boy', 'champpw7!', '박상혁', '1992-04-19', '010-8888-8888', 'normal', TRUE, '2025-08-25', 200),
    (2, 'highballer', 'highpw8!', '이진우', '1999-07-15', '010-9999-9999', 'normal', TRUE, '2025-09-01', 50),
    (2, 'vodka_star', 'vodkapw9!', '안지수', '1997-12-12', '010-1010-1010', 'normal', TRUE, '2025-09-05', 50),
    (2, 'gin_tonic', 'ginpw10!', '서민호', '1996-06-30', '010-1111-2222', 'normal', TRUE, '2025-09-07', 50),
    (2, 'whisky_time', 'whiskypw11!', '김성훈', '1989-08-21', '010-2222-3333', 'withdrawn', FALSE, '2025-09-10', 500),
    (2, 'rum_rider', 'rumpw12!', '홍길동', '1991-09-15', '010-3333-4444', 'normal', TRUE, '2025-09-11', 50),
    (2, 'tequila99', 'teqpw13!', '최다혜', '1998-05-22', '010-4444-5555', 'normal', TRUE, '2025-09-12', 60),
    (2, 'soju_kim', 'sojupw14!', '김철민', '1995-11-30', '010-5555-6666', 'normal', TRUE, '2025-09-13',340),
    (2, 'beer_lee', 'beerpw15!', '이수진', '1993-03-18', '010-6666-7777', 'suspended', FALSE, '2025-09-14', 200),
    (2, 'wine_park', 'winepw16!', '박지영', '1990-01-07', '010-7777-8888', 'normal', TRUE, '2025-09-15', 200),
    (1, 'admin02', 'adminpw2!', '서관리', '1985-06-05', '010-8888-9999', 'normal', TRUE, '2025-09-16', NULL),
    (2, 'bbq_master', 'pw1!', '이서준', '1994-02-14', '010-1212-1212', 'normal', TRUE, '2025-09-16', 150),
    (2, 'sool_scholar', 'pw2!', '김하늘', '1997-03-01', '010-1313-1313', 'normal', TRUE, '2025-09-16', 140),
    (2, 'wine_beginner', 'pw3!', '최다연', '2001-09-09', '010-1414-1414', 'normal', TRUE, '2025-09-17', 200),
    (2, 'beer_coder', 'pw4!', '박도윤', '1996-05-22', '010-1515-1515', 'normal', TRUE, '2025-09-17', 340),
    (2, 'mak_fan', 'pw5!', '정윤아', '1999-08-30', '010-1616-1616', 'normal', TRUE, '2025-09-17', 340),
    (2, 'high_holic', 'pw6!', '오세준', '1995-12-02', '010-1717-1717', 'normal', TRUE, '2025-09-18', 200),
    (2, 'sake_trip', 'pw7!', '장유나', '1998-01-28', '010-1818-1818', 'normal', TRUE, '2025-09-18', 340),
    (2, 'wine_note', 'pw8!', '신태훈', '1992-06-06', '010-1919-1919', 'normal', TRUE, '2025-09-18', 700),
    (2, 'beer_runner', 'pw9!', '한유진', '1997-11-11', '010-2020-2020', 'normal', TRUE, '2025-09-19', 200),
    (2, 'soju_writer', 'pw10!', '노수현', '1993-07-17', '010-2121-2121', 'normal', TRUE, '2025-09-19', 800);

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

INSERT INTO food_post (alcohol_no, board_title, board_content, food_explain, member_no, food_picture, board_date, board_seq, confirmed_yn, likes_no_1, likes_no_2, likes_no_3, likes_no_4)
VALUES
    (1, '치킨과 함께하는 시원한 맥주', '더위를 날려주는 치맥 조합', '바삭한 치킨과 시원한 맥주의 환상 궁합', 2, '/images/food/chicken_beer.jpg', '2025-09-01', 1, TRUE, 12, 4, 20, 2),
    (2, '삼겹살과 소주의 조합', '한국인의 영원한 소울푸드', '기름진 삼겹살과 깔끔한 소주', 3, '/images/food/samgyeopsal_soju.jpg', '2025-09-02', 2, TRUE, 18, 6, 22, 5),
    (8, '스테이크와 레드 와인', '분위기 있는 저녁 한 끼', '육즙 가득한 스테이크와 와인의 향연', 4, '/images/food/steak_wine.jpg', '2025-09-03', 3, TRUE, 5, 23, 4, 2),
    (3, '파전과 막걸리', '비 오는 날의 낭만', '고소한 파전과 구수한 막걸리', 5, '/images/food/pajeon_makgeolli.jpg', '2025-09-04', 4, TRUE, 4, 33, 1, 10 ),
    (1,'피자와 라거','탄산 톡톡 라거와 고소한 피자','치즈 풍미UP', 2, '/images/food/pizza_lager.jpg','2025-09-05',5, TRUE, 1, 13, 8, 4),
    (8,'치즈 플래터와 화이트 와인','가벼운 산도와 고소함','브리/고다/크래커', 4, '/images/food/cheese_white.jpg','2025-09-06',6, TRUE, 10, 3, 4, 9),
    (2,'골뱅이소면과 소주','매콤새콤에 소주 한 잔','국물까지 완벽', 5, '/images/food/gol_soju.jpg','2025-09-07',7, TRUE, 4, 6, 19, 2),
    (7,'감자튀김과 하이볼','짭짤바삭+탄산감','맥주대신 하이볼', 9, '/images/food/fries_highball.jpg','2025-09-08',8, TRUE, 18, 2, 5, 8),
    (9,'훈제치즈와 싱글몰트','스모키 매칭','은은한 피트향과 조화', 11, '/images/food/smoked_malt.jpg','2025-09-09',9, TRUE, 7, 9, 1, 8),
    (3,'해물파전과 막걸리','역시 비오는 날 정답','파/오징어 듬뿍', 5, '/images/food/haemul_mak.jpg','2025-09-10',10, TRUE, 1, 3, 5, 16);

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

INSERT INTO direct_message (send_member_id, recive_member_id, dm_content, dm_date, dm_read)
VALUES
    (2, 3, '이번 주말에 치맥 어때?', '2025-09-05', FALSE),
    (3, 2, '좋아! 치킨집 예약할게.', '2025-09-05', TRUE),
    (4, 5, '다음에 막걸리 한잔 하자', '2025-09-06', FALSE),
    (2,4,'다음 주에 피자 라거 콜?','2025-09-12', FALSE),
    (4,2,'콜! 치즈 플래터도 가자','2025-09-12', TRUE),
    (5,9,'하이볼집 리스트 공유해줘~','2025-09-13', FALSE),
    (3,2,'주말에 와인바 갈래?','2025-09-14', FALSE),
    (9,5,'막걸리집 추천 있음','2025-09-14', TRUE),
    (11,2,'싱글몰트 테이스팅 하자','2025-09-15', FALSE),
    (2,3,'치킨/양념 vs 후라이드?','2025-09-15', TRUE);

INSERT INTO report (member_no, member_no2, report_title, report_content, report_yn, report_date, report_source)
VALUES
    (3, 2, '부적절한 리뷰', '욕설이 포함된 리뷰 발견', FALSE, '2025-09-08', 'photo_review'),
    (5, 4, '스팸 게시글', '같은 내용 반복 등록', TRUE, '2025-09-09', 'food_post'),
    (2,3,'과도한 홍보','댓글에 광고 링크', FALSE,'2025-09-12','food_comment'),
    (4,5,'도배 의심','반복 리뷰 업로드', TRUE,'2025-09-13','photo_review'),
    (5,2,'비방 표현','상대 비하 표현', FALSE,'2025-09-14','direct_message'),
    (9,5,'허위 정보','사실과 다른 정보', FALSE,'2025-09-14','food_post'),
    (3,4,'부적절 이미지','정책 위반 추정', TRUE,'2025-09-15','photo_review'),
    (11,2,'욕설 신고','DM에서 욕설', TRUE,'2025-09-15','direct_message'),
    (6,7,'스팸 링크','외부 스팸 유도', FALSE,'2025-09-16','qnd_post'),
    (8,9,'저작권 의심','이미지 출처 불명', FALSE,'2025-09-16','photo_review');

INSERT INTO worldcup (worldcup_start_date, worldcup_finish_date, worldcup_winning_food)
VALUES
    ('2025-09-01', '2025-09-07', '치킨과 맥주'),
    ('2025-09-08', '2025-09-14', '삼겹살과 소주');

INSERT INTO eventFood (board_no, food_content, num_of_wins)
VALUES
    (1, '치킨', 3),
    (2, '삼겹살', 5),
    (3, '스테이크', 2),
    (4, '파전', 4);

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
INSERT INTO photo_review_comment (pr_detail, pr_at, review_no, pr_deleted)
VALUES
    ('치킨 바삭함 인정', '2025-09-05', 1, FALSE),
    ('역시 소주엔 삼겹살', '2025-09-06', 2, FALSE),
    ('레드와인 향 좋았어요', '2025-09-07', 3, FALSE);

-- 2) 사진 리뷰 파일 업로드
INSERT INTO pr_file_upload (pr_file_name, pr_file_type, pr_file_rename, pr_file_path, pr_file_at, review_no)
VALUES
    ('chicken.jpg', 'image/jpeg', 'chicken_1.jpg', '/uploads/review/1', '2025-09-05', 1),
    ('samgyeopsal.jpg', 'image/jpeg', 'sam_1.jpg', '/uploads/review/2', '2025-09-06', 2);

-- 3) 라운지(리뷰-술 매핑)
--   리뷰1=맥주(1), 리뷰2=소주(2), 리뷰3=와인(8)
INSERT INTO lounge (review_no, alcohol_no)
VALUES
    (1, 1),
    (2, 2),
    (3, 8);

-- 4) DM 파일 업로드
INSERT INTO dm_file_upload (dm_file_name, dm_file_type, dm_file_rename, dm_file_path, dm_file_at, dm_key)
VALUES
    ('menu.png', 'image/png', 'menu_20250905.png', '/uploads/dm/1', '2025-09-05', 1);

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
INSERT INTO qna_post (member_no, inquiry_content, inquiry_at)
VALUES
    (2, '사진 리뷰가 안 올라가요', '2025-09-12'),
    (3, '월드컵 일정이 궁금합니다', '2025-09-13');

-- 10) 월드컵-술 매핑 (worldcup_alcohol)
--     worldcup 1: 맥주(1), 와인(8)
--     worldcup 2: 소주(2), 하이볼(7)
--     일관 참조를 위해 worldcup_alcohol_no를 명시적으로 고정
INSERT INTO worldcup_alcohol (worldcup_alcohol_no, alcohol_no, worldcup_no)
VALUES
    (1, 1, 1),
    (2, 8, 1),
    (3, 2, 2),
    (4, 7, 2);

-- 11) 월드컵 참여 회원 (worldcup_join_member)
--     ID를 고정적으로 지정해 아래 picks에서 참조
INSERT INTO worldcup_join_member (worldcup_join_member_no, worldcup_no, member_no)
VALUES
    (1, 1, 2),
    (2, 1, 3),
    (3, 1, 4),
    (4, 2, 5),
    (5, 2, 2);

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

-- 14) 술BTI 참여 (albti_join_member)
INSERT INTO albti_join_member (alBIT_member_no, member_no, alBTI_no)
VALUES
    (1, 2, 1),  -- soju_love -> 활발함
    (2, 3, 2),  -- beer_queen -> 차분함
    (3, 4, 3),  -- wine_master -> 전통파
    (4, 5, 4);  -- makgeolli -> 도전적

-- 15) 술BTI 결과 출력 매핑 (albti_output)
INSERT INTO albti_output (alBTI_no, alBTI_alcohol_explain, board_no)
VALUES
    (1, '활발한 파티러버 스타일에 어울리는 맥주', 1),
    (2, '차분히 즐길 수 있는 와인 추천', 3),
    (3, '전통 한식과 어울리는 소주 추천', 2),
    (4, '새로운 조합에 도전하는 하이볼 추천', 4);