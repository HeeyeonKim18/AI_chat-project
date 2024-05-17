package beside2.ten039.dto.calendar;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Schema(description = "최신 일정 디데이 응답 dto")
public class LatestScheduleResponse {

    @Schema(description = "캘린더 id")
    private Long calendarId;

    @Schema(description = "캘린더 정보")
    private String info;

    @Schema(description = "디데이 날짜")
    private Long countDDay;
}
