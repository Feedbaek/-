package seoul.AutoEveryDay.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChargeHistoryDto {
    private Long id;
    @NotBlank
    private Long carId;
    @NotBlank
    private Long chargeSpotId;
    @NotBlank
    private Long amount;
}
