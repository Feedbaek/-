package seoul.AutoEveryDay.utils;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import seoul.AutoEveryDay.dto.CarDto;
import seoul.AutoEveryDay.dto.RoleDto;
import seoul.AutoEveryDay.entity.Role;

import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Component
@RequiredArgsConstructor
public class Converter {
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
}
