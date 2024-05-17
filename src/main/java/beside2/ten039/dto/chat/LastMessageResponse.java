package beside2.ten039.dto.chat;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "최근 메세지 응답 dto")
public class LastMessageResponse {

    @Schema(description = "최근 메세지 id")
    private Long chatId;

    @Schema(description = "최근 메세지 내용")
    private String lastMessage;
}
