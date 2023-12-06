package seoul.AutoEveryDay.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class RegisterReq {
    @NotBlank
    private String username;
    @NotBlank
    private String password;
}
