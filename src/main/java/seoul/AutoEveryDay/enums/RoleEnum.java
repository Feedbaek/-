package seoul.AutoEveryDay.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum RoleEnum {
    ROLE_ADMIN("ADMIN"),
    ROLE_USER("USER"),
    ROLE_ADVANCED_USER("ADVANCED_USER"),
    ROLE_CAR_RENTAL("CAR_RENTAL"),
    ROLE_TRACK_RESERVE("TRACK_RESERVE"),
    ROLE_GAS_STATION("GAS_STATION");

    private final String value;
}
