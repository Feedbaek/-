package seoul.AutoEveryDay.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReserveHistoryDto {
    private Long id;
    private Long userId;
    @NotBlank
    private Long trackId;
    @NotBlank
    private LocalDate date;
}
