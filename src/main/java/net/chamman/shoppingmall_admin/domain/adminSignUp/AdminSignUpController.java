package net.chamman.shoppingmall_admin.domain.adminSignUp;

import static net.chamman.shoppingmall_admin.exception.HttpStatusCode.SUCCESS;
import static net.chamman.shoppingmall_admin.exception.HttpStatusCode.SUCCESS_NO_DATA;

import java.time.Duration;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.chamman.shoppingmall_admin.domain.adminSignUp.dto.AdminSignUpFirstRequestDto;
import net.chamman.shoppingmall_admin.domain.adminSignUp.dto.AdminSignUpRequestDto;
import net.chamman.shoppingmall_admin.support.annotation.ClientSpecific;
import net.chamman.shoppingmall_admin.support.annotation.RateLimit;
import net.chamman.shoppingmall_admin.support.annotation.ValidateEmail;
import net.chamman.shoppingmall_admin.support.annotation.ValidatePhone;
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
@RequestMapping("/api/public/admin/signUp")
public class AdminSignUpController {

	private final AdminSignUpService adminSignUpService;
	private final ApiResponseFactory apiResponseFactory;

	@Operation(summary = "관리자 회원가입 1차", description = "관리자 가입 아이디, 비밀번호 입력")
	@PostMapping("/first")
	public ResponseEntity<ApiResponseDto<Map<String, String>>> signUpFirst(
			@Valid @RequestBody AdminSignUpFirstRequestDto dto,
			HttpServletRequest req, HttpServletResponse res) {
		log.debug("* 관리자 회원가입 1차 요청");
		
		boolean isMobileApp = CustomRequestContextHolder.isMobileApp();
		
		String adminSignUpToken = adminSignUpService.signUpFirst(dto);
		
		if (isMobileApp) {
			return ResponseEntity
					.ok(apiResponseFactory.success(SUCCESS, Map.of("X-Admin-SignUp-Token", adminSignUpToken)));
		} else {
			CookieUtil.addCookie(res, "X-Admin-SignUp-Token", adminSignUpToken, Duration.ofMinutes(10));
			
			return ResponseEntity.ok(apiResponseFactory.success(SUCCESS, null));
		}
	}
	
	@Operation(summary = "관리자 회원가입 2차", description = "이메일 인증")
	@SecurityRequirement(name = "X-Verification-Code-Email-Success-Token")
	@SecurityRequirement(name = "X-Admin-SignUp-Token")
	@PostMapping("/second")
	public ResponseEntity<ApiResponseDto<Void>> signUpSecond(
			@ClientSpecific("X-Verification-Code-Email-Success-Token") String verificationCodeEmailSuccessToken,
			@ClientSpecific("X-Admin-SignUp-Token") String adminSignUpToken,
			@ValidateEmail @RequestParam String email,
			HttpServletRequest req, HttpServletResponse res) {
		log.debug("* 관리자 회원가입 2차 요청. verificationCodeEmailSuccessToken: [{}], adminSignUpTokens: [{}]",
				LogMaskingUtil.maskToken(verificationCodeEmailSuccessToken, MaskLevel.MEDIUM),
				LogMaskingUtil.maskToken(adminSignUpToken, MaskLevel.MEDIUM));
		
		adminSignUpService.signUpSecond(email, verificationCodeEmailSuccessToken, adminSignUpToken);

		if (!CustomRequestContextHolder.isMobileApp()) {
			CookieUtil.deleteCookie(req, res, "X-Verification-Code-Email-Success-Token");
			CookieUtil.addCookie(res, "X-Admin-SignUp-Token", adminSignUpToken, Duration.ofMinutes(10));
		}
		
		return ResponseEntity.ok(apiResponseFactory.success(SUCCESS_NO_DATA));
	}
	
	@Operation(summary = "관리자 회원가입 3차", description = "휴대폰 인증")
	@SecurityRequirement(name = "X-Verification-Code-Phone-Success-Token")
	@SecurityRequirement(name = "X-Admin-SignUp-Token")
	@PostMapping("/third")
	public ResponseEntity<ApiResponseDto<Void>> signUpThird(
			@ClientSpecific("X-Verification-Code-Phone-Success-Token") String verificationCodeSmsSuccessToken,
			@ClientSpecific("X-Admin-SignUp-Token") String adminSignUpToken,
			@ValidatePhone @RequestParam String phone,
			HttpServletRequest req, HttpServletResponse res) {
		log.debug("* 관리자 회원가입 2차 요청. verificationCodeSmsSuccessToken: [{}], adminSignUpTokens: [{}], Client IP: [{}], Admin-Agent: [{}]",
				LogMaskingUtil.maskToken(verificationCodeSmsSuccessToken, MaskLevel.MEDIUM),
				LogMaskingUtil.maskToken(adminSignUpToken, MaskLevel.MEDIUM));
		
		adminSignUpService.signUpThird(phone, verificationCodeSmsSuccessToken, adminSignUpToken);
		
		if (!CustomRequestContextHolder.isMobileApp()) {
			CookieUtil.deleteCookie(req, res, "X-Verification-Code-Phone-Success-Token");
			CookieUtil.addCookie(res, "X-Admin-SignUp-Token", adminSignUpToken, Duration.ofMinutes(10));
		}
		
		return ResponseEntity.ok(apiResponseFactory.success(SUCCESS_NO_DATA));
	}
	
	@Operation(summary = "관리자 회원가입 4차", description = "프로필 입력")
	@SecurityRequirement(name = "X-Admin-SignUp-Token")
	@RateLimit
	@PostMapping("/third")
	public ResponseEntity<ApiResponseDto<Map<String, String>>> signUp(
			@ClientSpecific("X-Admin-SignUp-Token") String adminSignUpToken,
			@Valid @RequestBody AdminSignUpRequestDto dto,
			HttpServletRequest req, HttpServletResponse res) {
		
		String clientIp = CustomRequestContextHolder.getClientIp();
		boolean isMobileApp = CustomRequestContextHolder.isMobileApp();
		log.debug(
				"* 관리자 회원가입 2차 요청. adminSignUpTokens: [{}], Client IP: [{}], Admin-Agent: [{}]",
				LogMaskingUtil.maskToken(adminSignUpToken, MaskLevel.MEDIUM),
				clientIp, isMobileApp ? "mobile" : "web");
		
		String name = adminSignUpService.signUp(dto, adminSignUpToken, clientIp);
		
		if (!isMobileApp) {
			CookieUtil.deleteCookie(req, res, "X-Admin-SignUp-Token");
		}
		return ResponseEntity.ok(apiResponseFactory.success(SUCCESS, Map.of("name", name)));
	}
	
}
