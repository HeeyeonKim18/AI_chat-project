package beside2.ten039.controller;

import beside2.ten039.common.ApiResponses;
import beside2.ten039.dto.LastIdResult;
import beside2.ten039.dto.chat.*;
import beside2.ten039.service.ChatService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@Tag(name = "Chat API", description = "채팅 관련 api 입니다.")
@RestController
@RequiredArgsConstructor
@RequestMapping("/chat")
public class ChatController {

    private final ChatService chatService;

    private static final int PAGE_SIZE = 10;

    @Operation(summary = "사용자 채팅", description = "사용자 메세지를 저장하고 ai 답변을 저장 후 제공합니다.")
    @ApiResponse(responseCode = "200", description = "메세지 저장 및 답변 성공",
            content = @Content(schema = @Schema(implementation = SavedChatResponse.class)))
    @Parameter(name = "message", description = "사용자가 작성한 메세지")
    @Parameter(name = "memberId", description = "사용자 아이디")
    @PostMapping("/{memberId}")
    public ApiResponses sendMessage(@PathVariable("memberId") Long memberId,
                                    @RequestBody ChatRequest chatRequest) throws IOException {

        return ApiResponses.builder()
                .msg("사용자 답변이 저장되었습니다.")
                .data(chatService.saveChat(memberId, chatRequest))
                .build();
    }

    @Operation(summary = "최근 메세지", description = "가장 최근 메세지를 보여줍니다.")
    @Parameter(name = "memberId", description = "사용자 아이디")
    @ApiResponse(responseCode = "200", description = "최근 메세지 응답 성공",
            content = @Content(schema = @Schema(implementation = LastMessageResponse.class)))
    @GetMapping("/message/{memberId}")
    public ApiResponses getLastMessage(@PathVariable("memberId") Long memberId) {

        return ApiResponses.builder()
                .msg("최근 메세지를 가져옵니다.")
                .data(chatService.getLastMessage(memberId))
                .build();
    }

    // 조회(페이지네이션)
    @Operation(summary = "사용자 메세지 내역 조회", description = "사용자가 ai와 나눈 메세지를 조회합니다.")
    @Parameter(name = "memberId", description = "사용자 아이디")
    @Parameter(name = "lastChatId", description = "커서로 사용하는 데이터 식별자")
    @ApiResponse(responseCode = "200", description = "사용자 메세지 내역 조회 성공",
            content = @Content(schema = @Schema(implementation = GetChatResponse.class)))
    @GetMapping("/list/{memberId}")
    public ApiResponses getListByMember(@PathVariable("memberId") Long memberId,
                                        @RequestParam(value = "lastChatId", required = false) Long lastChatId) {
        return ApiResponses.builder()
                .msg("member " + memberId + "의 메세지를 조회합니다.")
                .data(chatService.findAllByCursor(memberId, lastChatId, PageRequest.of(0, PAGE_SIZE)))
                .build();
    }

    @Operation(summary = "마지막 chat 아이디", description = "마지막 채팅아이디를 조회합니다.")
    @ApiResponse(responseCode = "200", description = "채팅 마지막 아이디 조회 성공",
            content = @Content(schema = @Schema(implementation = LastIdResult.class))) // 수정
    @GetMapping("/last")
    public ApiResponses getLastPaperId(){
        return ApiResponses.builder()
                .msg("마지막 채팅 아이디를 조회합니다.")
                .data(chatService.findLastChatId())
                .build();
    }

}
