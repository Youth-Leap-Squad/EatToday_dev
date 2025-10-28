package com.eat.today.configure.security;

import com.eat.today.member.command.application.service.CommandMemberService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
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
    private final JwtTokenService jwtTokenService;

    @Autowired
    public JwtAuthenticationProvider(CommandMemberService commandMemberService,
                                     PasswordEncoder passwordEncoder,
                                     JwtTokenService jwtTokenService) {
        this.commandMemberService = commandMemberService;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenService = jwtTokenService;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        // 1) 로그인 폼 (username/password)
        if (authentication instanceof UsernamePasswordAuthenticationToken up) {
            String username = (String) up.getPrincipal();
            String rawPw = (String) up.getCredentials();

            UserDetails user = commandMemberService.loadUserByUsername(username);
            if (user == null) throw new UsernameNotFoundException("No user: " + username);
            if (!passwordEncoder.matches(rawPw, user.getPassword()))
                throw new BadCredentialsException("비밀번호가 일치하지 않습니다.");

            return new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
        }

        // 2) API Bearer JWT
        if (authentication instanceof JwtPreAuthenticatedToken jwtAuth) {
            String token = jwtAuth.getToken();

            JwtTokenService.JwtPayload payload = jwtTokenService.parseAndValidate(token);
            if (payload.username() == null || payload.username().isBlank()) {
                throw new BadCredentialsException("Token missing subject");
            }
            UserDetails user = commandMemberService.loadUserByUsername(payload.username());
            if (user == null) throw new UsernameNotFoundException("No user: " + payload.username());

            List<GrantedAuthority> authorities = jwtTokenService.toGrantedAuthorities(payload.roles());
            // DB 권한과 병합하고 싶다면 아래 주석을 사용
            // Set<GrantedAuthority> merged = new HashSet<>(authorities);
            // merged.addAll(user.getAuthorities());
            return new UsernamePasswordAuthenticationToken(user, null, authorities.isEmpty() ? user.getAuthorities() : authorities);
        }

        throw new ProviderNotFoundException("Unsupported auth type: " + authentication.getClass());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication)
                || JwtPreAuthenticatedToken.class.isAssignableFrom(authentication);
    }
}
