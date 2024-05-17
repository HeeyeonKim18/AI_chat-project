package beside2.ten039.controller;

import beside2.ten039.common.ApiResponses;
import beside2.ten039.dto.LastIdResult;
import beside2.ten039.dto.rollingpaper.PaperAllResponse;
import beside2.ten039.dto.rollingpaper.SavedRollingPaperRequest;
import beside2.ten039.dto.rollingpaper.SavedRollingPaperResponse;
import beside2.ten039.service.RollingPaperService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

@Tag(name = "RollingPaper API", description = "언제나 널 생각해 페이지 관련 api")
@RestController
@RequiredArgsConstructor
@RequestMapping("/paper")
public class RollingPaperController {

    private final RollingPaperService rollingPaperService;

    private static final int PAGE_SIZE = 10;

    @Operation(summary = "롤링페이퍼 작성", description = "롤링페이퍼를 작성합니다.")
    @Parameter(name = "memberId", description = "사용자 아이디")
    @Parameter(name = "message", description = "롤링페이퍼 내용")
    @ApiResponse(responseCode = "200", description = "롤링페이퍼 저장 성공",
            content = @Content(schema = @Schema(implementation = SavedRollingPaperResponse.class)))
    @PostMapping("/{memberId}")
    public ApiResponses writeRollingPaper(@PathVariable("memberId") Long memberId, @RequestBody SavedRollingPaperRequest message) {
        return ApiResponses.builder()
                .msg("롤링페이퍼 내용을 저장합니다.")
                .data(rollingPaperService.savePaper(memberId, message)).build();
    }

    @Operation(summary = "롤링페이퍼 전체 조회", description = "롤링페이퍼를 조회합니다.")
    @Parameter(name = "lastPaperId", description = "커서로 사용하는 데이터 식별자")
    @ApiResponse(responseCode = "200", description = "롤링페이퍼 조회 성공",
            content = @Content(schema = @Schema(implementation = PaperAllResponse.class))) // 수정
    @GetMapping("/list")
    public ApiResponses getList(@RequestParam(value = "lastPaperId", required = false) Long lastPaperId) {
        return ApiResponses.builder()
                .msg("롤링페이퍼 내용을 조회합니다.")
                .data(rollingPaperService.findAllByCursor(lastPaperId, PageRequest.of(0, PAGE_SIZE)))
                .build();
    }

    @Operation(summary = "마지막 롤링페이퍼 아이디", description = "마지막 롤링페이퍼를 조회합니다.")
    @ApiResponse(responseCode = "200", description = "롤링페이퍼 마지막 아이디 조회 성공",
            content = @Content(schema = @Schema(implementation = LastIdResult.class))) // 수정
    @GetMapping("/last")
    public ApiResponses getLastPaperId(){
        return ApiResponses.builder()
                .msg("마지막 롤링페이퍼 아이디를 조회합니다.")
                .data(rollingPaperService.findLastPaperId())
                .build();
    }
}
