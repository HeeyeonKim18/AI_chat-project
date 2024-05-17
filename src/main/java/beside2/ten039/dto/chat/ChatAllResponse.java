package beside2.ten039.dto.chat;

import beside2.ten039.domain.Chat;
import beside2.ten039.domain.Role;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@ToString
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "채팅 내역 정보")
public class ChatAllResponse {

    @Schema(description = "커서 식별자, 채팅 id")
    private Long chatId;

    @Schema(description = "역할")
    private Role role;

    @Schema(description = "채팅 메세지")
    private String message;

    @Schema(description = "시간")
    private String chatTime;

    public static ChatAllResponse of(Chat chat) {
        return toDto(chat);
    }

    public static ChatAllResponse toDto(Chat chat) {
        return ChatAllResponse.builder()
                .chatId(chat.getId())
                .role(chat.getRole())
                .message(chat.getMessage())
                .chatTime(chat.getCreatedDate().format(DateTimeFormatter.ofPattern("HH:mm")))
                .build();
    }

}
