package beside2.ten039.config.oauth;

import beside2.ten039.dto.oauth.OauthInfoRequest;
import beside2.ten039.dto.oauth.OauthInfoResponse;
import beside2.ten039.dto.oauth.OauthTokensDto;
import beside2.ten039.exception.KakaoServerErrorException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Component
@RequiredArgsConstructor
@Slf4j
public class KakaoApiClient {

    private static final String GRANT_TYPE = "authorization_code";

    @Value("${oauth2.kakao.provider.kakao.token-uri}")
    private String tokenUrl;
    @Value("${oauth2.kakao.provider.kakao.user-info-uri}")
    private String userInfoUrl;
    @Value("${oauth2.kakao.client-id}")
    private String clientId;
    @Value("${oauth2.kakao.client-secret}")
    private String clientSecret;
    @Value("${oauth2.kakao.redirect-uri}")
    private String redirectUrl;

    private final RestTemplate restTemplate;

    public String requestAccessToken(OauthInfoRequest params) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> body = params.makeBody();
        body.add("grant_type", GRANT_TYPE);
        body.add("client_id", clientId);
        body.add("client_secret", clientSecret);
        body.add("redirect_uri", redirectUrl);

        HttpEntity request = new HttpEntity(body, httpHeaders);

        if (request == null) {
            throw new KakaoServerErrorException("Kakao 토큰을 요청할 수 없습니다.");
        }

        OauthTokensDto response = restTemplate.postForObject(tokenUrl, request, OauthTokensDto.class);
        log.info("idToken ={}", response.getIdToken());

        if (response == null) {
            throw new KakaoServerErrorException("Kakao 토큰을 발급 받을 수 없습니다.");
        }

        return response.getAccessToken();
    }

    public OauthInfoResponse requestOauthInfo(String accessToken) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        httpHeaders.set("Authorization", "Bearer " + accessToken);

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("property_keys", "[\"kakao_account.email\", \"kakao_account.profile\"]");

        HttpEntity request = new HttpEntity(body, httpHeaders);

        if (request == null) {
            throw new KakaoServerErrorException("사용자 정보를 Kakao Auth Server로부터 요청할 수 없습니다.");
        }
        OauthInfoResponse response = restTemplate.postForObject(userInfoUrl, request, OauthInfoResponse.class);
        return response;
    }


}
