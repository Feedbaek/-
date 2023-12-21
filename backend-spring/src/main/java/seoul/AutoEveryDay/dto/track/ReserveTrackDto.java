package seoul.AutoEveryDay.dto.track;

import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReserveTrackDto {
    private Long id;
    private Long userId;
    @NotNull
    private Long trackId;
    @NotNull
    private LocalDate date;
}
