package com.eat.today.configure.security;

import org.springframework.security.authentication.AbstractAuthenticationToken;

/** Bearer 문자열을 담아 Provider로 넘기는 토큰 */
public class JwtPreAuthenticatedToken extends AbstractAuthenticationToken {
    private final String token;

    public JwtPreAuthenticatedToken(String token) {
        super(null);
        this.token = token;
        setAuthenticated(false);
    }

    @Override public Object getCredentials() { return token; }

    // ✅ principal을 token으로 반환(표준 동작, 디버깅/로깅/서드파티 호환에 유리)
    @Override public Object getPrincipal() { return token; }

    public String getToken() { return token; }
}
