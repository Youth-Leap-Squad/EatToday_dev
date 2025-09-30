package com.eat.today.member.command.application.controller;

import com.eat.today.member.command.application.dto.*;
import com.eat.today.member.command.application.service.CommandMemberService;
import com.eat.today.member.command.domain.aggregate.MemberEntity;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@ RestController
@Slf4j
public class CommandMemberController {

    private final CommandMemberService commandMemberService;
    private Environment env;
    private CommandMemberService memberService;
    private ModelMapper modelMapper;

    @Autowired
    public CommandMemberController(Environment env,
                                   CommandMemberService memberService,
                                   ModelMapper modelMapper, CommandMemberService commandMemberService) {
        this.env = env;
        this.memberService = memberService;
        this.modelMapper = modelMapper;
        this.commandMemberService = commandMemberService;
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

    // 회원 정보 수정
    @PatchMapping("/members/{memberPhone}")
    public ResponseEntity<CommandUpdateMemberDTO> updateMember(
            @PathVariable String memberPhone,
            @RequestBody CommandUpdateMemberDTO req) {

        // PathVariable을 DTO에 세팅
        req.setMemberPhone(memberPhone);

        // 서비스 호출 → 수정된 엔티티 반환
        MemberEntity updatedMember = commandMemberService.updatemember(req);

        // 응답 DTO 매핑
        CommandUpdateMemberDTO response =
                modelMapper.map(updatedMember, CommandUpdateMemberDTO.class);

        return ResponseEntity.ok(response);
    }

    // 회원 탈퇴
    @PostMapping("/members/{memberPhone}/withdraw")
    public ResponseEntity<Void> withdraw(@PathVariable String memberPhone,
                                         @RequestBody WithdrawRequest request) {
        memberService.withdraw(memberPhone, request.getMemberPw());
        return ResponseEntity.noContent().build();
    }


}
