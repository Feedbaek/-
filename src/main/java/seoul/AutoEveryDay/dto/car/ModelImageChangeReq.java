package seoul.AutoEveryDay.dto.car;

import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ModelImageChangeReq {
    private Long id;
    private String name;
    @NotBlank
    private MultipartFile image;
}
