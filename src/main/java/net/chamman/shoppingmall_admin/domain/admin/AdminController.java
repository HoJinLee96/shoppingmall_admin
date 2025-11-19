package net.chamman.shoppingmall_admin.domain.admin;

import static net.chamman.shoppingmall_admin.exception.HttpStatusCode.SUCCESS_NO_DATA;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.chamman.shoppingmall_admin.support.annotation.RateLimit;
import net.chamman.shoppingmall_admin.support.annotation.ValidateEmail;
import net.chamman.shoppingmall_admin.support.annotation.ValidatePhone;
import net.chamman.shoppingmall_admin.support.util.ApiResponseDto;
import net.chamman.shoppingmall_admin.support.util.ApiResponseFactory;

@Slf4j
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class AdminController {

	private final AdminQueryService adminQueryService;
	private final ApiResponseFactory apiResponseFactory;
	
	@RateLimit
	@PostMapping("/public/admin/exist/email")
	public ResponseEntity<ApiResponseDto<Void>> isEmailExistsForAdminSignUp(@ValidateEmail @RequestParam String email) {

		adminQueryService.isAdminEmailExist(email);
		
		return ResponseEntity.ok(apiResponseFactory.success(SUCCESS_NO_DATA));
	}

	@Operation(summary = "휴대폰 중복 검사")// description = "2040: 중복 없음, 4532: 휴대폰 번호 중복."
	@RateLimit
	@PostMapping("/public/admin/exist/phone")
	public ResponseEntity<ApiResponseDto<Void>> isPhoneExistForSignUp(@ValidatePhone @RequestParam String phone) {
		
		adminQueryService.isAdminPhoneExist(phone);
		
		return ResponseEntity.ok(apiResponseFactory.success(SUCCESS_NO_DATA));
	}
	
}
