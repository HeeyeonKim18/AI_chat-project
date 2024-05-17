package beside2.ten039.dto.rollingpaper;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "롤링페이퍼 저장 응답 dto")
public class SavedRollingPaperResponse {

    @Schema(description = "저장된 롤링페이퍼 아이디")
    private Long rollingPaperId;

    @Schema(description = "사용자 닉네임")
    private String randomName;


}
