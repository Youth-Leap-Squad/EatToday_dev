package com.eat.today.sns.query.dto.photoReviewComment;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public class PrcDTO {

    /* ---------------------- Create ---------------------- */
    @Data
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class CreateRequest {
        private Integer memberNo;

        @JsonAlias({"content", "detail"}) // 프론트가 content로 보내도 매핑됨
        private String prcDetail;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class CreateResponse {
        private Integer prcNo;
    }

    /* ---------------------- Update ---------------------- */
    @Data
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class UpdateRequest {
        private Integer memberNo;

        @JsonAlias({"content", "detail"}) // 프론트에서 content로 보내도 OK
        private String prcDetail;
    }

    /* ---------------------- 조회 DTO ---------------------- */
    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class CommentDTO {
        private Integer prcNo;
        private Integer memberNo;
        private Integer reviewNo;
        private String prcDetail;
        private String prcAt;
        private Boolean prcDeleted;
        private String memberEmail; // optional (join 시 표시용)
    }
}
