package com.eat.today.sns.command.domain.service;

import com.eat.today.member.command.application.service.MemberPointService;
import com.eat.today.member.command.domain.aggregate.PointPolicy;
import com.eat.today.sns.command.application.dto.photoReviewDTO.CreateRequest;
import com.eat.today.sns.command.application.dto.photoReviewDTO.UpdateRequest;
import com.eat.today.sns.command.application.entity.photoReview.PhotoReviewEntity;
import com.eat.today.sns.command.application.entity.prFileUpload.PrFileUploadEntity;
import com.eat.today.sns.command.domain.repository.photoReview.PhotoReviewRepository;
import com.eat.today.sns.command.domain.repository.prFileUpload.PrFileUploadRepository;
import com.eat.today.sns.command.domain.service.storage.PhotoReviewFileStorage;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class PhotoReviewCommandService {

    private final PhotoReviewRepository repository;
    private final PrFileUploadRepository fileRepo;
    private final PhotoReviewFileStorage storage;
    private final MemberPointService memberPointService;

    /* ---------- CREATE (본문만) : 기존 유지 ---------- */
    @Transactional
    public int create(CreateRequest req) {
        PhotoReviewEntity e = new PhotoReviewEntity();
        e.setBoardNo(req.getBoardNo());
        e.setMemberNo(req.getMemberNo());
        e.setReviewTitle(req.getReviewTitle());
        e.setReviewDate(req.getReviewDate());
        e.setReviewContent(req.getReviewContent());
        e.setReviewLike(0); // 생성 시 0 고정 권장
        PhotoReviewEntity saved = repository.save(e);
        
        // 사진 리뷰 작성 시 포인트 지급
        try {
            memberPointService.grantPoints(req.getMemberNo(), PointPolicy.PHOTO_REVIEW_CREATE);
        } catch (Exception ex) {
            log.error("사진 리뷰 작성 포인트 지급 실패 - 회원번호: {}, 리뷰번호: {}", req.getMemberNo(), saved.getReviewNo(), ex);
        }
        
        return saved.getReviewNo();
    }

    /* ---------- CREATE (본문 + 파일) : 오버로드 ---------- */
    @Transactional
    public int create(CreateRequest req, List<MultipartFile> files) throws IOException {
        PhotoReviewEntity e = new PhotoReviewEntity();
        e.setBoardNo(req.getBoardNo());
        e.setMemberNo(req.getMemberNo());
        e.setReviewTitle(req.getReviewTitle());
        e.setReviewDate(req.getReviewDate());
        e.setReviewContent(req.getReviewContent());
        e.setReviewLike(0);
        e = repository.save(e);

        if (files != null) {
            for (MultipartFile f : files) {
                if (f == null || f.isEmpty()) continue;
                var stored = storage.store(e.getReviewNo(), f); // 물리 저장
                PrFileUploadEntity fe = new PrFileUploadEntity();
                fe.setReview(e);
                fe.setPrFileName(stored.getOriginalName());
                fe.setPrFileRename(stored.getRename());
                fe.setPrFileType(stored.getContentType());
                fe.setPrFilePath(stored.getPublicPath());
                fe.setPrFileAt(stored.getSavedAt());
                fileRepo.save(fe);
            }
        }
        
        // 사진 리뷰 작성 시 포인트 지급
        try {
            memberPointService.grantPoints(req.getMemberNo(), PointPolicy.PHOTO_REVIEW_CREATE);
        } catch (Exception ex) {
            log.error("사진 리뷰 작성 포인트 지급 실패 - 회원번호: {}, 리뷰번호: {}", req.getMemberNo(), e.getReviewNo(), ex);
        }
        
        return e.getReviewNo();
    }

    /* ---------- UPDATE (본문 일부 + 파일 추가/삭제) ---------- */
    @Transactional
    public int editWithFiles(int reviewNo,
                             UpdateRequest reqPatch,
                             List<MultipartFile> addFiles,
                             List<Integer> deleteFileNos) throws IOException {

        PhotoReviewEntity e = repository.findById(reviewNo)
                .orElseThrow(() -> new EntityNotFoundException("review_no=" + reviewNo));

        // 1) 본문 부분 수정
        if (reqPatch != null) {
            if (reqPatch.getBoardNo() != null) e.setBoardNo(reqPatch.getBoardNo());
            if (reqPatch.getMemberNo() != null) e.setMemberNo(reqPatch.getMemberNo());
            if (reqPatch.getReviewTitle() != null) e.setReviewTitle(reqPatch.getReviewTitle());
            if (reqPatch.getReviewDate() != null) e.setReviewDate(reqPatch.getReviewDate());
            if (reqPatch.getReviewContent() != null) e.setReviewContent(reqPatch.getReviewContent());
            if (reqPatch.getReviewLike() != null) e.setReviewLike(reqPatch.getReviewLike());
            repository.save(e);
        }

        // 2) 파일 삭제
        if (deleteFileNos != null && !deleteFileNos.isEmpty()) {
            var files = fileRepo.findAllById(deleteFileNos);
            for (var fe : files) {
                if (fe.getReview().getReviewNo() != reviewNo) continue;
                storage.deletePhysical(fe.getPrFilePath()); // 물리 삭제(실패해도 DB는 삭제)
                fileRepo.delete(fe);
            }
        }

        // 3) 파일 추가
        if (addFiles != null) {
            for (MultipartFile f : addFiles) {
                if (f == null || f.isEmpty()) continue;
                var stored = storage.store(reviewNo, f);
                PrFileUploadEntity fe = new PrFileUploadEntity();
                fe.setReview(e);
                fe.setPrFileName(stored.getOriginalName());
                fe.setPrFileRename(stored.getRename());
                fe.setPrFileType(stored.getContentType());
                fe.setPrFilePath(stored.getPublicPath());
                fe.setPrFileAt(stored.getSavedAt());
                fileRepo.save(fe);
            }
        }

        return 1;
    }

    /* ---------- DELETE (리뷰 + 연관 파일 전체) ---------- */
    @Transactional
    public int delete(int reviewNo) {
        var eOpt = repository.findById(reviewNo);
        if (eOpt.isEmpty()) return 0;

        // 연관 파일들 물리 삭제
        var files = fileRepo.findByReview_ReviewNo(reviewNo);
        for (var fe : files) storage.deletePhysical(fe.getPrFilePath());

        repository.deleteById(reviewNo); // FK ON DELETE CASCADE면 파일 레코드도 함께 삭제
        return 1;
    }

    /* ---------- DELETE (파일) ---------- */
    @Transactional
    public int deleteFile(int reviewNo, int fileNo) {
        var feOpt = fileRepo.findById(fileNo);
        if (feOpt.isEmpty()) return 0;
        var fe = feOpt.get();
        if (fe.getReview().getReviewNo() != reviewNo) return 0;

        storage.deletePhysical(fe.getPrFilePath());
        fileRepo.delete(fe);
        return 1;
    }
}
