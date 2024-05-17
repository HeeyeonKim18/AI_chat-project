package beside2.ten039.service;

import beside2.ten039.domain.Member;
import beside2.ten039.domain.RollingPaper;
import beside2.ten039.dto.CursorResult;
import beside2.ten039.dto.LastIdResult;
import beside2.ten039.dto.rollingpaper.PaperAllResponse;
import beside2.ten039.dto.rollingpaper.SavedRollingPaperRequest;
import beside2.ten039.dto.rollingpaper.SavedRollingPaperResponse;
import beside2.ten039.exception.NotFoundException;
import beside2.ten039.repository.MemberRepository;
import beside2.ten039.repository.RollingPaperRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RollingPaperService {

    private final RollingPaperRepository rollingPaperRepository;
    private final MemberRepository memberRepository;

    /**
     * 롤링페이퍼 메세지 저장 로직
     *
     * @param memberId
     * @param request
     * @return SavedRollingPaperResponse
     */
    @Transactional
    public SavedRollingPaperResponse savePaper(Long memberId, SavedRollingPaperRequest request) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new NotFoundException("사용자 정보를 찾을 수 없습니다."));

        RollingPaper paper = RollingPaper.builder()
                .member(member)
                .message(request.getMessage())
                .build();

        RollingPaper savedPaper = rollingPaperRepository.save(paper);

        return new SavedRollingPaperResponse(savedPaper.getId(), member.getRandomName());
    }

    /**
     * 롤링페이퍼 조회 with paging 로직
     *
     * @param lastPaperId
     * @param size
     * @return GetPaperResponse
     */
    public List<PaperAllResponse> findAll(Long lastPaperId, int size) {
        RollingPaper paper = rollingPaperRepository.findById(lastPaperId)
                .orElseThrow(() -> new NotFoundException("존재하지 않는 rollingPaperId 입니다."));
        PageRequest pageRequest = PageRequest.of(0, size);
        Page<RollingPaper> pages = rollingPaperRepository.findAllByIdLessThanOrderByIdDesc(paper.getId(), pageRequest);
        List<RollingPaper> list = pages.getContent();

        return list.stream().map(PaperAllResponse::toDto).collect(Collectors.toList());

//        ScrollPaginationCollection<RollingPaper> paperCursor = ScrollPaginationCollection.of(list, size);
//        GetPaperResponse response = GetPaperResponse.of(paperCursor, rollingPaperRepository.countAllById(paper.getId()));
//        return response;
    }

    /**
     * 롤링페이퍼 조회 v3
     * @param lastPaperId
     * @param pageable
     * @return
     */
    public CursorResult<PaperAllResponse> findAllByCursor(Long lastPaperId, Pageable pageable) {
        List<PaperAllResponse> papers = getPapers(lastPaperId, pageable);
        final Long lastIdOfList = papers.isEmpty() ?
                null : papers.get(papers.size() - 1).getRollingPaperId();

        return new CursorResult<>(papers, hasNext(lastIdOfList));

    }

    private List<PaperAllResponse> getPapers(Long id, Pageable page) {
        List<RollingPaper> getPaperList;
        if (id == null) {
            getPaperList = rollingPaperRepository.findAllByOrderByIdDesc(page);
        } else {
            getPaperList = rollingPaperRepository.findByIdLessThanOrderByIdDesc(id, page);
        }
        return getPaperList.stream().map(paper -> PaperAllResponse.toDto(paper)).collect(Collectors.toList());
    }

    private Boolean hasNext(Long id) {
        if (id == null) return false;
        return rollingPaperRepository.existsByIdLessThan(id);
    }

    public LastIdResult findLastPaperId(){
        RollingPaper paper = rollingPaperRepository.lastId();
        return new LastIdResult(paper.getId());
    }

}
