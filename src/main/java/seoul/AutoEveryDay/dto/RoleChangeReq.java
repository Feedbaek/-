package seoul.AutoEveryDay.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RoleChangeReq {
    @NotBlank
    private Long userId;
    @NotBlank
    private String role;
}
