package seoul.AutoEveryDay.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChargeDto {
    private Long id;
    @NotBlank
    private String carNumber;
    @NotBlank
    private String chargeSpotName;
    @NotBlank
    private Long amount;
}
