package com.eat.today.qna_rounge_report.rounge.query.service;

import com.eat.today.qna_rounge_report.rounge.query.dto.PhotoReviewDTO;
import com.eat.today.qna_rounge_report.rounge.query.repository.PhotoReviewMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PhotoReviewServiceImpl implements PhotoReviewService {

    private final PhotoReviewMapper photoReviewMapper;

    public PhotoReviewServiceImpl(PhotoReviewMapper photoReviewMapper) {
        this.photoReviewMapper = photoReviewMapper;
    }

    @Override
    public List<PhotoReviewDTO> getAllByDateDesc() {
        return photoReviewMapper.selectAllOrderByDateDesc();
    }

    @Override
    public List<PhotoReviewDTO> getAllByLikeDesc() {
        return photoReviewMapper.selectAllOrderByLikeDesc();
    }

    @Override
    public List<PhotoReviewDTO> search(String keyword) {
        return photoReviewMapper.searchByKeyword(keyword);
    }

    @Override
    public List<PhotoReviewDTO> getByAlcoholNo(int alcoholNo) {
        return photoReviewMapper.selectByAlcoholNo(alcoholNo);
    }
}
