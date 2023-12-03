package seoul.AutoEveryDay.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JsonBody {
    private int status;
    private String message;
    private Object data;
}
