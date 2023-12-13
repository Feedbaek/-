package seoul.AutoEveryDay.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DriveHistoryDto {
    private Long id;
    private Long userId;
    @NotBlank
    private Long trackId;
    @NotBlank
    private LocalDate date;
}
