package com.eat.today.member.command.application.controller;

import com.eat.today.member.command.application.dto.CommandRequestRegisterMemberDTO;
import com.eat.today.member.command.application.dto.CommandResponseRegisterMemberDTO;
import com.eat.today.member.command.application.dto.CommandMemberDTO;
import com.eat.today.member.command.application.service.CommandMemberService;
import com.eat.today.member.command.application.service.EmailVerificationService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@Slf4j
public class CommandMemberController {

    private Environment env;
    private CommandMemberService memberService;
    private ModelMapper modelMapper;
    private EmailVerificationService emailVerificationService;

    @Autowired
    public CommandMemberController(Environment env,
                                   CommandMemberService memberService,
                                   ModelMapper modelMapper,
                                   EmailVerificationService emailVerificationService) {
        this.env = env;
        this.memberService = memberService;
        this.modelMapper = modelMapper;
        this.emailVerificationService = emailVerificationService;
    }

    @GetMapping("/health")
    public String status() {
        return ("Member Service 에서 작동중" + env.getProperty("spring.profiles.active"));
    }



    // 로그인 기능 전 회원가입 기능
    @PostMapping("/members")
    public ResponseEntity<CommandResponseRegisterMemberDTO> registerMember(@RequestBody CommandRequestRegisterMemberDTO newMember) {
        CommandMemberDTO commandMemberDTO = modelMapper.map(newMember, CommandMemberDTO.class);

        memberService.registMember(commandMemberDTO);

        CommandResponseRegisterMemberDTO responseMember = modelMapper.map(commandMemberDTO, CommandResponseRegisterMemberDTO.class);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseMember);
    }

    // 이메일 인증 확인 (토큰 방식 - 레거시)
    @GetMapping("/members/verify-email")
    public ResponseEntity<Map<String, String>> verifyEmail(@RequestParam String token) {
        try {
            boolean isVerified = emailVerificationService.verifyEmail(token);
            
            if (isVerified) {
                return ResponseEntity.ok(Map.of(
                    "message", "이메일 인증이 완료되었습니다.",
                    "status", "success"
                ));
            } else {
                return ResponseEntity.badRequest().body(Map.of(
                    "message", "유효하지 않거나 만료된 인증 토큰입니다.",
                    "status", "error"
                ));
            }
        } catch (Exception e) {
            log.error("이메일 인증 처리 중 오류 발생", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                "message", "이메일 인증 처리 중 오류가 발생했습니다.",
                "status", "error"
            ));
        }
    }

    // 이메일 인증 코드 확인 (새로운 방식)
    @PostMapping("/members/verify-code")
    public ResponseEntity<Map<String, String>> verifyEmailCode(
            @RequestParam String email,
            @RequestParam String code) {
        try {
            boolean isVerified = emailVerificationService.verifyEmailWithCode(email, code);
            
            if (isVerified) {
                return ResponseEntity.ok(Map.of(
                    "message", "이메일 인증이 완료되었습니다.",
                    "status", "success"
                ));
            } else {
                return ResponseEntity.badRequest().body(Map.of(
                    "message", "유효하지 않거나 만료된 인증 코드입니다.",
                    "status", "error"
                ));
            }
        } catch (Exception e) {
            log.error("이메일 인증 처리 중 오류 발생: {}", email, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                "message", "이메일 인증 처리 중 오류가 발생했습니다.",
                "status", "error"
            ));
        }
    }

    // 이메일 인증 재발송
    @PostMapping("/members/resend-verification")
    public ResponseEntity<Map<String, String>> resendVerificationEmail(@RequestParam String email) {
        try {
            emailVerificationService.createAndSendVerificationEmail(email);
            return ResponseEntity.ok(Map.of(
                "message", "인증 이메일이 재발송되었습니다.",
                "status", "success"
            ));
        } catch (Exception e) {
            log.error("이메일 재발송 중 오류 발생: {}", email, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                "message", "이메일 재발송 중 오류가 발생했습니다.",
                "status", "error"
            ));
        }
    }

    // 비밀번호 변경
    @PostMapping("/members/change-password")
    public ResponseEntity<Map<String, String>> changePassword(
            @RequestParam String currentPassword,
            @RequestParam String newPassword,
            @RequestParam(required = false) String email) { // 테스트용 email 파라미터 추가
        try {
            String memberEmail;
            if (email != null && !email.isEmpty()) {
                // email 파라미터가 제공되면 사용 (테스트용)
                memberEmail = email;
            } else {
                // JWT 토큰에서 이메일 가져오기 (운영용)
                memberEmail = getCurrentUserEmail();
            }
            
            memberService.changePassword(memberEmail, currentPassword, newPassword);
            return ResponseEntity.ok(Map.of(
                "message", "비밀번호가 성공적으로 변경되었습니다.",
                "status", "success"
            ));
        } catch (IllegalArgumentException e) {
            log.error("비밀번호 변경 실패: {}", e.getMessage());
            return ResponseEntity.badRequest().body(Map.of(
                "message", e.getMessage(),
                "status", "error"
            ));
        } catch (Exception e) {
            log.error("비밀번호 변경 중 오류 발생", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                "message", "비밀번호 변경 중 오류가 발생했습니다.",
                "status", "error"
            ));
        }
    }

    // 회원 정보 수정 전 비밀번호 재인증 (로그인된 사용자용)
    @PostMapping("/members/re-auth")
    public ResponseEntity<Map<String, Object>> reAuth(@RequestParam String password) {
        try {
            // TODO: JWT 토큰에서 현재 로그인한 사용자의 이메일 추출
            String currentUserEmail = getCurrentUserEmail();
            
            boolean isValid = memberService.verifyPassword(currentUserEmail, password);
            
            if (isValid) {
                return ResponseEntity.ok(Map.of(
                    "message", "비밀번호 인증 성공",
                    "status", "success",
                    "verified", true
                ));
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of(
                    "message", "비밀번호가 일치하지 않습니다.",
                    "status", "error",
                    "verified", false
                ));
            }
        } catch (Exception e) {
            log.error("비밀번호 재인증 중 오류 발생", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                "message", "인증 처리 중 오류가 발생했습니다.",
                "status", "error",
                "verified", false
            ));
        }
    }
    
    private String getCurrentUserEmail() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        log.info("Authentication: {}", authentication);
        log.info("Authentication Name: {}", authentication != null ? authentication.getName() : "null");
        log.info("Is Authenticated: {}", authentication != null ? authentication.isAuthenticated() : "false");
        
        if (authentication != null && authentication.isAuthenticated()) {
            String email = authentication.getName();
            log.info("Extracted email from SecurityContext: {}", email);
            return email; // 이메일 반환
        }
        
        log.warn("No authenticated user found in SecurityContext");
        throw new IllegalStateException("현재 로그인한 사용자가 없습니다. JWT 토큰을 확인해주세요.");
    }
}
