package com.eat.today.member.command.application.service;

import com.eat.today.member.command.domain.aggregate.MemberEntity;
import com.eat.today.member.command.domain.repository.MemberRepository;
import com.eat.today.member.command.domain.repository.SecessionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.InjectMocks;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CommandMemberServiceImpl_UserDetailsTest {

    @InjectMocks
    CommandMemberServiceImpl service;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Mock
    private SecessionRepository secessionRepository;

    @Test
    void loadUserByUsername_success_returnsUserDetailsWithAuthorities() {

        // given
        String phone = "010-1234-1629";

        // Mock MemberEntity를 생성하여 필요한 필드들을 설정
        MemberEntity member = org.mockito.Mockito.mock(MemberEntity.class);
        when(member.getMemberPhone()).thenReturn(phone);
        when(member.getMemberPw()).thenReturn("{bcrypt}hashed");
        when(member.isMemberActive()).thenReturn(true);
        when(member.getMemberRole()).thenReturn(MemberEntity.Role.USER);

        List<MemberEntity> members = Arrays.asList(member);

        when(memberRepository.findByMemberPhone(phone))
                .thenReturn(members);

        // when
        UserDetails ud = service.loadUserByUsername(phone);

        // then
        assertThat(ud.getUsername()).isEqualTo(phone);
        assertThat(ud.getPassword()).isEqualTo("{bcrypt}hashed");
        assertThat(ud.isEnabled()).isTrue();
        assertThat(ud.isAccountNonExpired()).isTrue();
        assertThat(ud.isAccountNonLocked()).isTrue();
        assertThat(ud.isCredentialsNonExpired()).isTrue();
        // 실제 서비스 구현에서는 MemberEntity의 권한과 관계없이 항상 ROLE_ADMIN과 ROLE_USER를 모두 부여함
        assertThat(ud.getAuthorities())
                .extracting(GrantedAuthority::getAuthority)
                .containsExactlyInAnyOrder("ROLE_USER");

    }

    @Test
    void loadUserByUsername_userNotFound_throwsException() {

        // given
        String phone = "010-9999-9999";

        when(memberRepository.findByMemberPhone(phone))
                .thenReturn(Arrays.asList()); // 빈 리스트 반환

        // when & then
        assertThatThrownBy(() -> service.loadUserByUsername(phone))
                .isInstanceOf(org.springframework.security.core.userdetails.UsernameNotFoundException.class)
                .hasMessageContaining("이 폰 번호를 가진 사용자는 존재하지 않습니다");

    }

    @Test
    void loadUserByUsername_nullResult_throwsException() {

        // given
        String phone = "010-1234-1629";

        when(memberRepository.findByMemberPhone(phone))
                .thenReturn(null); // null 반환

        // when & then
        assertThatThrownBy(() -> service.loadUserByUsername(phone))
                .isInstanceOf(org.springframework.security.core.userdetails.UsernameNotFoundException.class)
                .hasMessageContaining("이 폰 번호를 가진 사용자는 존재하지 않습니다");

    }

}