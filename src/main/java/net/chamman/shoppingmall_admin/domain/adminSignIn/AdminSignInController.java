package net.chamman.shoppingmall_admin.domain.adminSignIn;

import static net.chamman.shoppingmall_admin.exception.HttpStatusCode.SUCCESS;
import static net.chamman.shoppingmall_admin.exception.HttpStatusCode.SUCCESS_NO_DATA;

import java.time.Duration;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.chamman.shoppingmall_admin.domain.adminSignIn.dto.AdminSignInJwtDto;
import net.chamman.shoppingmall_admin.domain.adminSignIn.dto.AdminSignInRequestDto;
import net.chamman.shoppingmall_admin.infra.rate.RateLimitKey;
import net.chamman.shoppingmall_admin.security.principal.AdminDetails;
import net.chamman.shoppingmall_admin.support.annotation.ClientSpecific;
import net.chamman.shoppingmall_admin.support.annotation.RateLimit;
import net.chamman.shoppingmall_admin.support.context.CustomRequestContextHolder;
import net.chamman.shoppingmall_admin.support.util.ApiResponseDto;
import net.chamman.shoppingmall_admin.support.util.ApiResponseFactory;
import net.chamman.shoppingmall_admin.support.util.CookieUtil;
import net.chamman.shoppingmall_admin.support.util.LogMaskingUtil;
import net.chamman.shoppingmall_admin.support.util.LogMaskingUtil.MaskLevel;

@Slf4j
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class AdminSignInController {

	private final AdminSignInService adminSignService;
	private final ApiResponseFactory apiResponseFactory;

	@RateLimit(value = RateLimitKey.SIGN_IN_REQUEST)
	@PostMapping("/public/admin/signIn")
	public ResponseEntity<ApiResponseDto<AdminSignInJwtDto>> amdinSignIn(
			@Valid @RequestBody AdminSignInRequestDto signInRequestDto, HttpServletRequest req,
			HttpServletResponse res) {
		log.debug("* 관리자 로그인 요청. userName: [{}], Client IP: [{}], Admin-Agent: [{}]",
				LogMaskingUtil.maskEmail(signInRequestDto.userName(), MaskLevel.MEDIUM));
		
		String clientIp = CustomRequestContextHolder.getClientIp();
		
		AdminSignInJwtDto jwtDto = adminSignService.signInAndCreateJwt(signInRequestDto, clientIp);

		if (CustomRequestContextHolder.isMobileApp()) {
			return ResponseEntity.ok(apiResponseFactory.success(SUCCESS, jwtDto));
		} else {
			CookieUtil.addCookie(res, "X-Access-Token", jwtDto.accessToken(), Duration.ofMinutes(120));
			CookieUtil.addCookie(res, "X-Refresh-Token", jwtDto.refreshToken(), Duration.ofDays(14));

			return ResponseEntity.ok(apiResponseFactory.success(SUCCESS_NO_DATA, null));
		}
	}
	
	@Operation(summary = "관리자 로그아웃")
	@SecurityRequirement(name = "X-Access-Token")
	@SecurityRequirement(name = "X-Refresh-Token")
	@PostMapping("/private/admin/signOut")
	public ResponseEntity<ApiResponseDto<Void>> adminSignOut(
			@AuthenticationPrincipal AdminDetails adminDetails,
			@ClientSpecific(value = "X-Access-Token") String accessToken,
			@ClientSpecific(value = "X-Refresh-Token") String refreshToken,
			HttpServletRequest req, HttpServletResponse res) {
		log.debug("* 관리자 로그아웃 요청. Admin ID: [{}], AccessToken: [{}], Client IP: [{}], Admin-Agent: [{}]",
				adminDetails != null ? adminDetails.getId() : "anonymous",
						LogMaskingUtil.maskToken(accessToken, MaskLevel.MEDIUM));

		String clientIp = CustomRequestContextHolder.getClientIp();

		try {
			adminSignService.signOut(adminDetails.getId(), accessToken, refreshToken, clientIp);
		} finally {
			if (!CustomRequestContextHolder.isMobileApp()) {
				CookieUtil.deleteCookie(req, res, "X-Access-Token");
				CookieUtil.deleteCookie(req, res, "X-Refresh-Token");
			}
		}

		return ResponseEntity.ok(apiResponseFactory.success(SUCCESS_NO_DATA));
	}

	@Operation(summary = "JWT 재발급 (Refresh)", description = "RefreshToken을 사용하여 새로운 AccessToken과 RefreshToken을 발급받습니다.")
    @SecurityRequirement(name = "X-Refresh-Token")
    @PostMapping("/public/admin/refresh")
    public ResponseEntity<ApiResponseDto<AdminSignInJwtDto>> adminRefresh(
			@ClientSpecific(value = "X-Access-Token") String accessToken,
            @ClientSpecific(value = "X-Refresh-Token") String refreshToken,
            HttpServletRequest req, HttpServletResponse res) {
		log.debug("* AccessToken 재발급 요청.");

        String clientIp = CustomRequestContextHolder.getClientIp();

        AdminSignInJwtDto jwtDto = adminSignService.refresh(accessToken, refreshToken, clientIp);

        if (CustomRequestContextHolder.isMobileApp()) {
            return ResponseEntity.ok(apiResponseFactory.success(SUCCESS, jwtDto));
        } else {
            // 웹은 쿠키로
            CookieUtil.addCookie(res, "X-Access-Token", jwtDto.accessToken(), Duration.ofMinutes(120));
            CookieUtil.addCookie(res, "X-Refresh-Token", jwtDto.refreshToken(), Duration.ofDays(14));
            return ResponseEntity.ok(apiResponseFactory.success(SUCCESS_NO_DATA, null));
        }
    }
}
