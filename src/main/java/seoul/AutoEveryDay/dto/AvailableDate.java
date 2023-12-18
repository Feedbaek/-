package seoul.AutoEveryDay.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AvailableDate {
    private Integer day;
    private boolean available;
}
