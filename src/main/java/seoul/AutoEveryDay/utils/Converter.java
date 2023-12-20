package seoul.AutoEveryDay.utils;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import seoul.AutoEveryDay.dto.car.CarDto;
import seoul.AutoEveryDay.dto.car.CarModelRes;
import seoul.AutoEveryDay.dto.user.PrivilegeDto;
import seoul.AutoEveryDay.dto.user.RoleDto;
import seoul.AutoEveryDay.dto.user.UserDto;
import seoul.AutoEveryDay.entity.Privilege;
import seoul.AutoEveryDay.entity.Role;
import seoul.AutoEveryDay.entity.User;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
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
            list.add(carDto.getImage());

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

    public List<List<String>> convertCarModelDtoList(List<CarModelRes> carModelResList) {
        List<List<String>> listList = new ArrayList<>();

        for (CarModelRes carModelRes : carModelResList) {
            List<String> list = new ArrayList<>();

            list.add(carModelRes.getId().toString());
            list.add(carModelRes.getName());
            list.add(carModelRes.getImage());

            listList.add(list);
        }

        return listList;
    }

    private boolean isJPEG(MultipartFile file) {
        String contentType = file.getContentType();
        return contentType != null && contentType.equals("image/jpeg");
    }

    public boolean isActualJPEG(MultipartFile file) {
        if (!isJPEG(file)) {
            return false;
        }
        try (InputStream inputStream = file.getInputStream()) {
            byte[] firstTwoBytes = new byte[2];
            byte[] lastTwoBytes = new byte[2];

            inputStream.read(firstTwoBytes);

            if (firstTwoBytes[0] == (byte) 0xFF && firstTwoBytes[1] == (byte) 0xD8) {
                inputStream.skip(file.getSize() - 4);
                inputStream.read(lastTwoBytes);
                return lastTwoBytes[0] == (byte) 0xFF && lastTwoBytes[1] == (byte) 0xD9;
            }
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "올바르지 않은 이미지 파일입니다.");
        }
        return false;
    }

    public String convertImgToUrl(MultipartFile file, String path, String fileName) {
        // 프로젝트 내부 경로로 저장 경로 설정
        String uploadDir = "src/main/resources/static/image" + path;
        Path fullPath = Paths.get(uploadDir).resolve(fileName);

        // 파일 저장
        try (InputStream inputStream = file.getInputStream()) {
            Files.copy(inputStream, fullPath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            System.out.println("컨버터 이미지 저장 실패");
            System.out.println(path + "/" + fileName);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "이미지 저장 실패");
        }
        return "/image" + path + "/" + fileName;
    }
}
