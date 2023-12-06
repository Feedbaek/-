package seoul.AutoEveryDay.dto;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CarDto {
    private String number;
    private String type;
    private String status;
    private String comment;
}

