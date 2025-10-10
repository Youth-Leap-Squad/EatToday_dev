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
    @Override public Object getPrincipal() { return null; }
    public String getToken() { return token; }
}
