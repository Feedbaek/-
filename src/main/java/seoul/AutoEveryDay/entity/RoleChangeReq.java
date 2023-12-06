package seoul.AutoEveryDay.entity;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RoleChangeReq {
    private String username;
    private String role;
}
