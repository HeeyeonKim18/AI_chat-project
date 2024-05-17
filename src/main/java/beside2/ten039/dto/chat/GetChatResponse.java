package beside2.ten039.dto.chat;

import beside2.ten039.config.utils.ScrollPaginationCollection;
import beside2.ten039.domain.Chat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@ToString
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Schema(description = "채팅 내역 조회")
public class GetChatResponse {

    private static final long LAST_CURSOR = -1L;

    private List<ChatAllResponse> contents = new ArrayList<>();

    @Schema(description = "조회 가능한 데이터의 총 개수")
    private long totalElements;

    @Schema(description = "다음 스크롤에서 사용할 커서의 값으로 다음 스크롤이 존재하지 않을 경우 -1")
    private long nextCursor;

    private GetChatResponse(List<ChatAllResponse> contents, long totalElements, long nextCursor) {
        this.contents = contents;
        this.totalElements = totalElements;
        this.nextCursor = nextCursor;
    }

    public static GetChatResponse of(ScrollPaginationCollection<Chat> chatScroll, long totalElements) {
        if (chatScroll.isLastScroll()) {
            return GetChatResponse.newLastScroll(chatScroll.getCurrentScrollItems(),totalElements);
        }
        return GetChatResponse.newScrollHasNext(chatScroll.getCurrentScrollItems(),totalElements, chatScroll.getNextCursor().getId());
    }

    private static GetChatResponse newLastScroll(List<Chat> chatScroll, long totalElements) {
        return newScrollHasNext(chatScroll, totalElements, LAST_CURSOR);
    }

    private static GetChatResponse newScrollHasNext(List<Chat> chatScroll, long totalElements, long nextCursor) {
        return new GetChatResponse(getContents(chatScroll), totalElements, nextCursor);
    }

    private static List<ChatAllResponse> getContents(List<Chat> chatScroll) {
        return chatScroll.stream()
                .map(chat -> ChatAllResponse.of(chat))
                .collect(Collectors.toList());
    }
}
