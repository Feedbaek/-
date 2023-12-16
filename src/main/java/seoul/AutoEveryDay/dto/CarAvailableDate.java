package seoul.AutoEveryDay.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CarAvailableDate {
    private Integer day;
    private boolean available;
}
