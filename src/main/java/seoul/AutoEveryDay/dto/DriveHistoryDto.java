package seoul.AutoEveryDay.dto;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DriveHistoryDto {
    private Long id;
    @NotNull
    private Long userId;
    @NotNull
    private Long carId;
    @NotNull
    private Long trackId;
    @NotNull
    private Integer distance;
    @NotNull
    private Integer time;
    @NotNull
    private Integer averageSpeed;
    @NotNull
    private Integer maxSpeed;
    @NotBlank
    private String date;
}
