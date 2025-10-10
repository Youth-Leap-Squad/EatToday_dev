package com.eat.today.configure.security;

import com.eat.today.member.command.domain.aggregate.MemberEntity;
import com.eat.today.member.command.domain.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // username을 memberId로 사용한다고 가정
        MemberEntity m = memberRepository.findByMemberId(username)
                .orElseThrow(() -> new UsernameNotFoundException("존재하지 않는 회원입니다: " + username));
        return new CustomUserDetails(m);
    }
}
