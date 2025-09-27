package com.eat.today.member.command.application.service;

import com.eat.today.member.command.application.dto.CommandMemberDTO;
import com.eat.today.member.command.domain.aggregate.MemberEntity;
import com.eat.today.member.command.domain.repository.MemberRepository;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Slf4j
public class CommandMemberServiceImpl implements CommandMemberService {

    private final ModelMapper modelMapper;
    MemberRepository memberRepository;
    BCryptPasswordEncoder bCryptPasswordEncoder;

    public CommandMemberServiceImpl(MemberRepository memberRepository,
                                    ModelMapper modelMapper,
                                    BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.memberRepository = memberRepository;
        this.modelMapper = modelMapper;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Override
    public void registMember(CommandMemberDTO commandMemberDTO) {

        // 아이디 생성
        commandMemberDTO.setMemberId(UUID.randomUUID().toString());   //랜덤 식별자 -> 나중에 사용자 입력값으로 변경

        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        MemberEntity memberEntity = modelMapper.map(commandMemberDTO, MemberEntity.class);
        log.info("Service 계층에서 DTO ->Entity:{}", memberEntity);

        // MemberDTO로 넘어온 사용자 암호(평문)을 BCrypt 암호화 해서 MemberEntity에 전달
        memberEntity.setEncryptPwd(bCryptPasswordEncoder.encode(commandMemberDTO.getMemberPw()));

         
        memberRepository.save(memberEntity);
    }
}
