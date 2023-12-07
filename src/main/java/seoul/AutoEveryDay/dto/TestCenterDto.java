package seoul.AutoEveryDay.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TestCenterDto {
    @NotBlank
    private String name;
    @NotBlank
    private String address;
    @NotBlank
    private Integer capacity;
}
