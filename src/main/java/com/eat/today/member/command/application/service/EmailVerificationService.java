package com.eat.today.member.command.application.service;

import com.eat.today.member.command.domain.aggregate.EmailVerificationEntity;
import com.eat.today.member.command.domain.repository.EmailVerificationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class EmailVerificationService {

    private final EmailVerificationRepository emailVerificationRepository;
    private final EmailService emailService;

    /**
     * 이메일 인증 토큰 생성 및 발송
     */
    public String createAndSendVerificationEmail(String email) {
        // 기존 토큰 삭제
        emailVerificationRepository.deleteByEmail(email);

        // 새 토큰 생성
        String token = UUID.randomUUID().toString();
        
        // 6자리 인증 코드 생성
        String verificationCode = generateVerificationCode();
        
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime expiresAt = now.plusMinutes(30); // 10분 후 만료

        EmailVerificationEntity verification = new EmailVerificationEntity();
        verification.setToken(token);
        verification.setEmail(email);
        verification.setVerificationCode(verificationCode);
        verification.setCreatedAt(now);
        verification.setExpiresAt(expiresAt);
        verification.setVerified(false);
        verification.setType(EmailVerificationEntity.VerificationType.EMAIL_VERIFICATION);

        emailVerificationRepository.save(verification);

        // 이메일 발송 (인증 코드 포함)
        emailService.sendVerificationEmail(email, verificationCode);

        log.info("이메일 인증 코드 생성 및 발송 완료: {} (코드: {})", email, verificationCode);
        return token;
    }

    /**
     * 6자리 랜덤 인증 코드 생성
     */
    private String generateVerificationCode() {
        Random random = new Random();
        int code = 100000 + random.nextInt(900000); // 100000 ~ 999999
        return String.valueOf(code);
    }

    /**
     * 이메일 인증 확인 (토큰 방식 - 레거시)
     */
    public boolean verifyEmail(String token) {
        Optional<EmailVerificationEntity> verificationOpt = emailVerificationRepository.findByToken(token);
        
        if (verificationOpt.isEmpty()) {
            log.warn("유효하지 않은 인증 토큰: {}", token);
            return false;
        }

        EmailVerificationEntity verification = verificationOpt.get();
        
        if (!verification.isValid()) {
            log.warn("만료되었거나 이미 사용된 토큰: {}", token);
            return false;
        }

        // 인증 완료 처리
        verification.setVerified(true);
        emailVerificationRepository.save(verification);

        log.info("이메일 인증 완료: {}", verification.getEmail());
        return true;
    }

    /**
     * 이메일과 인증 코드로 인증 확인 (새로운 방식)
     */
    public boolean verifyEmailWithCode(String email, String code) {
        Optional<EmailVerificationEntity> verificationOpt = 
                emailVerificationRepository.findFirstByEmailAndTypeOrderByCreatedAtDesc(
                        email, EmailVerificationEntity.VerificationType.EMAIL_VERIFICATION);
        
        if (verificationOpt.isEmpty()) {
            log.warn("이메일에 대한 인증 정보가 없습니다: {}", email);
            return false;
        }

        EmailVerificationEntity verification = verificationOpt.get();
        
        // 만료 확인
        if (!verification.isValid()) {
            log.warn("만료되었거나 이미 사용된 인증 코드: {}", email);
            return false;
        }

        // 코드 일치 확인
        if (!verification.getVerificationCode().equals(code)) {
            log.warn("인증 코드 불일치: {} (입력: {})", email, code);
            return false;
        }

        // 인증 완료 처리
        verification.setVerified(true);
        emailVerificationRepository.save(verification);

        log.info("이메일 인증 완료: {} (코드: {})", email, code);
        return true;
    }

    /**
     * 이메일 인증 상태 확인
     */
    public boolean isEmailVerified(String email) {
        Optional<EmailVerificationEntity> verificationOpt = 
                emailVerificationRepository.findFirstByEmailAndTypeOrderByCreatedAtDesc(
                        email, EmailVerificationEntity.VerificationType.EMAIL_VERIFICATION);
        
        return verificationOpt.isPresent() && verificationOpt.get().isVerified();
    }

    /**
     * 비밀번호 재설정 토큰 생성 및 발송
     */
    public String createAndSendPasswordResetEmail(String email) {
        // 기존 토큰 삭제
        emailVerificationRepository.deleteByEmail(email);

        // 새 토큰 생성
        String token = UUID.randomUUID().toString();
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime expiresAt = now.plusHours(1); // 1시간 후 만료

        EmailVerificationEntity verification = new EmailVerificationEntity();
        verification.setToken(token);
        verification.setEmail(email);
        verification.setCreatedAt(now);
        verification.setExpiresAt(expiresAt);
        verification.setVerified(false);
        verification.setType(EmailVerificationEntity.VerificationType.PASSWORD_RESET);

        emailVerificationRepository.save(verification);

        // 이메일 발송
        emailService.sendPasswordResetEmail(email, token);

        log.info("비밀번호 재설정 토큰 생성 및 발송 완료: {}", email);
        return token;
    }

    /**
     * 비밀번호 재설정 토큰 검증
     */
    public boolean verifyPasswordResetToken(String token) {
        Optional<EmailVerificationEntity> verificationOpt = emailVerificationRepository.findByToken(token);
        
        if (verificationOpt.isEmpty()) {
            return false;
        }

        EmailVerificationEntity verification = verificationOpt.get();
        return verification.getType() == EmailVerificationEntity.VerificationType.PASSWORD_RESET 
                && verification.isValid();
    }

    /**
     * 토큰으로 이메일 조회
     */
    public Optional<String> getEmailByToken(String token) {
        return emailVerificationRepository.findByToken(token)
                .map(EmailVerificationEntity::getEmail);
    }
}
