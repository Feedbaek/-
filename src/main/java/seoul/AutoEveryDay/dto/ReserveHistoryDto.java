package seoul.AutoEveryDay.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
    @NotNull
    private Long trackId;
    @NotNull
    private LocalDate date;
}
