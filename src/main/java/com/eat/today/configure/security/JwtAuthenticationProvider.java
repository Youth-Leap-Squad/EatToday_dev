package com.eat.today.configure.security;

import com.eat.today.member.command.application.service.CommandMemberService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class JwtAuthenticationProvider implements AuthenticationProvider {

    private final CommandMemberService commandMemberService;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenService jwtTokenService; // 토큰 파싱/검증 전담

    @Autowired
    public JwtAuthenticationProvider(CommandMemberService commandMemberService,
                                     PasswordEncoder passwordEncoder,
                                     JwtTokenService jwtTokenService) { // 주입
        this.commandMemberService = commandMemberService;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenService = jwtTokenService;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        // 1) 로그인(/login) 시도: 아이디/비밀번호
        if (authentication instanceof UsernamePasswordAuthenticationToken up) {
            String username = (String) up.getPrincipal();   // email 또는 phone 중 하나로 통일
            String rawPw    = (String) up.getCredentials();

            UserDetails user = commandMemberService.loadUserByUsername(username);
            if (user == null) throw new UsernameNotFoundException("No user: " + username);
            if (!passwordEncoder.matches(rawPw, user.getPassword()))
                throw new BadCredentialsException("비밀번호가 일치하지 않습니다.");

            return new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
        }

        // 2) API 호출 시도: Bearer JWT
        if (authentication instanceof JwtPreAuthenticatedToken jwtAuth) {
            String token = jwtAuth.getToken();

            JwtTokenService.JwtPayload payload = jwtTokenService.parseAndValidate(token);
            if (payload.username() == null || payload.username().isBlank()) {
                throw new BadCredentialsException("Token missing subject");
            }
            UserDetails user = commandMemberService.loadUserByUsername(payload.username());


            // 권한은 토큰에서 읽거나, DB(=user.getAuthorities())와 병합
            List authorities = jwtTokenService.toGrantedAuthorities(payload.roles());

            return new UsernamePasswordAuthenticationToken(user, null, authorities);
        }

        throw new ProviderNotFoundException("Unsupported auth type: " + authentication.getClass());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication)
                || JwtPreAuthenticatedToken.class.isAssignableFrom(authentication);
    }
}
