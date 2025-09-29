package com.eat.today.member.command.application.service;

import com.eat.today.configure.MemberRole;
import com.eat.today.member.command.application.dto.CommandMemberDTO;
import com.eat.today.member.command.domain.aggregate.MemberEntity;

import com.eat.today.member.command.domain.repository.MemberRepository;

import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public class CommandMemberServiceImpl implements CommandMemberService, UserDetailsService {

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
        memberEntity.setMemberPw(bCryptPasswordEncoder.encode(commandMemberDTO.getMemberPw()));

        memberEntity.setMemberRole(MemberEntity.Role.USER);

        memberRepository.save(memberEntity);
    }


    @Override
    public UserDetails loadUserByUsername(String phone) throws UsernameNotFoundException {

        // 폰 번호와 일치하는 폰을 가진 회원을 조회해서 MemberEntity로 변환(조회) 받음
        // 여러 결과가 있을 경우 첫 번째만 가져오기
        List<MemberEntity> members = memberRepository.findByMemberPhone(phone);
        MemberEntity loginMember = null;
        
        if (members != null && !members.isEmpty()) {
            loginMember = members.get(0); // 첫 번째 결과만 사용
        }

        // 회원이 로그인 시 아이디(폰 번호)를 잘못 입력 했다면
        if(loginMember==null){
            throw new UsernameNotFoundException(phone + "이 폰 번호를 가진 사용자는 존재하지 않습니다. 다시 입력하세요. ");
        }

        // DB에서 조회된 해당 폰 번호를 가진 회원이 가진 권한을 가져와 List<GrantedAuthority>로 전환
        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
        grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_USER"));

        return new User(loginMember.getMemberPhone(), loginMember.getMemberPw(),
                true,true,true,true,grantedAuthorities);
    }


}
