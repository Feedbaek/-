package seoul.AutoEveryDay.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class RegisterReq {
    private String username;
    private String password;
}
