package com.eat.today.qna_rounge_report.rounge.query.service;

import com.eat.today.qna_rounge_report.rounge.query.dto.PhotoReviewDTO;
import com.eat.today.qna_rounge_report.rounge.query.dto.PhotoReviewPageResponse;
import com.eat.today.qna_rounge_report.rounge.query.repository.PhotoReviewMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PhotoReviewServiceImpl implements PhotoReviewService {

    private final PhotoReviewMapper mapper;

    public PhotoReviewServiceImpl(PhotoReviewMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public PhotoReviewPageResponse getAllByDateDescPaged(int page, int size) {
        int offset = page * size;
        List<PhotoReviewDTO> list = mapper.selectAllOrderByDateDescPaged(offset, size);
        long total = mapper.countAll();
        int totalPages = (int) Math.ceil((double) total / size);
        boolean hasNext = page + 1 < totalPages;
        return new PhotoReviewPageResponse(list, page, size, total, totalPages, hasNext);
    }

    @Override
    public PhotoReviewPageResponse getAllByLikeDescPaged(int page, int size) {
        int offset = page * size;
        List<PhotoReviewDTO> list = mapper.selectAllOrderByLikeDescPaged(offset, size);
        long total = mapper.countAllByLike();
        int totalPages = (int) Math.ceil((double) total / size);
        boolean hasNext = page + 1 < totalPages;
        return new PhotoReviewPageResponse(list, page, size, total, totalPages, hasNext);
    }

    @Override
    public PhotoReviewPageResponse searchPaged(String keyword, int page, int size) {
        int offset = page * size;
        List<PhotoReviewDTO> list = mapper.searchByKeywordPaged(keyword, offset, size);
        long total = mapper.countByKeyword(keyword);
        int totalPages = (int) Math.ceil((double) total / size);
        boolean hasNext = page + 1 < totalPages;
        return new PhotoReviewPageResponse(list, page, size, total, totalPages, hasNext);
    }

    @Override
    public PhotoReviewPageResponse getByAlcoholNoPaged(int alcoholNo, int page, int size) {
        int offset = page * size;
        List<PhotoReviewDTO> list = mapper.selectByAlcoholNoPaged(alcoholNo, offset, size);
        long total = mapper.countByAlcoholNo(alcoholNo);
        int totalPages = (int) Math.ceil((double) total / size);
        boolean hasNext = page + 1 < totalPages;
        return new PhotoReviewPageResponse(list, page, size, total, totalPages, hasNext);
    }

    @Override
    public PhotoReviewPageResponse getByMemberNoPaged(int memberNo, int page, int size) {
        int offset = page * size;
        List<PhotoReviewDTO> list = mapper.selectByMemberNoPaged(memberNo, offset, size);
        long total = mapper.countByMemberNo(memberNo);
        int totalPages = (int) Math.ceil((double) total / size);
        boolean hasNext = page + 1 < totalPages;
        return new PhotoReviewPageResponse(list, page, size, total, totalPages, hasNext);
    }
}