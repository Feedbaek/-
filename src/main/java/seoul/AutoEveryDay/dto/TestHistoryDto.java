package seoul.AutoEveryDay.dto;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TestHistoryDto {
    private String userName;
    private String testCenterName;
    private LocalDate time;
}
