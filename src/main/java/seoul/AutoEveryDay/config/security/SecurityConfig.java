package seoul.AutoEveryDay.config.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;

import static org.springframework.security.config.Customizer.withDefaults;

// spring security 설정 파일 입니다.
@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
public class SecurityConfig {
    private final String[] WHITE_LIST = {
            // swagger
            "/v3/api-docs/**",
            "/swagger-ui/**",
            "/swagger-resources/**",
            // register
            "/register",
            // login
            "/login",
            "/login/process",
            // logout
    };
    @Bean
    protected SecurityFilterChain myConfig(HttpSecurity http) throws Exception {
        /* 허용 페이지 등록 */
        http.authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(WHITE_LIST).permitAll()  // 모든 사용자 허용 경로
                        .anyRequest().authenticated())  // 그 외 나머지 경로는 전부 인증 필요
                // 로그인
                .formLogin(form -> form
                        .loginPage("/login") // 로그인 페이지
                        .loginProcessingUrl("/login/process") // 로그인 Form Action Url
                        .usernameParameter("username") // 아이디 파라미터명 설정, default: username
                        .passwordParameter("password") // 패스워드 파라미터명 설정, default: password
                        .defaultSuccessUrl("/home") // 로그인 성공 후 이동 페이지
                        .permitAll()
                )
                // 로그아웃
                .logout(withDefaults())
                .csrf(AbstractHttpConfigurer::disable); // csrf 비활성화

        return http.build();
    }
    @Bean
    public PasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
}