package seoul.AutoEveryDay.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import seoul.AutoEveryDay.entity.Privilege;
import seoul.AutoEveryDay.entity.Role;
import seoul.AutoEveryDay.entity.User;
import seoul.AutoEveryDay.entity.UserGroup;
import seoul.AutoEveryDay.enums.PrivilegeEnum;
import seoul.AutoEveryDay.repository.PrivilegeRepository;
import seoul.AutoEveryDay.repository.RoleRepository;
import seoul.AutoEveryDay.repository.UserGroupRepository;
import seoul.AutoEveryDay.repository.UserRepository;

import java.util.*;

import static seoul.AutoEveryDay.enums.PrivilegeEnum.*;
import static seoul.AutoEveryDay.enums.RoleEnum.*;

@Component
@RequiredArgsConstructor
public class SetupAuthority implements
        ApplicationListener<ContextRefreshedEvent> {

    private boolean alreadySetup = false;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PrivilegeRepository privilegeRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserGroupRepository userGroupRepository;

    @Override
    @Transactional
    public void onApplicationEvent(ContextRefreshedEvent event) { // 애플리케이션 시작 시점에 실행
        // 이미 실행되었다면 종료
        if (alreadySetup)
            return;

        // 기본 권한 생성
        Privilege readPrivilege
                = createPrivilegeIfNotFound(READ_PRIVILEGE.getValue());
        Privilege writePrivilege
                = createPrivilegeIfNotFound(WRITE_PRIVILEGE.getValue());
        Privilege deletePrivilege
                = createPrivilegeIfNotFound(DELETE_PRIVILEGE.getValue());

        // 권한에 따른 역할 생성
        Set<Privilege> allPrivileges = Arrays.stream(PrivilegeEnum.values())
                .map(PrivilegeEnum::getValue)
                .map(this::createPrivilegeIfNotFound)
                .collect(HashSet::new, HashSet::add, HashSet::addAll);
        Set<Privilege> advancedUserPrivileges = new HashSet<>(Arrays.asList(readPrivilege, writePrivilege, deletePrivilege));
        Set<Privilege> userPrivileges = Collections.singleton(readPrivilege);

        // 역할 생성
        Role adminRole = createRoleIfNotFound(ROLE_ADMIN.getValue(), allPrivileges);
        Role advancedRole = createRoleIfNotFound(ROLE_ADVANCED_USER.getValue(), advancedUserPrivileges);
        Role basicRole = createRoleIfNotFound(ROLE_USER.getValue(), userPrivileges);

        // 관리자 그룹 생성
        UserGroup adminGroup = userGroupRepository.save(UserGroup.builder()
                .name("관리자")
                .build());

        // 현대오토에버 그룹 생성
        UserGroup autoEverGroup = userGroupRepository.save(UserGroup.builder()
                .name("현대오토에버")
                .build());

        // admin 계정 생성
        User user = new User();
        user.setUsername("admin");
        user.setPassword(passwordEncoder.encode("1234"));
        user.setRoles(Collections.singleton(adminRole));
        user.setUserGroup(adminGroup);
        userRepository.save(user);

        // 현대오토에버 계정 생성
        User autoEverUser = new User();
        autoEverUser.setUsername("user");
        autoEverUser.setPassword(passwordEncoder.encode("1234"));
        autoEverUser.setRoles(Collections.singleton(basicRole));
        autoEverUser.setUserGroup(autoEverGroup);
        userRepository.save(autoEverUser);

        alreadySetup = true;
    }

    @Transactional
    public Privilege createPrivilegeIfNotFound(String name) {

        Privilege privilege = privilegeRepository.findByName(name).orElse(null);
        if (privilege == null) {
            privilege = Privilege.builder()
                        .name(name)
                        .build();
            privilegeRepository.save(privilege);
        }
        return privilege;
    }

    @Transactional
    public Role createRoleIfNotFound(String name, Set<Privilege> privileges) {
        Role role = roleRepository.findByName(name).orElse(null);
        if (role == null) {
            role = Role.builder()
                    .name(name)
                    .build();
            role.setPrivileges(privileges);
            roleRepository.save(role);
        }
        return role;
    }
}
