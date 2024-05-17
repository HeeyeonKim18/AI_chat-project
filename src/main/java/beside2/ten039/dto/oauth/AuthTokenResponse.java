package beside2.ten039.dto.oauth;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "로그인 응답 dto")
public class AuthTokenResponse {

    @Schema(description = "엑세스 토큰(30분)")
    private String accessToken;
    @Schema(description = "리프레시 토근(7일)")
    private String refreshToken;
    @Schema(description = "토큰 인증 방식")
    private String grantType;
    @Schema(description = "사용자 아이디")
    private Long memberId;
    @Schema(description = "사용자 닉네임")
    private String randomName;

}
