package com.eat.today.configure.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.Instant;
import java.util.*;

/**
 * JWT 발급/검증 유틸리티 (JJWT 0.11.5)
 * - sub: 로그인 식별자(이메일)
 * - roles: ["ROLE_ADMIN", "ROLE_USER"] 형태(미리 ROLE_ 접두어가 없다면 자동으로 붙여서 저장)
 */
@Service
public class JwtTokenService {

    /**
     * application.yml 의 jwt.secret 우선, 없으면 환경변수 JWT_SECRET,
     * 그것도 없으면 기본값(개발용) 사용. 운영에서는 반드시 교체하세요.
     */
    private final SecretKey key;

    public JwtTokenService(
            @Value("${jwt.secret:}") String propSecret
    ) {
        String secret = (propSecret != null && !propSecret.isBlank())
                ? propSecret
                : Optional.ofNullable(System.getenv("JWT_SECRET"))
                .orElse("please-change-me-please-change-me-012345");

        // HMAC-SHA256 에 충분한 길이 확보(>= 32 bytes)
        this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    /** 파싱 결과 DTO */
    public record JwtPayload(String username, List<String> roles, Date issuedAt, Date expiresAt) {}

    /** 토큰 발급 */
    public String issueToken(String username,
                             Collection<? extends GrantedAuthority> authorities,
                             Duration ttl) {
        // ROLE 접두어 보정
        List<String> roles = authorities.stream()
                .map(GrantedAuthority::getAuthority)
                .map(this::ensureRolePrefix)
                .toList();

        Instant now = Instant.now();

        return Jwts.builder()
                .setSubject(username)            // 이메일/로그인ID
                .claim("roles", roles)           // 권한
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(now.plus(ttl)))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    /** 토큰 검증 & 파싱 */
    public JwtPayload parseAndValidate(String token) {
        try {
            Jws<Claims> jws = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token);

            Claims c = jws.getBody();
            String username = c.getSubject();

            @SuppressWarnings("unchecked")
            List<String> rolesRaw = (List<String>) c.get("roles");
            List<String> roles = (rolesRaw == null ? List.of() : rolesRaw.stream()
                    .filter(Objects::nonNull)
                    .map(this::ensureRolePrefix)
                    .toList());

            return new JwtPayload(username, roles, c.getIssuedAt(), c.getExpiration());
        } catch (JwtException e) {
            // 서명불일치/만료/변조 등
            throw new IllegalArgumentException("유효하지 않은 JWT 토큰입니다.", e);
        }
    }

    /** roles → Spring Security 권한 객체 */
    public List<GrantedAuthority> toGrantedAuthorities(List<String> roles) {
        if (roles == null || roles.isEmpty()) return List.of();
        List<GrantedAuthority> list = new ArrayList<>(roles.size());
        for (String r : roles) {
            list.add(new SimpleGrantedAuthority(ensureRolePrefix(r)));
        }
        return list;
    }

    /** "ADMIN" → "ROLE_ADMIN" 보정 */
    private String ensureRolePrefix(String role) {
        if (role == null || role.isBlank()) return "ROLE_USER";
        return role.startsWith("ROLE_") ? role : "ROLE_" + role;
    }


}
