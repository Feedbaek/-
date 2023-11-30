package seoul.AutoEveryDay.config.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.access.expression.DefaultWebSecurityExpressionHandler;

import static org.springframework.security.config.Customizer.withDefaults;
import static seoul.AutoEveryDay.enums.PrivilegeEnum.*;
import static seoul.AutoEveryDay.enums.RoleEnum.*;

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
            // index
            "/",
            // register
            "/register",
            // login
            "/login",
            "/login/process",
            // logout
            "/logout",
    };

    private final String[] ADMIN_LIST = {
            "/admin/**",
    };

    private final String[] ADVANCED_USER_LIST = {
            "/advanced_user",
    };

    @Bean
    protected SecurityFilterChain myConfig(HttpSecurity http) throws Exception {
        /* 허용 페이지 등록 */
        http.authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(WHITE_LIST).permitAll()  // 모든 사용자 허용 경로
//                        .requestMatchers(ADMIN_LIST).hasRole(ROLE_ADMIN.getValue())  // ADMIN 권한만 허용 경로
//                        .requestMatchers(ADVANCED_USER_LIST).hasAuthority(WRITE_PRIVILEGE.getValue()) // ADVANCED_USER 권한만 허용 경로
                        .anyRequest().hasAuthority(READ_PRIVILEGE.getValue()))  // 그 외 나머지 경로는 전부 인증된 사용자만 허용
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
                .logout(withDefaults()) // '/logout' 으로 로그아웃
                .csrf(AbstractHttpConfigurer::disable); // csrf 비활성화

        return http.build();
    }
    @Bean
    public PasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /** 아래는 역할별로 계층 구조를 설정해주는 기능. 하지만 당장 Role 말고 Authority를 사용중 */
//    @Bean
//    public RoleHierarchy roleHierarchy() {
//        RoleHierarchyImpl roleHierarchy = new RoleHierarchyImpl();
//        String hierarchy = ROLE_ADMIN.name() + " > " + ROLE_ADVANCED_USER.name() + "\n"
//                + ROLE_ADVANCED_USER.name() + " > " + ROLE_USER.name();
//        roleHierarchy.setHierarchy(hierarchy);
//        return roleHierarchy;
//    }
//    @Bean
//    public DefaultWebSecurityExpressionHandler customWebSecurityExpressionHandler() {
//        DefaultWebSecurityExpressionHandler expressionHandler = new DefaultWebSecurityExpressionHandler();
//        expressionHandler.setRoleHierarchy(roleHierarchy());
//        return expressionHandler;
//    }
}