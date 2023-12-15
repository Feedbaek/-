package seoul.AutoEveryDay.service;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import seoul.AutoEveryDay.dto.RegisterReq;
import seoul.AutoEveryDay.entity.Role;
import seoul.AutoEveryDay.entity.User;
import seoul.AutoEveryDay.entity.UserGroup;
import seoul.AutoEveryDay.repository.RoleRepository;
import seoul.AutoEveryDay.repository.UserGroupRepository;
import seoul.AutoEveryDay.repository.UserRepository;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.BDDMockito.given;
import static seoul.AutoEveryDay.enums.RoleEnum.ROLE_USER;
import static seoul.AutoEveryDay.enums.UserGroupEnum.GROUP_AUTOEVER;
import static seoul.AutoEveryDay.enums.UserGroupEnum.GROUP_HYUNDAI;

@ExtendWith(MockitoExtension.class)
@DisplayName("로그인 서비스 테스트")
public class LoginServiceTest {
    @InjectMocks
    private LoginService loginService;
    @Mock
    private UserRepository userRepository;
    @Mock
    private UserGroupRepository userGroupRepository;
    @Mock
    private RoleRepository roleRepository;
    @Mock
    private static PasswordEncoder passwordEncoder;
    @BeforeAll
    public static void setUp() {
        passwordEncoder = new BCryptPasswordEncoder();
    }

    public RegisterReq makeRegisterReq() {
        return RegisterReq.builder()
                .username("test")
                .password("test")
                .name("테스터")
                .userGroup(GROUP_AUTOEVER.getValue())
                .build();
    }

    public static UserGroup makeUserGroup() {
        return UserGroup.builder()
                .id(1L)
                .name(GROUP_AUTOEVER.getValue())
                .build();
    }

    public static User makeUser(UserGroup userGroup) {
        return User.builder()
                .id(1L)
                .username("user1")
                .password("1234")
                .userGroup(userGroup)
                .build();
    }

    public static User makeUser() {
        return User.builder()
                .id(1L)
                .username("user1")
                .password("1234")
                .userGroup(makeUserGroup())
                .build();
    }

    public static Role makeRole() {
        return Role.builder()
                .id(1L)
                .name(ROLE_USER.getValue())
                .build();
    }

    @Test
    @DisplayName("회원가입 테스트")
    public void registerTest() {
        // given
        RegisterReq registerReq = makeRegisterReq();
        UserGroup userGroup = makeUserGroup();
        Role role = makeRole();

        given(userGroupRepository.findByName(registerReq.getUserGroup())).willReturn(Optional.of(userGroup));
        given(roleRepository.findByName(ROLE_USER.getValue())).willReturn(Optional.of(role));
        given(userRepository.save(Mockito.any(User.class))).will(invocation -> {
            User user = invocation.getArgument(0);
            user.setId(1L);
            return user;
        });

        // when
        User user = loginService.register(registerReq);

        // then
        assertThat(user.getId()).isEqualTo(1L);
        assertThat(user.getUsername()).isEqualTo(registerReq.getUsername());
        assertThat(user.getPassword()).isEqualTo(passwordEncoder.encode(registerReq.getPassword()));
        assertThat(user.getName()).isEqualTo(registerReq.getName());
        assertThat(user.getUserGroup().getName()).isEqualTo(registerReq.getUserGroup());
    }

    @Test
    @DisplayName("모든 유저 그룹 이름 가져오기 테스트")
    public void findAllGroupNamesTest() {
        // given
        UserGroup userGroup1 = makeUserGroup();
        UserGroup userGroup2 = UserGroup.builder()
                .id(2L)
                .name(GROUP_HYUNDAI.getValue())
                .build();
        given(userGroupRepository.findAll()).willReturn(Arrays.asList(userGroup1, userGroup2));
        // when
        List<String> groupNames = loginService.findAllGroupNames();

        // then
        assertThat(groupNames.size()).isEqualTo(2);
        assertThat(groupNames.get(0)).isEqualTo(GROUP_AUTOEVER.getValue());
        assertThat(groupNames.get(1)).isEqualTo(GROUP_HYUNDAI.getValue());
    }
}
