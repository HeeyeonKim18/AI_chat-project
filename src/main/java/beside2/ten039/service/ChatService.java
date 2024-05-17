package beside2.ten039.service;

import beside2.ten039.config.utils.ScrollPaginationCollection;
import beside2.ten039.domain.Chat;
import beside2.ten039.domain.Member;
import beside2.ten039.domain.Role;
import beside2.ten039.domain.RollingPaper;
import beside2.ten039.dto.CursorResult;
import beside2.ten039.dto.LastIdResult;
import beside2.ten039.dto.chat.*;
import beside2.ten039.exception.NotFoundException;
import beside2.ten039.repository.ChatRepository;
import beside2.ten039.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ChatService {

    private final ChatRepository chatRepository;
    private final MemberRepository memberRepository;
    private final RestTemplate restTemplate;

    @Value("${clova.chat.secret}")
    private String secretKey;

    @Value("${clova.chat.api-url}")
    private String apiUrl;

    @Value("${clova.chat.api-key}")
    private String apiKey;

    @Value("${clova.chat.request-id}")
    private String requestId;


    private static String chatbotMessage = "";

    /**
     * 조회 V3
     * @param memberId
     * @param lastChatId
     * @param pageable
     * @return
     */
    public CursorResult<ChatAllResponse> findAllByCursor(long memberId, Long lastChatId, Pageable pageable) {
        List<ChatAllResponse> chat = getChat(memberId, lastChatId, pageable);
        final Long lastIdOfList = chat.isEmpty() ?
                null : chat.get(chat.size() - 1).getChatId();

        return new CursorResult<>(chat, hasNext(lastIdOfList));

    }

    private List<ChatAllResponse> getChat(Long memberId, Long id, Pageable page) {
        Member member = getMember(memberId);
        List<Chat> getChatList;
        if (id == null) {
            getChatList = chatRepository.findAllByMemberOrderByIdDesc(member, page);
        } else {
            getChatList = chatRepository.findByMemberAndIdLessThanOrderByIdDesc(member, id, page);
        }
        return getChatList.stream().map(chat -> ChatAllResponse.toDto(chat)).collect(Collectors.toList());
    }

    private Boolean hasNext(Long id) {
        if (id == null) return false;
        return chatRepository.existsByIdLessThan(id);
    }

    public LastIdResult findLastChatId(){
        Chat chat = chatRepository.lastId();
        return new LastIdResult(chat.getId());
    }

    /**
     * v1 조회 with paging
     *
     * @param memberId
     * @param lastChatId
     * @param size
     * @return ChatAllResponse without totalElement, nextCursor
     */
    @Transactional(readOnly = true)
    public List<ChatAllResponse> findAllMessageListByMember(Long memberId, Long lastChatId, int size) {
        Member member = getMember(memberId);

        PageRequest pageRequest = PageRequest.of(0, size);
        Page<Chat> pages = chatRepository.findAllByMemberAndIdLessThanOrderByIdDesc(member, lastChatId, pageRequest);
        List<Chat> list = pages.getContent();

        return list.stream()
                .map(ChatAllResponse::toDto)
                .collect(Collectors.toList());
    }

    /**
     * v2 조회 with paging
     *
     * @param memberId
     * @param lastChatId
     * @param size
     * @return GetChatResponse(ChatAllResponse, totalElement, nextCursor)
     */
    @Transactional(readOnly = true)
    public GetChatResponse findAllMessageByMember(Long memberId, Long lastChatId, int size) {
        Member member = getMember(memberId);
        Chat chat = getChat(lastChatId);

        PageRequest pageRequest = PageRequest.of(0, size + 1);
        Page<Chat> pages = chatRepository.findAllByMemberAndIdLessThanOrderByIdDesc(member, chat.getId(), pageRequest);
        List<Chat> list = pages.getContent();

        ScrollPaginationCollection<Chat> chatCursor = ScrollPaginationCollection.of(list, size);
        GetChatResponse response = GetChatResponse.of(chatCursor, chatRepository.countAllByMember(member));

        return response;
    }

    /**
     * 채팅 정보 조회하기
     *
     * @param lastChatId
     * @return
     */
    private Chat getChat(Long lastChatId) {
        return chatRepository.findById(lastChatId)
                .orElseThrow(() -> new NotFoundException("존재하지 않는 채팅 id 입니다."));
    }


    /**
     * 마지막 메세지 출력
     *
     * @param memberId
     * @return id, ai 최근 메세지
     */
    @Transactional(readOnly = true)
    public LastMessageResponse getLastMessage(Long memberId) {
        Member member = getMember(memberId);
        Chat chat = chatRepository.lastMessage(member, Role.assistant);
        return new LastMessageResponse(chat.getId(), chat.getMessage());
    }

    /**
     * 사용자 정보 조회하기
     *
     * @param memberId
     * @return member
     */
    private Member getMember(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new NotFoundException("해당 회원은 존재하지 않습니다."));
        return member;
    }

    /**
     * 사용자 채팅을 저장하고 서버와 통신하여 ai 답변을 얻음
     *
     * @param memberId
     * @param chatRequest
     * @return SavedChatResponse(userChatId, aiChatId, message)
     */
    public SavedChatResponse saveChat(Long memberId, ChatRequest chatRequest) {

        SavedChatResponse chatResponse = new SavedChatResponse();

        // 서버 -> 서버 통신을 위한 헤더, 바디 포함한 HttpEntity
        HttpEntity request = getHttpEntity(chatRequest);

        // post 형식으로 통신
        Map response = restTemplate.postForObject(apiUrl, request, Map.class);

        // 엔티티 조회
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new NotFoundException("정보에 맞는 회원이 존재하지 않습니다."));

        // chatMessage 저장하기 "user"
        Chat savedUserChat = buildChatByUser(chatRequest, member);

        // json parsing
        String[] contents = jsonParsing(response);

        // chatMessage 저장하기 "assistant"
        List<Long> savedAssiChatIdList = new ArrayList<>();
        List<String> contentList = new ArrayList<>();
        List<String> assiChatTime = new ArrayList<>();
        for (String content : contents) {
            Chat savedAssiChat = buildChatByAssi(member, content);
            savedAssiChatIdList.add(savedAssiChat.getId());
            contentList.add(content);
            assiChatTime.add(savedAssiChat.getCreatedDate().format(DateTimeFormatter.ofPattern("HH:mm")));
        }

        String userChatTime = savedUserChat.getCreatedDate().format(DateTimeFormatter.ofPattern("HH:mm"));

        return new SavedChatResponse(savedUserChat.getId(), userChatTime,
                savedAssiChatIdList, assiChatTime, contentList);
    }

    /**
     * 헤더 및 바디 구성 for 서버 -> 서버 통신
     *
     * @param chatRequest
     */
    private HttpEntity getHttpEntity(ChatRequest chatRequest) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        httpHeaders.set("X-NCP-CLOVASTUDIO-API-KEY", apiKey);
        httpHeaders.set("X-NCP-APIGW-API-KEY", secretKey);
        httpHeaders.set("X-NCP-CLOVASTUDIO-REQUEST-ID", requestId);

        HttpEntity request = new HttpEntity(getReqMessage(chatRequest.getMessage()), httpHeaders);

        System.out.println(request.getBody());
        return request;
    }

    /**
     * request의 바디에 들어갈 내용 parsing
     *
     * @param chatMessage
     */
    private String getReqMessage(String chatMessage) {
        String requestBody = "";

        try {
            JSONObject obj = new JSONObject();
            JSONObject data_obj = new JSONObject();

            data_obj.put("role", Role.user);
            data_obj.put("content", chatMessage);

            JSONArray message_array = new JSONArray();
            message_array.put(data_obj);

            obj.put("messages", message_array);

            requestBody = obj.toString();
        } catch (Exception e) {
            System.out.println("## Exception : " + e);
        }
        return requestBody;
    }

    /**
     * 응답 값 json parsing
     *
     * @param response
     */
    private static String[] jsonParsing(Map response) {
        JSONObject resultObj = new JSONObject(response);
        JSONObject result = resultObj.getJSONObject("result");
        JSONObject messageObj = result.getJSONObject("message");
        String content = messageObj.getString("content");
        String[] contents = content.split("\\n\\n");
        return contents;
    }

    /**
     * 어시스턴드 값 저장
     *
     * @param member
     * @param content
     */
    private Chat buildChatByAssi(Member member, String content) {
        Chat chatByAssi = Chat.builder()
                .role(Role.assistant)
                .member(member)
                .message(content)
                .build();

        return chatRepository.save(chatByAssi);
    }

    /**
     * 사용자 값 저장
     *
     * @param chatRequest
     * @param member
     */
    private Chat buildChatByUser(ChatRequest chatRequest, Member member) {
        Chat chatByUser = Chat.builder()
                .role(Role.user)
                .member(member)
                .message(chatRequest.getMessage())
                .build();

        return chatRepository.save(chatByUser);
    }
}
