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
public class RentCarDto {
    private Long id;
    @NotNull
    private Long carId;
    @NotNull
    private LocalDate pickupDate;
    @NotNull
    private LocalDate returnDate;
}
