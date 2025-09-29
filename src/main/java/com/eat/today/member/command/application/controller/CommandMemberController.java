package com.eat.today.member.command.application.controller;

import com.eat.today.member.command.application.dto.CommandRequestRegisterMemberDTO;
import com.eat.today.member.command.application.dto.CommandResponseRegisterMemberDTO;
import com.eat.today.member.command.application.dto.CommandMemberDTO;
import com.eat.today.member.command.application.service.CommandMemberService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@ RestController
@Slf4j
public class CommandMemberController {

    private Environment env;
    private CommandMemberService memberService;
    private ModelMapper modelMapper;

    @Autowired
    public CommandMemberController(Environment env,
                                   CommandMemberService memberService,
                                   ModelMapper modelMapper) {
        this.env = env;
        this.memberService = memberService;
        this.modelMapper = modelMapper;
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
}
