package net.chamman.shoppingmall_admin.security.filter;

import java.io.IOException;
import java.util.stream.Stream;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.WebUtils;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.chamman.shoppingmall_admin.config.SecurityConfig;
import net.chamman.shoppingmall_admin.security.jwt.JwtAuthenticationToken;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final AuthenticationManager authenticationManager;
    private final AntPathMatcher pathMatcher = new AntPathMatcher();

    private static final String[] EXCLUDED_URLS = Stream.concat(
            Stream.of(SecurityConfig.PUBLIC_ADMIN_URLS),
            Stream.of(SecurityConfig.STATIC_RESOURCES)).toArray(String[]::new);

    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain filterChain)
            throws ServletException, IOException {
        log.debug("* JwtAuthenticationFilter.doFilterInternal 시작.");

        // 1. AccessToken 추출 (기존 로직 재사용)
        String accessToken = getAccessToken(req);

        // 2. 토큰이 없으면 다음 필터로 (인증 안함)
        if (!StringUtils.hasText(accessToken)) {
            log.debug("* AccessToken 없음.");
            filterChain.doFilter(req, res);
            return;
        }

        try {
            // 3.  인증 매니저에게 인증 요청 (가장 큰 변화)
            //    인증 전 토큰 생성
            JwtAuthenticationToken authRequest = new JwtAuthenticationToken(accessToken);

            //    Manager에게 인증 위임 -> Manager는 JwtAuthenticationProvider를 호출
            Authentication authResult = authenticationManager.authenticate(authRequest);

            // 4. 인증 성공 시 SecurityContext에 저장
            SecurityContextHolder.getContext().setAuthentication(authResult);
            log.debug("* 인증 성공. SecurityContext에 저장: {}", authResult.getName());

        } catch (AuthenticationException e) {
            // 5.  인증 실패 시 (Provider가 던진 예외)
            //    JwtExpiredException -> CredentialsExpiredException
            //    JwtIllegalException -> BadCredentialsException
            log.warn("* JWT 인증 실패: {}", e.getMessage());
            SecurityContextHolder.clearContext(); // 컨텍스트 깨끗이 비우기

            //  중요 
            // 예외를 잡고 아무것도 안하면 안돼.
            // Spring Security의 ExceptionTranslationFilter가 이 예외를 처리할 수 있도록
            // 예외를 *다시 던지거나*, 아니면 여기서 *직접* EntryPoint를 호출해야 함.
            // 하지만 우리는 이미 SecurityConfig에 EntryPoint를 설정했으므로,
            // 예외가 필터 밖으로 던져지도록 놔두는 게(catch 블록을 아예 없애는 게) 베스트.
            // ... 하지만 OncePerRequestFilter는 예외를 던지면 톰캣까지 전파될 수 있으니
            // 여기서는 예외를 잡되, 리프레시 로직 대신 그냥 SecurityContext만 비우고
            // filterChain.doFilter()를 호출해서 "미인증 상태로" 다음으로 넘기는게 맞아.
            // 그러면 ExceptionTranslationFilter가 "인증 객체가 없네?" 하고 EntryPoint를 호출할...
            // 아니야, 더 복잡해진다.

            //  가장 깔끔한 방법:
            // Spring Security의 기본 필터인 `BearerTokenAuthenticationFilter`의 방식을 모방하자.
            // 걔는 `try-catch`로 `AuthenticationException`을 잡고,
            // `authenticationEntryPoint.commence(req, res, e)`를 *직접* 호출한 뒤 `return;` (필터 체인 종료)
            // ... 하지만 우리는 `AuthenticationManager`를 주입받았지, `EntryPoint`를 안 받았어.

            //  최종 결론: 가장 간단하고 표준적인 방법
            // `try-catch`를 쓰지 말고, 예외가 발생하면 필터 밖으로 그냥 던져지게 둔다.
            // `ExceptionTranslationFilter` (Spring Security 기본 필터)가 이 필터 *밖에서*
            // 예외를 잡아서 `SecurityConfig`에 등록된 `CustomAdminAuthenticationEntryPoint`를 호출해줄 거야.
            //
            // ... 근데 위에서 `try-catch`를 이미 썼네.
            // 그럼 `catch` 블록에서 예외를 다시 던지자.
            throw e; //  중요! 예외를 다시 던져서 Spring Security가 처리하도록 함.
        }

        // 6. 다음 필터 실행
        filterChain.doFilter(req, res);
    }

    protected String getAccessToken(HttpServletRequest req) {
        String clientType = req.getHeader("X-Client-Type");
        boolean isMobileApp = clientType != null && clientType.contains("mobile");

        if (isMobileApp) {
            return req.getHeader("X-Access-Token");
        } else {
            Cookie cookie = WebUtils.getCookie(req, "X-Access-Token");
            if (cookie != null) {
                return cookie.getValue();
            }
        }
        return null;
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String uri = request.getRequestURI();
        for (String excludedUrl : EXCLUDED_URLS) {
            if (pathMatcher.match(excludedUrl, uri)) {
                return true;
            }
        }
        return false;
    }
}