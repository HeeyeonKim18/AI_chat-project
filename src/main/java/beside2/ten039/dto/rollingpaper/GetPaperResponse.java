package beside2.ten039.dto.rollingpaper;

import beside2.ten039.config.utils.ScrollPaginationCollection;
import beside2.ten039.domain.RollingPaper;
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
@Schema(description = "롤링페이퍼 전체 조회")
public class GetPaperResponse {

    private static final long LAST_CURSOR = -1L;

    private List<PaperAllResponse> contents = new ArrayList<>();

    @Schema(description = "조회 가능한 데이터의 총 개수")
    private long totalElements;

    @Schema(description = "다음 스크롤에서 사용할 커서의 값으로 다음 스크롤이 존재하지 않을 경우 -1")
    private long nextCursor;

    private GetPaperResponse(List<PaperAllResponse> contents, long totalElements, long nextCursor) {
        this.contents = contents;
        this.totalElements = totalElements;
        this.nextCursor = nextCursor;
    }

    public static GetPaperResponse of(ScrollPaginationCollection<RollingPaper> paperScroll, long totalElements) {
        if (paperScroll.isLastScroll()) {
            return GetPaperResponse.newLastScroll(paperScroll.getCurrentScrollItems(), totalElements);
        }
        return GetPaperResponse.newScrollHasNext(paperScroll.getCurrentScrollItems(), totalElements, paperScroll.getNextCursor().getId());
    }

    private static GetPaperResponse newLastScroll(List<RollingPaper> rollingPapers, long totalElements) {
        return newScrollHasNext(rollingPapers, totalElements, LAST_CURSOR);
    }

    private static GetPaperResponse newScrollHasNext(List<RollingPaper> rollingPapers, long totalElements, long nextCursor) {
        return new GetPaperResponse(getContents(rollingPapers), totalElements, nextCursor);
    }

    private static List<PaperAllResponse> getContents(List<RollingPaper> rollingPapers) {
        return rollingPapers.stream()
                .map(paper -> PaperAllResponse.of(paper))
                .collect(Collectors.toList());
    }
}
