package seoul.AutoEveryDay.utils;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import seoul.AutoEveryDay.dto.*;
import seoul.AutoEveryDay.entity.Privilege;
import seoul.AutoEveryDay.entity.RentCar;
import seoul.AutoEveryDay.entity.Role;
import seoul.AutoEveryDay.entity.User;

import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Component
@RequiredArgsConstructor
public class Converter {
    public UserDto convertToUserDto(User user) {

        return UserDto.builder()
                .id(user.getId())
                .username(user.getUsername())
                .password(user.getPassword())
                .name(user.getName())
                .userGroup(user.getUserGroup().getName())
                .build();
    }
    public List<UserDto> convertToUserDtoList(List<User> userList) {
        return userList.stream()
                .map(this::convertToUserDto)
                .toList();
    }
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

    public List<List<String>> convertCarDtoList(List<CarDto> carDtoList) {
        List<List<String>> listList = new ArrayList<>();

        for (CarDto carDto : carDtoList) {
            List<String> list = new ArrayList<>();
            list.add(carDto.getId().toString());
            list.add(carDto.getNumber());
            list.add(carDto.getModel());
            list.add(carDto.getStatus());
            list.add(carDto.getComment());
            listList.add(list);
        }

        return listList;
    }

    public List<String> getDayOfWeek(LocalDate now) {
        List<String> dayOfWeek = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            dayOfWeek.add(now.plusDays(i).getDayOfWeek().getDisplayName(TextStyle.NARROW, Locale.KOREAN));
        }
        return dayOfWeek;
    }

    private PrivilegeDto convertToPrivilegeDto(Privilege privilege) {
        return PrivilegeDto.builder()
                .id(privilege.getId())
                .name(privilege.getName())
                .build();
    }

    public List<PrivilegeDto> convertToPrivilegeDtoList(List<Privilege> privilegeList) {
        return privilegeList.stream()
                .map(this::convertToPrivilegeDto)
                .toList();
    }
}
