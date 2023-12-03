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
import seoul.AutoEveryDay.enums.PrivilegeEnum;
import seoul.AutoEveryDay.repository.PrivilegeRepository;
import seoul.AutoEveryDay.repository.RoleRepository;
import seoul.AutoEveryDay.repository.UserRepository;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

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
        List<Privilege> allPrivileges = Arrays.stream(PrivilegeEnum.values())
                .map(PrivilegeEnum::getValue)
                .map(this::createPrivilegeIfNotFound)
                .toList();
        List<Privilege> advancedUserPrivileges = Arrays.asList(
                readPrivilege, writePrivilege, deletePrivilege);
        List<Privilege> userPrivileges = Collections.singletonList(readPrivilege);

        // 역할 생성
        Role adminRole = createRoleIfNotFound(ROLE_ADMIN.getValue(), allPrivileges);
        createRoleIfNotFound(ROLE_ADVANCED_USER.getValue(), advancedUserPrivileges);
        createRoleIfNotFound(ROLE_USER.getValue(), userPrivileges);

        // admin 계정 생성
        User user = new User();
        user.setUsername("admin");
        user.setPassword(passwordEncoder.encode("1234"));
        user.setRoles(Collections.singletonList(adminRole));
        userRepository.save(user);

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
    public Role createRoleIfNotFound(String name, Collection<Privilege> privileges) {
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
