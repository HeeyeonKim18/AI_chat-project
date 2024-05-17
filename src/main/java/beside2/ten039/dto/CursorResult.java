package beside2.ten039.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@Schema(description = "롤링페이퍼 조회 dto")
public class CursorResult<T> {

    private List<T> values;

    @Schema(description = "다음 페이지 여부 확인")
    private Boolean hasNext;

    public CursorResult(List<T> values, Boolean hasNext) {
        this.values = values;
        this.hasNext = hasNext;
    }
}
