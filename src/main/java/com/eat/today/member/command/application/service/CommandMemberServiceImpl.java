package com.eat.today.member.command.application.service;

import com.eat.today.member.command.application.dto.CommandMemberDTO;
import com.eat.today.member.command.application.dto.CommandUpdateMemberDTO;
import com.eat.today.member.command.domain.aggregate.MemberEntity;

import com.eat.today.member.command.domain.repository.MemberRepository;

import jakarta.transaction.Transactional;
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
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
@Transactional
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
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        // 이메일과 일치하는 이메일을 가진 회원을 조회해서 MemberEntity로 변환(조회) 받음
        // 여러 결과가 있을 경우 첫 번째만 가져오기
        List<MemberEntity> members = memberRepository.findByMemberEmail(email);
        MemberEntity loginMember = null;

        if (members != null && !members.isEmpty()) {
            loginMember = members.get(0); // 첫 번째 결과만 사용
        }

        // 회원이 로그인 시 아이디(이메일)를 잘못 입력 했다면
        if(loginMember==null){
            throw new UsernameNotFoundException(email + " 이메일을 가진 사용자는 존재하지 않습니다. 다시 입력하세요. ");
        }

        // DB에서 조회된 해당 이메일을 가진 회원이 가진 권한을 가져와 List<GrantedAuthority>로 전환
        List<GrantedAuthority> authorities = new ArrayList<>();
        if (loginMember.getMemberRole() == MemberEntity.Role.ADMIN) {
            authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
        } else {
            authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
        }

        return new User(
                loginMember.getMemberEmail(), loginMember.getMemberPw(),
                true, true, true, true, authorities
        );
    }

    @Override
    public void updatemember(CommandUpdateMemberDTO dto) {
        if (dto == null || dto.getMemberEmail() == null) {
            throw new IllegalArgumentException("이메일은 필수입니다. ");
        }

        List<MemberEntity> members = memberRepository.findByMemberEmail(dto.getMemberEmail());
        if (members == null || members.isEmpty()) {
            throw new IllegalArgumentException("해당 이메일의 회원을 찾을 수 없습니다: " + dto.getMemberEmail());
        }
        MemberEntity member = members.get(0);

        boolean changed = false;

        // 이름
        if (notBlank(dto.getMemberName()) && !dto.getMemberName().equals(member.getMemberName())) {
            member.setMemberName(dto.getMemberName());
            changed = true;
        }

        // 생년월일 변경
        if (notBlank(dto.getMemberBirth()) && !dto.getMemberBirth().equals(member.getMemberBirth())) {
            member.setMemberBirth(dto.getMemberBirth());
            changed = true;
        }

        // 4. 활성/비활성 상태 변경
        if (dto.getMemberActive() != null && !dto.getMemberActive().equals(member.isMemberActive())) {
            member.setMemberActive(dto.getMemberActive());
            changed = true;
        }


        // 5. 변경된 게 없으면?
        if (!changed) {
            // throw new IllegalArgumentException("수정할 값이 없습니다."); // 필요 시 예외
        }
        // JPA 더티체킹으로 트랜잭션 종료 시 UPDATE 쿼리 자동 반영
    }

    private boolean notBlank(String s) {
        return s != null && !s.isBlank();
    }

    @Override
    public void withdraw(String memberEmail, String rawPw) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'withdraw'");
    }
}



