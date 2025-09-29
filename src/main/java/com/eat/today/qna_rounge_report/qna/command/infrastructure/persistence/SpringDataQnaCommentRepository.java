package com.eat.today.qna_rounge_report.qna.command.infrastructure.persistence;

import com.eat.today.qna_rounge_report.qna.command.domain.aggregate.QnaComment;
import org.springframework.data.jpa.repository.JpaRepository;

interface SpringDataQnaCommentRepository extends JpaRepository<QnaComment, Integer> {

}