package beside2.ten039.controller;

import beside2.ten039.common.ApiResponses;
import beside2.ten039.dto.calendar.CalendarDDayRequest;
import beside2.ten039.dto.calendar.LatestScheduleResponse;
import beside2.ten039.service.CalendarService;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Calendar API", description = "일정 관련하여 정보를 제공하는 api")
@RestController
@RequiredArgsConstructor
@RequestMapping("/calendar")
public class CalendarController {

    private final CalendarService calendarService;

    @Operation(summary = "기념일 조회", description = "다가오는 기념일 중 가장 빠른 기념일을 조회합니다.")
    @ApiResponse(responseCode = "200", description = "최신 기념일 조회 성공",
            content = @Content(schema = @Schema(implementation = LatestScheduleResponse.class)))
    @GetMapping("/dday")
    public ApiResponses getDDay() {
        return ApiResponses.builder()
                .msg("푸바오 기념일을 불러옵니다.")
                .data(calendarService.findLatestSchedule())
                .build();
    }

    @Hidden
    @PostMapping("/dday")
    public ApiResponses saveDDay(@RequestBody CalendarDDayRequest request) {
        return ApiResponses.builder()
                .msg("저장")
                .data(calendarService.savedDate(request))
                .build();
    }
}
