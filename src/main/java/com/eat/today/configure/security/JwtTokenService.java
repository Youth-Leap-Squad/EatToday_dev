package com.eat.today.configure.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.Instant;
import java.util.*;

@Service
public class JwtTokenService {

    private final SecretKey key = Keys.hmacShaKeyFor(
            Optional.ofNullable(System.getenv("JWT_SECRET"))
                    .orElse("please-change-me-please-change-me-012345")
                    .getBytes(StandardCharsets.UTF_8)
    );

    public record JwtPayload(String username, List<String> roles) {}

    /** 토큰 검증 */
    public JwtPayload parseAndValidate(String token) {
        try {
            Jws<Claims> claimsJws = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token);

            Claims claims = claimsJws.getBody();
            String username = claims.getSubject();

            @SuppressWarnings("unchecked")
            List<String> roles = (List<String>) claims.get("roles");
            if (roles == null) roles = List.of();

            return new JwtPayload(username, roles);
        } catch (JwtException e) {
            throw new IllegalArgumentException("유효하지 않은 JWT 토큰입니다.", e);
        }
    }

    /** 권한 변환 */
    public List<GrantedAuthority> toGrantedAuthorities(List<String> roles) {
        if (roles == null) return List.of();
        List<GrantedAuthority> list = new ArrayList<>();
        for (String r : roles) {
            String name = (r != null && r.startsWith("ROLE_")) ? r : "ROLE_" + r;
            list.add(new SimpleGrantedAuthority(name));
        }
        return list;
    }

    /** 토큰 발급 */
    public String issueToken(String username,
                             Collection<? extends GrantedAuthority> authorities,
                             Duration ttl) {
        Instant now = Instant.now();
        List<String> roles = new ArrayList<>();
        if (authorities != null) {
            for (GrantedAuthority a : authorities) {
                roles.add(a.getAuthority());
            }
        }

        Map<String, Object> claims = new HashMap<>();
        claims.put("roles", roles);

        return Jwts.builder()
                .setSubject(username)
                .claim("roles", roles)
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(now.plus(ttl)))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }
}
