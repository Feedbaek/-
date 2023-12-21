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
import seoul.AutoEveryDay.enums.UserGroupEnum;
import seoul.AutoEveryDay.repository.PrivilegeRepository;
import seoul.AutoEveryDay.repository.RoleRepository;
import seoul.AutoEveryDay.repository.UserGroupRepository;
import seoul.AutoEveryDay.repository.UserRepository;

import java.util.*;

import static seoul.AutoEveryDay.enums.PrivilegeEnum.*;
import static seoul.AutoEveryDay.enums.RoleEnum.*;
import static seoul.AutoEveryDay.enums.UserGroupEnum.GROUP_ADMIN;
import static seoul.AutoEveryDay.enums.UserGroupEnum.GROUP_AUTOEVER;

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
        Privilege carRentalPrivilege
                = createPrivilegeIfNotFound(CAR_RENTAL_PRIVILEGE.getValue());
        Privilege trackReservePrivilege
                = createPrivilegeIfNotFound(TRACK_RESERVE_PRIVILEGE.getValue());
        Privilege gasStationPrivilege
                = createPrivilegeIfNotFound(GAS_STATION_PRIVILEGE.getValue());

        // 권한에 따른 역할 생성
        Set<Privilege> allPrivileges = Arrays.stream(PrivilegeEnum.values())
                .map(PrivilegeEnum::getValue)
                .map(this::createPrivilegeIfNotFound)
                .collect(HashSet::new, HashSet::add, HashSet::addAll);
        Set<Privilege> advancedUserPrivileges = new HashSet<>(Arrays.asList(readPrivilege, carRentalPrivilege, trackReservePrivilege, gasStationPrivilege));
        Set<Privilege> userPrivileges = Collections.singleton(readPrivilege);
        Set<Privilege> carRentalPrivileges = new HashSet<>(Arrays.asList(readPrivilege, carRentalPrivilege));
        Set<Privilege> trackReservePrivileges = new HashSet<>(Arrays.asList(readPrivilege, trackReservePrivilege));
        Set<Privilege> gasStationPrivileges = new HashSet<>(Arrays.asList(readPrivilege, gasStationPrivilege));

        // 역할 생성
        Role adminRole = createRoleIfNotFound(ROLE_ADMIN.getValue(), allPrivileges);
        Role advancedRole = createRoleIfNotFound(ROLE_ADVANCED_USER.getValue(), advancedUserPrivileges);
        Role basicRole = createRoleIfNotFound(ROLE_USER.getValue(), userPrivileges);
        Role carRentalRole = createRoleIfNotFound(ROLE_CAR_RENTAL.getValue(), carRentalPrivileges);
        Role trackReserveRole = createRoleIfNotFound(ROLE_TRACK_RESERVE.getValue(), trackReservePrivileges);
        Role gasStationRole = createRoleIfNotFound(ROLE_GAS_STATION.getValue(), gasStationPrivileges);

        // 그룹 생성
        List<UserGroup> allUserGroups = Arrays.stream(UserGroupEnum.values())
                .map(UserGroupEnum::getValue)
                .map(this::createUserGroupIfNotFound)
                .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);

        // 관리자 그룹 생성
        UserGroup adminGroup = createUserGroupIfNotFound(GROUP_ADMIN.getValue());

        // 현대오토에버 그룹 생성
        UserGroup autoEverGroup = createUserGroupIfNotFound(GROUP_AUTOEVER.getValue());

        // admin 계정 생성
        User user = new User();
        user.setUsername("admin");
        user.setPassword(passwordEncoder.encode("1234"));
        user.setName("관리자");
        user.setRoles(Collections.singleton(adminRole));
        user.setUserGroup(adminGroup);
        userRepository.save(user);

        // 현대오토에버 계정 생성
        User autoEverUser = new User();
        autoEverUser.setUsername("user");
        autoEverUser.setPassword(passwordEncoder.encode("1234"));
        autoEverUser.setName("김민석");
        autoEverUser.setRoles(Collections.singleton(advancedRole));
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

    @Transactional
    public UserGroup createUserGroupIfNotFound(String name) {
        UserGroup userGroup = userGroupRepository.findByName(name).orElse(null);
        if (userGroup == null) {
            userGroup = UserGroup.builder()
                    .name(name)
                    .build();
            userGroupRepository.save(userGroup);
        }
        return userGroup;
    }
}
