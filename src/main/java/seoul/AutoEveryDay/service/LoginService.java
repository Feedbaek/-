package seoul.AutoEveryDay.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import seoul.AutoEveryDay.dto.RegisterReq;
import seoul.AutoEveryDay.entity.Privilege;
import seoul.AutoEveryDay.entity.Role;
import seoul.AutoEveryDay.entity.User;
import seoul.AutoEveryDay.repository.RoleRepository;
import seoul.AutoEveryDay.repository.UserRepository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static seoul.AutoEveryDay.enums.RoleEnum.ROLE_USER;

@Service
@Slf4j(topic = "UserService")
@Transactional
@RequiredArgsConstructor
public class LoginService implements UserDetailsService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    // 로그인 여부 확인, 로그인 되어있으면 true, 아니면 false
    public static boolean isAuthenticated() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication instanceof AnonymousAuthenticationToken) {
            return false;
        }
        return authentication.isAuthenticated();
    }

    public void validateDuplicateUser(User user) {
        userRepository.findByUsername(user.getUsername())
            .ifPresent(m -> {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "이미 존재하는 회원입니다.");
            });
    }
    public void save(User user) {
        validateDuplicateUser(user);
        try {
            userRepository.save(user);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "회원 저장에 실패했습니다.");
        }
    }

    public User findByName(String name) {
        return userRepository.findByUsername(name)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "회원을 찾을 수 없습니다."));
    }

    // 기본 유저로 회원가입
    public void register(RegisterReq registerReq) {
        Role role = roleRepository.findByName(ROLE_USER.getValue())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "회원가입에 실패했습니다."));
        User user = User.builder()
                .username(registerReq.getUsername())
                .password(passwordEncoder.encode(registerReq.getPassword()))
                .roles(Collections.singletonList(role))
                .build();
        save(user);
    }


    /** ====================================================
     * <아래는 UserDetailsService 구현부>
     * =====================================================*/

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다."));
        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .authorities(getAuthorities(user.getRoles()))
                .build();
    }

    private Collection<? extends GrantedAuthority> getAuthorities(
            Collection<Role> roles) {

        return getGrantedAuthorities(getPrivileges(roles));
    }

    private List<GrantedAuthority> getGrantedAuthorities(List<String> privileges) {
        List<GrantedAuthority> authorities = new ArrayList<>();
        for (String privilege : privileges) {
            authorities.add(new SimpleGrantedAuthority(privilege));
        }
        return authorities;
    }

    private List<String> getPrivileges(Collection<Role> roles) {

        List<String> privileges = new ArrayList<>();
        List<Privilege> collection = new ArrayList<>();
        for (Role role : roles) {
//            privileges.add(role.getName());  // 이거는 role 이름을 권한으로 인식하는데 왜 하는지 모름
            collection.addAll(role.getPrivileges());
        }
        for (Privilege item : collection) {
            privileges.add(item.getName());
        }
        return privileges;
    }
}
