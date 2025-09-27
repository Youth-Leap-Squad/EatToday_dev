package com.eat.today.qna_rounge_report.qna.command.infrastructure.persistence;


import com.eat.today.qna_rounge_report.qna.command.domain.aggregate.QnaPost;
import org.springframework.data.jpa.repository.JpaRepository;

interface SpringDataQnaPostRepository extends JpaRepository<QnaPost, Integer> {

}
