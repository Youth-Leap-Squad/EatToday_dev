package com.eat.today.sns.query.service.photoReview;

import com.eat.today.sns.query.dto.photoReview.PRDTO;
import com.eat.today.sns.query.repository.photoReview.PRMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PRQueryService {
    private final PRMapper mapper;

    public PRDTO getDetail(int reviewNo) {
        return mapper.findByReviewNo(reviewNo);
    }

    public List<PRDTO> getListByBoard(int boardNo) {
        return mapper.findByBoardNo(boardNo);
    }

    public List<PRDTO> getListByMember(int memberNo) {
        return mapper.findByMemberNo(memberNo);
    }

    public List<PRDTO> getLatestByBoard(int boardNo) {
        return mapper.findLatestByBoard(boardNo);
    }
}
