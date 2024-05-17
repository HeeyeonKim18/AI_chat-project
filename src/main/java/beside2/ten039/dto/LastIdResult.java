package beside2.ten039.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "마지막 아이디 조회 dto")
public class LastIdResult{

    @Schema(description = "마지막으로 저장된 아이디")
    private Long lastId;
}
