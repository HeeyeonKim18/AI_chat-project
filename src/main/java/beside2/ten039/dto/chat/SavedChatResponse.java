package beside2.ten039.dto.chat;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.List;

@ToString
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "사용자 메세지 저장 & ai 답변 응답")
public class SavedChatResponse {

    @Schema(description = "저장된 사용자 메세지 아이디")
    private Long savedUserChatId;

    @Schema(description = "사용자가 메세지를 보낸 시간")
    private String userChatTime;

    @Schema(description = "저장된 ai 메세지 아이디")
    private List<Long> savedAssiChatId;

    @Schema(description = "ai가 답변을 보낸 시간")
    private List<String> assiChatTime;

    @Schema(description = "ai 응답 메세지")
    private List<String> messageResponse;

}
