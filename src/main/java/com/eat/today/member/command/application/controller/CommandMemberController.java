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
}
