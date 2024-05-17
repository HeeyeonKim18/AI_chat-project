package beside2.ten039.config.interceptor;

import beside2.ten039.config.oauth.TokenProvider;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

@RequiredArgsConstructor
public class LoginInterceptor implements HandlerInterceptor {

    private final TokenProvider tokenProvider;
    private final static String HEADER_AUTHORIZATION = "Authorization";
    private final static String TOKEN_PRIFIX = "Bearer ";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String authorizationHeader = request.getHeader(HEADER_AUTHORIZATION);
        String token = getAccessToken(authorizationHeader);

        if (StringUtils.hasText(token) && tokenProvider.validToken(token)) {
//            Long userId = tokenProvider.getUserId(token);
//            Member member = memberRepository.findById(userId)
//                    .orElseThrow(() -> new RuntimeException("사용자 아이디가 존재하지 않습니다. "));
//
//            if (member.getId() == userId) {
//                return true;
//            }
            return true;
        }
        throw new IllegalAccessException("유효하지 않은 토큰 값으로 접근할 수 없습니다.");
    }

    private String getAccessToken(String authorizationHeader) {
        if (authorizationHeader != null && authorizationHeader.startsWith(TOKEN_PRIFIX)) {
            return authorizationHeader.substring(TOKEN_PRIFIX.length());
        }
        return null;
    }
}