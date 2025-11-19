package net.chamman.shoppingmall_admin.domain.verification;

import static net.chamman.shoppingmall_admin.exception.HttpStatusCode.SUCCESS;

import java.time.Duration;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import net.chamman.shoppingmall_admin.domain.verification.dto.VerificationCodeEmailCompareDto;
import net.chamman.shoppingmall_admin.domain.verification.dto.VerificationCodeSmsCompareDto;
import net.chamman.shoppingmall_admin.infra.rate.RateLimitService;
import net.chamman.shoppingmall_admin.security.token.TokenService.TokenPurpose;
import net.chamman.shoppingmall_admin.support.annotation.ClientSpecific;
import net.chamman.shoppingmall_admin.support.annotation.ValidateEmail;
import net.chamman.shoppingmall_admin.support.annotation.ValidatePhone;
import net.chamman.shoppingmall_admin.support.context.CustomRequestContextHolder;
import net.chamman.shoppingmall_admin.support.util.ApiResponseDto;
import net.chamman.shoppingmall_admin.support.util.ApiResponseFactory;
import net.chamman.shoppingmall_admin.support.util.CookieUtil;

@Tag(name = "VerificationController", description = "인증 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class VerificationController {
	
	private final VerificationService verificationService;
	private final RateLimitService rateLimitService;
	private final ApiResponseFactory apiResponseFactory;
	
	@Operation(summary = "휴대폰 문자 인증번호 발송", description = "휴대폰 문자 인증번호 발송")
	@PostMapping("/public/verify/send/sms")
	public ResponseEntity<ApiResponseDto<Map<String,String>>> sendSmsCode(
			@ValidatePhone @RequestParam String phone,
			HttpServletRequest req, HttpServletResponse res) {
		
		String clientIp = CustomRequestContextHolder.getClientIp();
		boolean isMobileApp = CustomRequestContextHolder.isMobileApp();
		
		rateLimitService.checkVerificationCodeRequest(clientIp);
		
		String verificationCodeSmsRequestToken = verificationService.sendCodeSms(phone);
		
		if(isMobileApp) {
			return ResponseEntity.ok(apiResponseFactory.success(SUCCESS, Map.of("X-Verification-Code-Phone-Request-Token",verificationCodeSmsRequestToken)));
		}else {
			CookieUtil.addCookie(res, "X-Verification-Code-Phone-Request-Token", verificationCodeSmsRequestToken, Duration.ofMinutes(3));
			
			return ResponseEntity.ok(apiResponseFactory.success(SUCCESS, null));
		}
	}
	
	@Operation(summary = "이메일 인증번호 발송", description = "이메일 인증번호 발송")
	@PostMapping("/public/verify/send/email")
	public ResponseEntity<ApiResponseDto<Map<String,String>>> sendEmailCode(
			@ValidateEmail @RequestParam String email,
			HttpServletRequest req, HttpServletResponse res) {
		
		String clientIp = CustomRequestContextHolder.getClientIp();
		boolean isMobileApp = CustomRequestContextHolder.isMobileApp();
		
		rateLimitService.checkVerificationCodeRequest(clientIp);
		
		String verificationCodeEmailRequestToken = verificationService.sendCodeEmail(email);
		
		if(isMobileApp) {
			return ResponseEntity.ok(apiResponseFactory.success(SUCCESS, Map.of("X-Verification-Code-Email-Request-Token", verificationCodeEmailRequestToken)));
		}else {
			CookieUtil.addCookie(res, "X-Verification-Code-Email-Request-Token", verificationCodeEmailRequestToken, Duration.ofMinutes(3));
			
			return ResponseEntity.ok(apiResponseFactory.success(SUCCESS, null));

		}
	}
	
	@Operation(summary = "관리자 가입 휴대폰 문자 인증번호 검사", description = "휴대폰 문자 인증번호 검사")
	@PostMapping("/public/verify/compare/sms/signUp")
	public ResponseEntity<ApiResponseDto<Map<String,String>>> compareSmsVerificationForSignUp(
			@ClientSpecific("X-Verification-Code-Phone-Request-Token") String verificationCodePhoneRequestToken,
			@Valid @RequestBody VerificationCodeSmsCompareDto dto,
			HttpServletRequest req, HttpServletResponse res) {
		
		String clientIp = CustomRequestContextHolder.getClientIp();
		boolean isMobileApp = CustomRequestContextHolder.isMobileApp();
		
		rateLimitService.checkVerificationCodeRequest(clientIp);
		
		String token = verificationService.compareCodeSms(TokenPurpose.ADMIN_SIGNUP, verificationCodePhoneRequestToken, dto.phone(), dto.code());
		
		if(isMobileApp) {
			return ResponseEntity.ok(apiResponseFactory.success(SUCCESS, Map.of("X-Verification-Code-Phone-Success-Token",token)));
		}else {
			CookieUtil.deleteCookie(req, res, "X-Verification-Code-Phone-Request-Token");
			CookieUtil.addCookie(res, "X-Verification-Code-Phone-Success-Token", token, Duration.ofMinutes(5));
			
			return ResponseEntity.ok(apiResponseFactory.success(SUCCESS, null));
		}
	}
	
	@Operation(summary = "관리자 가입 이메일 인증 인증번호 검사", description = "이메일 인증 인증번호 검사")
	@PostMapping("/public/verify/compare/email/signUp")
	public ResponseEntity<ApiResponseDto<Map<String,String>>> compareEmailVerificationForSignUp(
			@ClientSpecific("X-Verification-Code-Email-Request-Token") String verificationCodeEmailRequestToken,
			@Valid @RequestBody VerificationCodeEmailCompareDto dto,
			HttpServletRequest req, HttpServletResponse res) {
		
		String clientIp = CustomRequestContextHolder.getClientIp();
		boolean isMobileApp = CustomRequestContextHolder.isMobileApp();
		
		rateLimitService.checkVerificationCodeRequest(clientIp);
		
		String token = verificationService.compareCodeEmail(TokenPurpose.ADMIN_SIGNUP, verificationCodeEmailRequestToken, dto.email(), dto.code());

		if(isMobileApp) {
			return ResponseEntity.ok(apiResponseFactory.success(SUCCESS, Map.of("X-Verification-Code-Email-Success-Token",token)));
		}else {
			CookieUtil.deleteCookie(req, res, "X-Verification-Code-Email-Request-Token");
			CookieUtil.addCookie(res, "X-Verification-Code-Email-Success-Token", token, Duration.ofMinutes(5));
			
			return ResponseEntity.ok(apiResponseFactory.success(SUCCESS, null));
		}
	}
	
	
	@Operation(summary = "관리자 탈퇴 휴대폰 문자 인증번호 검사", description = "휴대폰 문자 인증번호 검사")
	@PostMapping("/public/verify/compare/sms/withdrawal")
	public ResponseEntity<ApiResponseDto<Map<String,String>>> compareSmsVerificationForWithdrawal(
			@ClientSpecific("X-Verification-Code-Phone-Request-Token") String verificationCodePhoneRequestToken,
			@Valid @RequestBody VerificationCodeSmsCompareDto dto,
			HttpServletRequest req, HttpServletResponse res) {
		
		String clientIp = CustomRequestContextHolder.getClientIp();
		boolean isMobileApp = CustomRequestContextHolder.isMobileApp();
		
		rateLimitService.checkVerificationCodeRequest(clientIp);
		
		String token = verificationService.compareCodeSms(TokenPurpose.ADMIN_WITHDRAWAL, verificationCodePhoneRequestToken, dto.phone(), dto.code());
		
		if(isMobileApp) {
			return ResponseEntity.ok(apiResponseFactory.success(SUCCESS, Map.of("X-Verification-Code-Phone-Success-Token",token)));
		}else {
			CookieUtil.deleteCookie(req, res, "X-Verification-Code-Phone-Request-Token");
			CookieUtil.addCookie(res, "X-Verification-Code-Phone-Success-Token", token, Duration.ofMinutes(5));
			
			return ResponseEntity.ok(apiResponseFactory.success(SUCCESS, null));
		}
	}
	
}
