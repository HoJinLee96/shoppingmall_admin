package net.chamman.shoppingmall_admin.security.jwt;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.chamman.shoppingmall_admin.exception.security.jwt.JwtExpiredException;
import net.chamman.shoppingmall_admin.exception.security.jwt.JwtIllegalException;
import net.chamman.shoppingmall_admin.exception.security.jwt.JwtParseException;
import net.chamman.shoppingmall_admin.security.jwt.dto.AdminAccessTokenClaimsDto;
import net.chamman.shoppingmall_admin.security.principal.AdminDetails;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationProvider implements AuthenticationProvider {

    private final JwtService jwtService;

    /**
     * 실제 인증 로직
     * 
     */
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        JwtAuthenticationToken jwtAuthToken = (JwtAuthenticationToken) authentication;
        String accessToken = (String) jwtAuthToken.getPrincipal();

        try {
            // 1. 블랙리스트 검사 (로그아웃된 토큰인지)
        	/** @throws JwtIllegalException */
            jwtService.isExistAccessTokenBlackList(accessToken); 

            // 2. 토큰 파싱 및 검증 (서명, 구조, 클레임)
            /** @throws JwtExpiredException, JwtParseException, JwtIllegalException */
            AdminAccessTokenClaimsDto claimsDto = jwtService.parseAccessToken(accessToken); 

            // 3. 인증된 사용자 정보(AdminDetails) 생성
            AdminDetails adminDetails = new AdminDetails(claimsDto);

            // 4. 인증 완료된 토큰 생성
            return new JwtAuthenticationToken(adminDetails, adminDetails.getAuthorities());

        } catch (JwtExpiredException e) {
            // 중요: 네가 만든 CustomException을 Spring Security의 AuthenticationException으로 변환
            log.warn("* AccessToken 만료.");
            throw new CredentialsExpiredException("AccessToken Expired", e);
        } catch (JwtIllegalException | JwtParseException e) {
            // 중요: 토큰이 이상하거나 파싱 실패 시
            log.warn("* 유효하지 않은 AccessToken.");
            throw new BadCredentialsException("Invalid AccessToken", e);
        } catch (Exception e) {
            // 그 외 모든 예외
            log.error("* JWT 인증 중 알 수 없는 에러", e);
            throw new BadCredentialsException("JWT Authentication Error", e);
        }
    }

    /**
     * 이 Provider가 어떤 타입의 Authentication을 처리할 수 있는지 정의
     */
    @Override
    public boolean supports(Class<?> authentication) {
        // 우리는 JwtAuthenticationToken만 처리할 거야
        return JwtAuthenticationToken.class.isAssignableFrom(authentication);
    }
}