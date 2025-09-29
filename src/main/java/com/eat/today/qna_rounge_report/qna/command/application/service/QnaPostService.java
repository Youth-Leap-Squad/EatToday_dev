package com.eat.today.qna_rounge_report.qna.command.application.service;


import com.eat.today.qna_rounge_report.qna.command.application.dto.CreateQnaPostRequest;
import com.eat.today.qna_rounge_report.qna.command.application.dto.QnaPostResponse;
import com.eat.today.qna_rounge_report.qna.command.domain.aggregate.QnaPost;
import com.eat.today.qna_rounge_report.qna.command.domain.repository.QnaPostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class QnaPostService {

    private final QnaPostRepository qnaPostRepository;

    // 문의 게시글 생성
    public QnaPostResponse create(CreateQnaPostRequest req) {
        QnaPost saved = qnaPostRepository.save(
                new QnaPost(req.memberNo(), req.inquiryContent(), req.inquiryAt())
        );
        return QnaPostResponse.from(saved);
    }

    // 문의 게시글 수정
    public QnaPostResponse update(Integer id, String newContent) {
        QnaPost post = qnaPostRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("문의사항 없음: " + id));
        post.updateContent(newContent);
        return QnaPostResponse.from(post);
    }

    // 문의 게시글 삭제
    public void delete(Integer id) {
        QnaPost post = qnaPostRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("문의사항 없음: " + id));
        qnaPostRepository.deleteById(id);
    }
}