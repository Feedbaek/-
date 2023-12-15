package seoul.AutoEveryDay.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import seoul.AutoEveryDay.entity.Privilege;
import seoul.AutoEveryDay.entity.Role;
import seoul.AutoEveryDay.entity.User;
import seoul.AutoEveryDay.repository.PrivilegeRepository;
import seoul.AutoEveryDay.repository.RoleRepository;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j(topic = "AuthorizeService")
public class AuthorizeService {
    private final RoleRepository roleRepository;
    private final PrivilegeRepository privilegeRepository;

    public String grantRole(User user, String roleName) {
        Role role = roleRepository.findByName(roleName).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "역할을 찾을 수 없습니다.")
        );
        try {
            role.getUsers().add(user);
        } catch (Exception e) {
            log.error("역할 부여 실패: " + e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "역할 부여 실패");
        }
        return null;
    }

    public String createRole(String roleName) {
        Role role = Role.builder()
                .name(roleName)
                .build();
        try {
            roleRepository.save(role);
        } catch (Exception e) {
            log.error("역할 생성 실패: " + e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "역할 생성 실패");
        }
        return roleName;
    }

    public String deleteRole(String roleName) {
        Role role = roleRepository.findByName(roleName).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "역할을 찾을 수 없습니다.")
        );
        if (!role.getUsers().isEmpty()) {
            log.error("역할에 해당하는 사용자가 존재합니다.");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "역할에 해당하는 사용자가 존재합니다.");
        }
        try {
            roleRepository.delete(role);
        } catch (Exception e) {
            log.error("역할 삭제 실패: " + e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "역할 삭제 실패");
        }
        return null;
    }

    public String grantPrivilege(String roleName, String privilegeName) {
        Role role = roleRepository.findByName(roleName).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "역할을 찾을 수 없습니다.")
        );
        Privilege privilege = privilegeRepository.findByName(privilegeName).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "권한을 찾을 수 없습니다.")
        );
        try {
            role.getPrivileges().add(privilege);
        } catch (Exception e) {
            log.error("권한 부여 실패: " + e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "권한 부여 실패");
        }
        return privilegeName;
    }

    public String revokePrivilege(String roleName, String privilegeName) {
        Role role = roleRepository.findByName(roleName).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "역할을 찾을 수 없습니다.")
        );
        Privilege privilege = privilegeRepository.findByName(privilegeName).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "권한을 찾을 수 없습니다.")
        );
        try {
            role.getPrivileges().remove(privilege);
            privilege.getRoles().remove(role);
        } catch (Exception e) {
            log.error("권한 회수 실패: " + e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "권한 회수 실패");
        }
        return privilegeName;
    }

    public String createPrivilege(String privilegeName) {
        Privilege privilege = Privilege.builder()
                .name(privilegeName)
                .build();
        try {
            privilegeRepository.save(privilege);
        } catch (Exception e) {
            log.error("권한 생성 실패: " + e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "권한 생성 실패");
        }
        return privilegeName;
    }

    public String deletePrivilege(String privilegeName) {
        Privilege privilege = privilegeRepository.findByName(privilegeName).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "권한을 찾을 수 없습니다.")
        );
        try {
            privilegeRepository.delete(privilege);
        } catch (Exception e) {
            log.error("권한 삭제 실패: " + e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "권한 삭제 실패");
        }
        return privilegeName;
    }
}
