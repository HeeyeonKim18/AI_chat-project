package beside2.ten039.dto.rollingpaper;

import beside2.ten039.domain.Member;
import beside2.ten039.domain.RollingPaper;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@ToString
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "전체 조회 응답 dto")
public class PaperAllResponse {

    @Schema(description = "롤링페이퍼 아이디")
    private Long rollingPaperId;

    @Schema(description = "사용자 아이디")
    private long memberId;

    @Schema(description = "사용자 닉네임")
    private String randomName;

    @Schema(description = "롤링페이퍼 메세지")
    private String content;

    public static PaperAllResponse of(RollingPaper rollingPaper) {
        return toDto(rollingPaper);
    }

    public static PaperAllResponse toDto(RollingPaper rollingPaper) {
        return PaperAllResponse.builder()
                .rollingPaperId(rollingPaper.getId())
                .memberId(rollingPaper.getMember().getId())
                .randomName(rollingPaper.getMember().getRandomName())
                .content(rollingPaper.getMessage())
                .build();
    }

}
