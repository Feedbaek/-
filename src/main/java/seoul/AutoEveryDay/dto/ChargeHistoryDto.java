package seoul.AutoEveryDay.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChargeHistoryDto {
    private Long id;
    @NotNull
    private Long carId;
    @NotNull
    private Long chargeSpotId;
    @NotNull
    private Integer amount;
}
