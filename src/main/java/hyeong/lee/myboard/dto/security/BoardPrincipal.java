package hyeong.lee.myboard.dto.security;

import hyeong.lee.myboard.dto.UserAccountDto;
import lombok.Builder;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
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

    public static BoardPrincipal of(String username, String password, String email, String nickname, String memo) {
        
        // TODO: 로그인 사용자 별 권한 분리
        Set<RoleType> roleTypes = Set.of(RoleType.USER);
        
        return BoardPrincipal.builder()
                .username(username)
                .password(password)
                .authorities(roleTypes.stream()
                        .map(RoleType::getAuth)
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toUnmodifiableSet()))
                .email(email)
                .nickname(nickname)
                .memo(memo)
                .build();
    }

    public UserAccountDto toDto() {
        return UserAccountDto.builder()
                .userId(username)
                .password(password)
                .email(email)
                .nickname(nickname)
                .memo(memo).build();
    }

    public static BoardPrincipal from(UserAccountDto dto) {
        return BoardPrincipal.of(dto.getUserId(), dto.getPassword(), dto.getEmail(), dto.getNickname(), dto.getMemo());
    }

    public enum RoleType {
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
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
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
