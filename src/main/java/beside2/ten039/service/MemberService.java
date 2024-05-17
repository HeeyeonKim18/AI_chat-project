package beside2.ten039.service;

import beside2.ten039.config.oauth.KakaoApiClient;
import beside2.ten039.config.oauth.TokenProvider;
import beside2.ten039.domain.Member;
import beside2.ten039.dto.calendar.DDayResponse;
import beside2.ten039.dto.oauth.AuthTokenResponse;
import beside2.ten039.dto.oauth.MemberDto;
import beside2.ten039.dto.oauth.OauthInfoRequest;
import beside2.ten039.dto.oauth.OauthInfoResponse;
import beside2.ten039.exception.InternalServerErrorException;
import beside2.ten039.exception.NotFoundException;
import beside2.ten039.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class MemberService {

    private final MemberRepository memberRepository;
    private final KakaoApiClient kakaoApiClient;
    private final TokenProvider tokenProvider;

    private static final String grantType = "Bearer ";
    private static final Long accessTokenValidationTime = 30 * 60 * 1000L;   //30분
    private static final Long refreshTokenValidationTime = 7 * 24 * 60 * 60 * 1000L; //7일

    /**
     * 소셜 로그인 로직: validate token -> findOrCreateMember -> make token
     * @param params
     * @return AuthTokenResponse
     */
    public AuthTokenResponse login(OauthInfoRequest params) {
        String accessToken = kakaoApiClient.requestAccessToken(params);
        OauthInfoResponse oauthInfoResponse = kakaoApiClient.requestOauthInfo(accessToken);
        MemberDto member = findOrCreateMember(oauthInfoResponse);
        String getAccessToken = tokenProvider.generateToken(member.getMemberId(), accessTokenValidationTime);
        String getRefreshToken = tokenProvider.generateToken(member.getMemberId(), refreshTokenValidationTime);
        return new AuthTokenResponse(getAccessToken, getRefreshToken, grantType, member.getMemberId(), member.getRandomName());
    }

    /**
     * 사용자 존재 -> find, 존재 x -> newMember()
     * @param response
     * @return MemberDto
     */
    public MemberDto findOrCreateMember(OauthInfoResponse response) {
        Optional<Member> member = memberRepository.findByEmail(response.getEmail());
        if (member.isEmpty()) {
            MemberDto memberDto = newMember(response);
            return memberDto;
        }else {
            Member findMember = member.get();
            return new MemberDto(findMember.getId(), findMember.getRandomName());
        }
    }

    /**
     * 사용자 정보 db 저장
     * @param response
     * @return MemberDto
     */
    public MemberDto newMember(OauthInfoResponse response) {

        String randomName = "";

        while (true) {
            randomName = RandomStringUtils.random(10, true, true);
            Optional<Member> memberByRandomName = memberRepository.findByRandomName(randomName);
            if (memberByRandomName.isEmpty()) break;
        }

        Member member = Member.builder()
                .email(response.getEmail())
                .nickname(response.getNickname())
                .randomName(randomName)
                .build();

        Member savedMember = memberRepository.save(member);

        if (savedMember.getId() == null) {
            throw new InternalServerErrorException("회원 정보가 저장되지 않았습니다.");
        }

        return new MemberDto(savedMember.getId(), savedMember.getRandomName());
    }

    /**
     * 푸바오 만난 날: 현재일 - 가입일 = 디데이
     * @param memberId
     * @return DDayResponse
     */
    @Transactional(readOnly = true)
    public DDayResponse getDDay(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new NotFoundException("조회한 회원의 정보가 존재하지 않습니다."));

        LocalDate createdDate = member.getCreatedDate();
        long dDay = LocalDate.now().toEpochDay() - createdDate.toEpochDay();
        return new DDayResponse(dDay + 1);
    }
}
