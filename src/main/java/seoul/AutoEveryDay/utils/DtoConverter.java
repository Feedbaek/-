package seoul.AutoEveryDay.utils;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import seoul.AutoEveryDay.dto.RoleDto;
import seoul.AutoEveryDay.entity.Role;

import java.util.List;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class DtoConverter {
    public RoleDto convertToRoleDto(Role role) {
        return RoleDto.builder()
                .id(role.getId())
                .name(role.getName())
                .build();
    }

    public List<RoleDto> convertToRoleDtoList(List<Role> roleList) {
        return roleList.stream()
                .map(this::convertToRoleDto)
                .toList();
    }
}
