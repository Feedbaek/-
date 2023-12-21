package seoul.AutoEveryDay.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum UserGroupEnum {
    GROUP_ADMIN("ADMIN"),
    GROUP_HYUNDAI("HYUNDAI"),
    GROUP_AUTOEVER("AUTOEVER"),
    GROUP_MOBIS("MOBIS");

    private final String value;
}
