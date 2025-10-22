package com.eat.today.qna_rounge_report.rounge.query.repository;

import com.eat.today.qna_rounge_report.rounge.query.dto.PhotoReviewDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface PhotoReviewMapper {

    List<PhotoReviewDTO> selectAllOrderByDateDescPaged(@Param("offset") int offset,
                                                       @Param("limit") int limit);
    long countAll();

    List<PhotoReviewDTO> selectAllOrderByLikeDescPaged(@Param("offset") int offset,
                                                       @Param("limit") int limit);
    long countAllByLike();

    List<PhotoReviewDTO> searchByKeywordPaged(@Param("keyword") String keyword,
                                              @Param("offset") int offset,
                                              @Param("limit") int limit);
    long countByKeyword(@Param("keyword") String keyword);

    List<PhotoReviewDTO> selectByAlcoholNoPaged(@Param("alcoholNo") int alcoholNo,
                                                @Param("offset") int offset,
                                                @Param("limit") int limit);

    List<PhotoReviewDTO> selectByAlcoholNoLikeDescPaged(@Param("alcoholNo") int alcoholNo,
                                                        @Param("offset") int offset,
                                                        @Param("limit") int size);
    long countByAlcoholNo(@Param("alcoholNo") int alcoholNo);

    List<PhotoReviewDTO> selectByMemberNoPaged(@Param("memberNo") int memberNo,
                                               @Param("offset") int offset,
                                               @Param("limit") int limit);
    long countByMemberNo(@Param("memberNo") int memberNo);

}