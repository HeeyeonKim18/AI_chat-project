package beside2.ten039.dto.chat;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Schema(description = "저장할 채팅 메세지")
public class ChatRequest {

    @Schema(description = "채팅 메세지")
    private String message;
}
