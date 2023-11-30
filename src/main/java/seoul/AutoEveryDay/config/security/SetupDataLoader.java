package seoul.AutoEveryDay.config.security;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import seoul.AutoEveryDay.entity.Privilege;
import seoul.AutoEveryDay.entity.Role;
import seoul.AutoEveryDay.entity.User;
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
public class SetupDataLoader implements
        ApplicationListener<ContextRefreshedEvent> {

    boolean alreadySetup = false;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PrivilegeRepository privilegeRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if (alreadySetup)
            return;
        Privilege readPrivilege
                = createPrivilegeIfNotFound(READ_PRIVILEGE.getValue());
        Privilege writePrivilege
                = createPrivilegeIfNotFound(WRITE_PRIVILEGE.getValue());
        Privilege deletePrivilege
                = createPrivilegeIfNotFound(DELETE_PRIVILEGE.getValue());
        Privilege adminPrivilege
                = createPrivilegeIfNotFound(ADMIN_PRIVILEGE.getValue());

        List<Privilege> adminPrivileges = Arrays.asList(
                readPrivilege, writePrivilege, deletePrivilege, adminPrivilege);
        List<Privilege> advancedUserPrivileges = Arrays.asList(
                readPrivilege, writePrivilege, deletePrivilege);

        Role adminRole = createRoleIfNotFound(ROLE_ADMIN.getValue(), adminPrivileges);
        createRoleIfNotFound(ROLE_USER.getValue(), Collections.singletonList(readPrivilege));
        createRoleIfNotFound(ROLE_ADVANCED_USER.getValue(), advancedUserPrivileges);

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
