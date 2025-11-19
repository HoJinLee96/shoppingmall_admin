package net.chamman.shoppingmall_admin.domain.adminProfile;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.chamman.shoppingmall_admin.exception.HttpStatusCode;
import net.chamman.shoppingmall_admin.security.principal.AdminDetails;
import net.chamman.shoppingmall_admin.support.annotation.ClientSpecific;
import net.chamman.shoppingmall_admin.support.context.CustomRequestContextHolder;
import net.chamman.shoppingmall_admin.support.util.ApiResponseDto;
import net.chamman.shoppingmall_admin.support.util.ApiResponseFactory;
import net.chamman.shoppingmall_admin.support.util.CookieUtil;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class AdminProfileController {

	private final AdminProfileService adminProfileService;
	private final ApiResponseFactory apiResponseFactory;
	
	@Operation(summary = "관리자 탈퇴")
	@SecurityRequirement(name = "X-Verification-Code-Phone-Success-Token")
	@SecurityRequirement(name = "X-Access-Token")
	@SecurityRequirement(name = "X-Refresh-Token")
	@PostMapping("/private/admin/withdrawal")
	public ResponseEntity<ApiResponseDto<Void>> adminWithdrawal(
			@AuthenticationPrincipal AdminDetails adminDetail,
			@ClientSpecific(value = "X-Verification-Code-Sms-Success-Token") String smsToken,
			@ClientSpecific(value = "X-Access-Token") String accessToken,
			@ClientSpecific(value = "X-Refresh-Token") String refreshToken,
			HttpServletRequest req, HttpServletResponse res) {
		log.debug("* 관리자 탈퇴 요청");
		
		String clientIp = CustomRequestContextHolder.getClientIp();
		
		adminProfileService.softDeleteAdmin(adminDetail.getId(), smsToken, accessToken, refreshToken, clientIp);
		
		
		if (CustomRequestContextHolder.isMobileApp()) {
			return ResponseEntity
					.ok(apiResponseFactory.success(HttpStatusCode.SUCCESS_NO_DATA));
		} else {
			CookieUtil.deleteCookie(req, res, "X-Verification-Code-Phone-Success-Token");
			CookieUtil.deleteCookie(req, res, "X-Access-Token");
			CookieUtil.deleteCookie(req, res, "X-Refresh-Token");
			
			return ResponseEntity.ok(apiResponseFactory.success(HttpStatusCode.SUCCESS_NO_DATA));
		}
	}

}
