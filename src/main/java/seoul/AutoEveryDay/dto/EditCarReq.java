package seoul.AutoEveryDay.dto;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EditCarReq {
    private String number;
    private String status;
    private String comment;
}
