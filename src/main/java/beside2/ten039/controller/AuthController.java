package beside2.ten039.controller;

import beside2.ten039.common.ApiResponses;
import beside2.ten039.dto.oauth.AuthTokenResponse;
import beside2.ten039.dto.oauth.OauthInfoRequest;
import beside2.ten039.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Oauth API", description = "카카오 로그인 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/oauth")
public class AuthController {

    private final MemberService memberService;

    @Operation(summary = "사용자 저장 및 토큰 발급",
            description = "전달받은 인가코드로 사용자를 저장하고 토큰을 발급합니다. 로그인을 제외한 모든 api 헤더에는 grantType + token 포함해야 함",
            responses = { @ApiResponse(responseCode = "200", description = "로그인 성공",
                    content = @Content(schema = @Schema(implementation = AuthTokenResponse.class)))})
    @Parameter(name = "authorizationCode", description = "카카오에서 전달 받은 인가코드")
    @PostMapping("/authorize")
    public ApiResponses loginKakao(@RequestBody OauthInfoRequest params) {
        return ApiResponses.builder()
                .msg("사용자 로그인을 성공하였습니다.")
                .data(memberService.login(params))
                .build();
    }


}
