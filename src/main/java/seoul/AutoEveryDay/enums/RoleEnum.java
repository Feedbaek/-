package seoul.AutoEveryDay.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum RoleEnum {
    ROLE_ADMIN("ADMIN"),
    ROLE_USER("USER"),
    ROLE_ADVANCED_USER("ADVANCED_USER");

    private final String value;
}
