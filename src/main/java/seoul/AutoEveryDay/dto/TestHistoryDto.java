package seoul.AutoEveryDay.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TestHistoryDto {
    @NotBlank
    private String userName;
    @NotBlank
    private String testCenterName;
    private LocalDate time;
}
