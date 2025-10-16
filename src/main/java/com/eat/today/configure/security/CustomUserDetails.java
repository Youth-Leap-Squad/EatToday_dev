package com.eat.today.configure.security;

import com.eat.today.member.command.domain.aggregate.MemberEntity;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Getter
public class CustomUserDetails extends User {
    
    private final Integer memberNo;
    private final MemberEntity.Role memberRole;
    
    public CustomUserDetails(String username, 
                            String password, 
                            Collection<? extends GrantedAuthority> authorities,
                            Integer memberNo,
                            MemberEntity.Role memberRole) {
        super(username, password, true, true, true, true, authorities);
        this.memberNo = memberNo;
        this.memberRole = memberRole;
    }
    
    // MemberEntity를 받는 편의 생성자
    public CustomUserDetails(MemberEntity member) {
        super(member.getMemberEmail(), 
              member.getMemberPw(),
              true, true, true, true,
              getAuthoritiesFromRole(member.getMemberRole()));
        this.memberNo = member.getMemberNo();
        this.memberRole = member.getMemberRole();
    }
    
    private static List<GrantedAuthority> getAuthoritiesFromRole(MemberEntity.Role role) {
        List<GrantedAuthority> authorities = new ArrayList<>();
        if (role == MemberEntity.Role.ADMIN) {
            authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
            authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
        } else {
            authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
        }
        return authorities;
    }
}
