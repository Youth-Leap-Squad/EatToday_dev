package com.eat.today.configure.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthorizationFilter extends OncePerRequestFilter {

    private final AuthenticationManager authenticationManager;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain)
            throws ServletException, IOException {

        // 이미 인증된 경우 재인증 생략
        if (SecurityContextHolder.getContext().getAuthentication() == null) {
            String authz = request.getHeader("Authorization");
            if (authz != null && authz.startsWith("Bearer ")) {
                String token = authz.substring(7).trim();
                try {
                    // Provider가 토큰을 처리할 수 있도록 커스텀 Authentication으로 래핑
                    JwtPreAuthenticatedToken jwt = new JwtPreAuthenticatedToken(token);
                    Authentication authenticated = authenticationManager.authenticate(jwt);
                    SecurityContextHolder.getContext().setAuthentication(authenticated);

                    // CustomUserDetails가 있으면 memberNo를 요청 속성에 전달(컨트롤러/서비스에서 활용 가능)
                    Object principal = authenticated.getPrincipal();
                    if (principal instanceof CustomUserDetails cud && cud.getMemberNo() != null) {
                        request.setAttribute("memberNo", cud.getMemberNo());
                    }
                } catch (Exception e) {
                    // 토큰이 유효하지 않으면 인증 없이 진행 (결과는 401/403로 정리)
                    SecurityContextHolder.clearContext();
                    log.debug("JWT auth failed: {}", e.getMessage());
                }
            }
        }

        Authentication a = SecurityContextHolder.getContext().getAuthentication();
        System.out.println("[AUTHZ] path=" + request.getRequestURI()
                + " headerAuth=" + request.getHeader("Authorization")
                + " contextAuth=" + (a==null? "null" : a.getName() + " " + a.getAuthorities()));

        chain.doFilter(request, response);
    }
}