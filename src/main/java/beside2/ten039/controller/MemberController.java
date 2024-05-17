package beside2.ten039.controller;

import beside2.ten039.common.ApiResponses;
import beside2.ten039.dto.calendar.DDayResponse;
import beside2.ten039.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Member API", description = "사용자 관련(디데이) 등의 api 제공")
@RestController
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController {

    private final MemberService memberService;

    @Operation(summary = "푸바오 만난 날", description = "현재 - 가입 일자 = 디데이 ")
    @Parameter(name = "memberId", description = "사용자 아이디")
    @ApiResponse(responseCode = "200", description = "디데이 조회 성공",
            content = @Content(schema = @Schema(implementation = DDayResponse.class)))
    @GetMapping("/dday/{memberId}")
    public ApiResponses getDDay(@PathVariable("memberId") Long memberId) {
        return ApiResponses.builder()
                .msg("푸바오 만난 날을 계산합니다.")
                .data(memberService.getDDay(memberId))
                .build();
    }
}
