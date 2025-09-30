package com.eat.today.sns.query.dto.photoReviewComment;

import lombok.Data;

public class PrcDTO {

    /** 댓글 작성 요청(JSON): { "memberNo": 1, "detail": "..." } */
    @Data
    public static class CreateRequest {
        private int memberNo;
        private String detail;
    }

    /** 댓글 수정 요청(JSON): { "memberNo": 1, "detail": "..." } */
    @Data
    public static class UpdateRequest {
        private int memberNo;
        private String detail;
    }

    /** 댓글 삭제 요청(JSON): { "memberNo": 1 } */
    @Data
    public static class DeleteRequest {
        private int memberNo;
    }

    /** 작성 응답(JSON): { "prcNo": 123 } */
    @Data
    public static class CreateResponse {
        private int prcNo;
        public CreateResponse(int prcNo) { this.prcNo = prcNo; }
    }
}
