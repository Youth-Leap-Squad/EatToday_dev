package com.eat.today.member.command.domain.repository;

import com.eat.today.member.command.domain.aggregate.EmailVerificationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EmailVerificationRepository extends JpaRepository<EmailVerificationEntity, Long> {

    /**
     * 토큰으로 이메일 인증 정보 조회
     */
    Optional<EmailVerificationEntity> findByToken(String token);

    /**
     * 이메일로 가장 최근의 인증 정보 조회
     */
    Optional<EmailVerificationEntity> findFirstByEmailOrderByCreatedAtDesc(String email);

    /**
     * 이메일과 타입으로 가장 최근의 인증 정보 조회
     */
    Optional<EmailVerificationEntity> findFirstByEmailAndTypeOrderByCreatedAtDesc(
            String email, EmailVerificationEntity.VerificationType type);

    /**
     * 이메일로 모든 인증 정보 삭제
     */
    void deleteByEmail(String email);
}
