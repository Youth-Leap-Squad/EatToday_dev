package com.eat.today.member.command.application.service;

import com.eat.today.member.command.application.dto.CommandMemberDTO;
import com.eat.today.member.command.application.dto.CommandUpdateMemberDTO;
import com.eat.today.member.command.domain.aggregate.MemberEntity;
import com.eat.today.member.command.domain.aggregate.SecessionEntity;
import com.eat.today.member.command.domain.repository.MemberRepository;
import com.eat.today.member.command.domain.repository.SecessionRepository;
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

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
@Transactional
public class CommandMemberServiceImpl implements CommandMemberService, UserDetailsService {

    private final ModelMapper modelMapper;
    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final SecessionRepository secessionRepository;

    public CommandMemberServiceImpl(MemberRepository memberRepository,
                                    ModelMapper modelMapper,
                                    BCryptPasswordEncoder bCryptPasswordEncoder,
                                    SecessionRepository secessionRepository) {
        this.memberRepository = memberRepository;
        this.modelMapper = modelMapper;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.secessionRepository = secessionRepository;
    }

    @Override
    public void registMember(CommandMemberDTO commandMemberDTO) {
        // 랜덤 식별자 (추후 사용자 입력으로 대체 가능)
        commandMemberDTO.setMemberId(UUID.randomUUID().toString());

        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        MemberEntity memberEntity = modelMapper.map(commandMemberDTO, MemberEntity.class);
        log.info("Service 계층에서 DTO ->Entity: {}", memberEntity);

        // 평문 비번 → BCrypt
        memberEntity.setMemberPw(bCryptPasswordEncoder.encode(commandMemberDTO.getMemberPw()));

        // 기본 권한
        memberEntity.setMemberRole(MemberEntity.Role.USER);

        memberRepository.save(memberEntity);
    }

    /** 전화번호로 대상 한 건 선택: 활성회원 우선 → 최신 가입자 */
    private MemberEntity pickOneByPhone(String phone) {
        List<MemberEntity> list = memberRepository
                .findByMemberPhone(phone);

        if (list == null || list.isEmpty()) {
            throw new UsernameNotFoundException(phone + " 이 폰 번호를 가진 사용자는 존재하지 않습니다.");
        }
        return list.get(0);
    }

    @Override
    public UserDetails loadUserByUsername(String phone) throws UsernameNotFoundException {
        MemberEntity loginMember = pickOneByPhone(phone);

        // 권한 매핑: Enum(Role) → ROLE_*
        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        if (loginMember.getMemberRole() != null) {
            grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_" + loginMember.getMemberRole().name()));
        } else {
            grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_USER"));
        }

        // username은 정책에 맞게 phone 사용(필요 시 memberId로 바꿔도 됨)
        return new User(
                loginMember.getMemberPhone(),      // username
                loginMember.getMemberPw(),         // bcrypt pw
                loginMember.isMemberActive(),      // enabled (비활성은 로그인 불가)
                true, true, true,
                grantedAuthorities
        );
    }

    // 회원 정보 수정
    @Override
    public MemberEntity updatemember(CommandUpdateMemberDTO dto) {
        if (dto == null || dto.getMemberPhone() == null) {
            throw new IllegalArgumentException("핸드폰 번호는 필수입니다.");
        }

        MemberEntity member = pickOneByPhone(dto.getMemberPhone());
        boolean changed = false;

        if (notBlank(dto.getMemberName()) && !dto.getMemberName().equals(member.getMemberName())) {
            member.setMemberName(dto.getMemberName());
            changed = true;
        }
        if (notBlank(dto.getMemberBirth()) && !dto.getMemberBirth().equals(member.getMemberBirth())) {
            member.setMemberBirth(dto.getMemberBirth());
            changed = true;
        }
        if (dto.getMemberActive() != null
                && dto.getMemberActive() != member.isMemberActive()) {
            member.setMemberActive(dto.getMemberActive());
            changed = true;
        }

        if (!changed) {
            log.info("업데이트 변경사항 없음 (phone={})", dto.getMemberPhone());
        }
        // @Transactional 더티체킹으로 자동 반영
        return member;
    }

    private boolean notBlank(String s) {
        return s != null && !s.isBlank();
    }

    // 회원 탈퇴
    @Override
    public void withdraw(String memberPhone, String rawPw) {
        MemberEntity member = pickOneByPhone(memberPhone);

        if (!bCryptPasswordEncoder.matches(rawPw, member.getMemberPw())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        // 상태 업데이트
        member.setMemberStatus("withdrawn");
        member.setMemberActive(false);
        memberRepository.save(member); // 명시적 저장(선호 시 생략 가능: 더티체킹)

        // secession 테이블 insert
        SecessionEntity secession = SecessionEntity.builder()
                .member(member)
                .secessionAt(LocalDateTime.now())
                .build();
        secessionRepository.save(secession);
    }
}