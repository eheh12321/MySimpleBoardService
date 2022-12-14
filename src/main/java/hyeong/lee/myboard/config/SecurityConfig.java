package hyeong.lee.myboard.config;

import hyeong.lee.myboard.dto.request.UserAccountDto;
import hyeong.lee.myboard.dto.security.BoardPrincipal;
import hyeong.lee.myboard.handler.LoginFailureHandler;
import hyeong.lee.myboard.handler.LoginSuccessHandler;
import hyeong.lee.myboard.repository.UserAccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

@RequiredArgsConstructor
@Configuration
public class SecurityConfig {

    private final AuthenticationSuccessHandler loginSuccessHandler;
    private final AuthenticationFailureHandler loginFailureHandler;
    private final LogoutSuccessHandler logoutSuccessHandler;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http.csrf().disable()
                .authorizeHttpRequests(auth ->
                        auth.requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()
                                .antMatchers("/", "/boards/**", "/api/**", "/resources/**", "/storage/**", "/about/**", "/user/sign-up", "/login").permitAll()
                                .antMatchers("/admin/**").hasRole("ADMIN")
                                .anyRequest().authenticated()
                )
                .formLogin()
                    .loginPage("/login")
                    .loginProcessingUrl("/login")
                    .successHandler(loginSuccessHandler)
                    .failureHandler(loginFailureHandler)
                .and()
                    .logout()
                    .invalidateHttpSession(true)
                    .deleteCookies("JSESSION_ID")
                    .logoutSuccessHandler(logoutSuccessHandler)
                .and()
                .build();
    }
    @Bean
    public UserDetailsService userDetailsService(UserAccountRepository userAccountRepository) {
        // ????????? ????????? ?????? ??????
        // ????????? BoradPrincipal ????????? ????????? ?????? ?????? ?????? ???????????? ?????????
        return username -> userAccountRepository
                .findById(username) // username??? ?????? UserAccount ????????? ????????????
                .map(UserAccountDto::from) // UserAccountDTO??? ?????? ??? ?????????
                .map(BoardPrincipal::from) // (UserDetails??? ????????????) BoardPrincipal ?????? ?????? ??????
                .orElseThrow(() -> new UsernameNotFoundException("????????? ?????? ??? ???????????? - username: " + username));
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
}
