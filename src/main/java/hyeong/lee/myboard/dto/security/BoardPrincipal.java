package hyeong.lee.myboard.dto.security;

import hyeong.lee.myboard.domain.UserAccount;
import hyeong.lee.myboard.dto.request.UserAccountDto;
import lombok.Builder;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Builder
public class BoardPrincipal implements UserDetails {

    private final String username;
    private final String password;
    private final Collection<? extends GrantedAuthority> authorities;
    private final String email;
    private final String nickname;
    private final String memo;

    private int loginFailCount; // 5회 이상 실패 시 계정 잠김 설정

    public static BoardPrincipal of(String username, String password, String auth, String email, String nickname, String memo, int loginFailCount) {

        RoleType roleType = RoleType.valueOf(auth); // auth 문자열에 해당하는 Enum 꺼내기

        Set<GrantedAuthority> roleSet = new HashSet<>();
        // 동시에 여러개의 권한을 가질수도 있으므로 ','로 구분된 여러개의 권한 집어넣기
        for(String role : roleType.getAuth().split(",")) {
            roleSet.add(new SimpleGrantedAuthority(role));
        }

        return BoardPrincipal.builder()
                .username(username)
                .password(password)
                .authorities(roleSet)
                .email(email)
                .nickname(nickname)
                .memo(memo)
                .loginFailCount(loginFailCount)
                .build();
    }

    public UserAccountDto toDto() {
        return UserAccountDto.builder()
                .userId(username)
                .password(password)
                .email(email)
                .nickname(nickname)
                .memo(memo)
                .loginFailCount(loginFailCount).build();
    }

    public UserAccount toEntity() {
        return UserAccount.builder()
                .userId(username)
                .userPassword(password)
                .email(email)
                .nickname(nickname)
                .memo(memo)
                .loginFailCount(loginFailCount).build();
    }

    public static BoardPrincipal from(UserAccountDto dto) {
        return BoardPrincipal.of(dto.getUserId(),
                dto.getPassword(),
                dto.getAuth(),
                dto.getEmail(),
                dto.getNickname(),
                dto.getMemo(),
                dto.getLoginFailCount());
    }

    public enum RoleType {
        ADMIN("ROLE_ADMIN"),
        USER("ROLE_USER");

        @Getter
        private final String auth;

        RoleType(String auth) {
            this.auth = auth;
        }
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonLocked() { // 계정이 잠겨있지 않은지? (true=사용 가능)
        // 비밀번호 5회 초과 실패 시 계정 잠김 설정
        return loginFailCount < 5;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
