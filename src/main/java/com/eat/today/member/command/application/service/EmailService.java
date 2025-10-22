package com.eat.today.member.command.application.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {

    private final JavaMailSender mailSender;

    /**
     * 이메일 인증 메일 발송
     */
    public void sendVerificationEmail(String toEmail, String verificationCode) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(toEmail);
            message.setSubject("[EatToday] 이메일 인증 코드");
            
            message.setText(
                "안녕하세요! EatToday에 가입해주셔서 감사합니다.\n\n" +
                "아래 인증 코드를 입력하여 이메일 인증을 완료해주세요:\n\n" +
                "━━━━━━━━━━━━━━━━━━━━━━\n" +
                "   인증 코드: " + verificationCode + "\n" +
                "━━━━━━━━━━━━━━━━━━━━━━\n\n" +
                "이 인증 코드는 10분 후에 만료됩니다.\n" +
                "본인이 요청하지 않은 경우 이 이메일을 무시해주세요.\n\n" +
                "감사합니다.\n" +
                "EatToday 팀"
            );
            
            mailSender.send(message);
            log.info("인증 이메일 발송 완료: {} (코드: {})", toEmail, verificationCode);
            
        } catch (Exception e) {
            log.error("이메일 발송 실패: {}", toEmail, e);
            throw new RuntimeException("이메일 발송에 실패했습니다.", e);
        }
    }

    /**
     * 비밀번호 재설정 이메일 발송
     */
    public void sendPasswordResetEmail(String toEmail, String resetToken) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(toEmail);
            message.setSubject("[EatToday] 비밀번호 재설정");
            
            String resetUrl = "http://localhost:8080/members/reset-password?token=" + resetToken;
            
            message.setText(
                "안녕하세요! EatToday 비밀번호 재설정 요청입니다.\n\n" +
                "아래 링크를 클릭하여 새로운 비밀번호를 설정해주세요:\n" +
                resetUrl + "\n\n" +
                "이 링크는 1시간 후에 만료됩니다.\n\n" +
                "본인이 요청하지 않은 경우 이 이메일을 무시해주세요.\n\n" +
                "감사합니다.\n" +
                "EatToday 팀"
            );
            
            mailSender.send(message);
            log.info("비밀번호 재설정 이메일 발송 완료: {}", toEmail);
            
        } catch (Exception e) {
            log.error("비밀번호 재설정 이메일 발송 실패: {}", toEmail, e);
            throw new RuntimeException("이메일 발송에 실패했습니다.", e);
        }
    }
}
