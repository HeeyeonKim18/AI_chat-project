package beside2.ten039.dto.calendar;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "푸바오 만난날 응답 dto")
public class DDayResponse {

    @Schema(description = "디데이 값")
    private Long dDay;
}
