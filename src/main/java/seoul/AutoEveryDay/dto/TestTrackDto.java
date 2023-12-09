package seoul.AutoEveryDay.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TestTrackDto {
    private Long id;
    @NotBlank
    private String name;
    @NotBlank
    private String description;
}
