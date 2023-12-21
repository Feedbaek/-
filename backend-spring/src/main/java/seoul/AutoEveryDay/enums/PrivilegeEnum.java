package seoul.AutoEveryDay.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum PrivilegeEnum {
    READ_PRIVILEGE("READ"),
    WRITE_PRIVILEGE("WRITE"),
    DELETE_PRIVILEGE("DELETE"),

    CAR_RENTAL_PRIVILEGE("CAR_RENTAL"),
    TRACK_RESERVE_PRIVILEGE("TRACK_RESERVE"),
    GAS_STATION_PRIVILEGE("GAS_STATION"),

    ADMIN_PRIVILEGE("ADMIN");

    private final String value;
}
