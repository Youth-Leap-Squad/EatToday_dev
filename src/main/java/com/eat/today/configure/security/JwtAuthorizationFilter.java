package com.eat.today.configure.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
public class JwtAuthorizationFilter extends OncePerRequestFilter {

    private final AuthenticationManager authenticationManager;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain)
            throws ServletException, IOException {

        if (SecurityContextHolder.getContext().getAuthentication() == null) {
            String authz = request.getHeader(HttpHeaders.AUTHORIZATION);
            if (authz != null && authz.startsWith("Bearer ")) {
                String token = authz.substring(7).trim();
                try {
                    JwtPreAuthenticatedToken pre = new JwtPreAuthenticatedToken(token);
                    Authentication authenticated = authenticationManager.authenticate(pre);
                    SecurityContextHolder.getContext().setAuthentication(authenticated);

                    Object principal = authenticated.getPrincipal();
                    if (principal instanceof CustomUserDetails cud && cud.getMemberNo() != null) {
                        request.setAttribute("memberNo", cud.getMemberNo());
                    }
                } catch (Exception e) {
                    SecurityContextHolder.clearContext();
                }
            }
        }

        Authentication a = SecurityContextHolder.getContext().getAuthentication();
        System.out.println("[AUTHZ] path=" + request.getRequestURI()
                + " headerAuth=" + request.getHeader(HttpHeaders.AUTHORIZATION)
                + " contextAuth=" + (a == null ? "null" : a.getName() + " " + a.getAuthorities()));

        chain.doFilter(request, response);
    }
}
