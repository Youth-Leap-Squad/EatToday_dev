package com.eat.today.sns.query.dto.photoReviewComment;

import lombok.Data;

public class PrcDTO {

    @Data
    public static class CreateRequest {
        private int memberNo;
        private String detail;
    }

    @Data
    public static class UpdateRequest {
        private int memberNo;
        private String detail;
    }

    @Data
    public static class DeleteRequest {
        private int memberNo;
    }

    @Data
    public static class CreateResponse {
        private int prcNo;
        public CreateResponse(int prcNo) { this.prcNo = prcNo; }
    }
}
