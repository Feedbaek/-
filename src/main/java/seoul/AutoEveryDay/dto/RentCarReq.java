package seoul.AutoEveryDay.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RentCarReq {
    @NotBlank
    private String number;
    @NotBlank
    private LocalDate pickupDate;
    @NotBlank
    private LocalDate returnDate;
}
