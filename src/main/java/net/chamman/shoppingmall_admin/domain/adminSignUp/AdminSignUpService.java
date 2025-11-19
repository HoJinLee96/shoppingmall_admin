package net.chamman.shoppingmall_admin.domain.adminSignUp;

import java.util.Objects;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.chamman.shoppingmall_admin.domain.admin.Admin;
import net.chamman.shoppingmall_admin.domain.admin.Admin.AdminStatus;
import net.chamman.shoppingmall_admin.domain.admin.AdminQueryService;
import net.chamman.shoppingmall_admin.domain.admin.AdminRepository;
import net.chamman.shoppingmall_admin.domain.adminSignLog.AdminSignLog.SignResult;
import net.chamman.shoppingmall_admin.domain.adminSignLog.AdminSignLogService;
import net.chamman.shoppingmall_admin.domain.adminSignUp.dto.AdminSignUpFirstRequestDto;
import net.chamman.shoppingmall_admin.domain.adminSignUp.dto.AdminSignUpRequestDto;
import net.chamman.shoppingmall_admin.exception.common.ValidationException;
import net.chamman.shoppingmall_admin.exception.security.token.TokenIllegalException;
import net.chamman.shoppingmall_admin.security.token.TokenService;
import net.chamman.shoppingmall_admin.security.token.TokenService.TokenPurpose;
import net.chamman.shoppingmall_admin.security.token.TokenService.TokenType;
import net.chamman.shoppingmall_admin.security.token.dto.AdminSignUpProgressTokenDto;
import net.chamman.shoppingmall_admin.security.token.dto.VerificationCodeEmailSuccessDto;
import net.chamman.shoppingmall_admin.security.token.dto.VerificationCodeSmsSuccessDto;

@Slf4j
@RequiredArgsConstructor
@Service
public class AdminSignUpService {
//
	private final AdminRepository adminRepository;
	private final AdminQueryService adminQueryService;
	private final AdminSignLogService adminSignLogService;
	private final TokenService tokenService;
	private final PasswordEncoder passwordEncoder;

	/**
	 * 관리자 가입 이메일, 인증, 비밀번호 입력 단계
	 * 
	 * @param email
	 * @param password
	 * @param valificationEmailToken
	 * 
	 * @return
	 */
	@Transactional
	public String signUpFirst(AdminSignUpFirstRequestDto dto) {
		log.debug("* createAdminSignUpToken 시작. ");

		// 두 비밀번호 검증
		if (!Objects.equals(dto.password(), dto.confirmPassword())) {
			throw new ValidationException(
					"두 비밀번호가 일치하지 않음. password: " + dto.password() + ", confirmPassword: " + dto.confirmPassword());
		}
		
		adminQueryService.isAdminUserNameExist(dto.userName());
		
		AdminSignUpProgressTokenDto signUpTokendto = AdminSignUpProgressTokenDto.firstStep(dto.userName(), dto.password());
        
		return tokenService.issueToken(signUpTokendto, AdminSignUpProgressTokenDto.TOKENTYPE);
	}
	
	public void signUpSecond(String email, String verificationToken, String signUpToken) {
		
		AdminSignUpProgressTokenDto signUpTokendto = tokenService.restoreTokenDto(AdminSignUpProgressTokenDto.TOKENTYPE, signUpToken);
		
		// 이메일 인증 토큰 검증
		VerificationCodeEmailSuccessDto verificationCodeEmailSuccessDto = tokenService.restoreTokenDto(VerificationCodeEmailSuccessDto.TOKENTYPE, verificationToken);
		TokenPurpose purpose = verificationCodeEmailSuccessDto.getTokenPurpose();
		if(!purpose.equals(TokenPurpose.ADMIN_SIGNUP)) {
			throw new TokenIllegalException("관리자 가입 중 이메일 인증 Payload의 목적 불일치.");
		}
		if (!Objects.equals(verificationCodeEmailSuccessDto.getEmail(), email)) {
			throw new TokenIllegalException("관리자 가입 중 이메일 인증 Payload의 이메일 불일치.");
		}

		// 이메일 중복 검사
		adminQueryService.isAdminEmailExist(email);
		
		AdminSignUpProgressTokenDto nextSignUpTokenDto = signUpTokendto.withEmail(email);
		tokenService.updateTokenPayload(signUpToken, nextSignUpTokenDto, TokenType.ADMIN_SIGNUP_PROGRESS);
		
		tokenService.deleteToken(TokenType.VERIFICATION_CODE_EMAIL_SUCCESS, verificationToken);
	}
	
	public void signUpThird(String phone, String verificationToken, String signUpToken) {
		
		AdminSignUpProgressTokenDto signUpTokendto = tokenService.restoreTokenDto(AdminSignUpProgressTokenDto.TOKENTYPE, signUpToken);
		
		// 휴대폰 인증 토큰 검증
		VerificationCodeSmsSuccessDto verificationCodeSmsSuccessDto = tokenService
				.restoreTokenDto(VerificationCodeSmsSuccessDto.TOKENTYPE, verificationToken);
		TokenPurpose purpose = verificationCodeSmsSuccessDto.getTokenPurpose();
		if(!purpose.equals(TokenPurpose.ADMIN_SIGNUP)) {
			throw new TokenIllegalException("관리자 가입 중 휴대폰 인증 Payload의 목적 불일치.");
		}
		if (!Objects.equals(verificationCodeSmsSuccessDto.getPhone(), phone)) {
			throw new TokenIllegalException("관리자 가입 중 휴대폰 인증 Payload의 휴대폰 불일치.");
		}

		// 휴대폰 중복 검사
		adminQueryService.isAdminPhoneExist(phone);
		
		AdminSignUpProgressTokenDto nextSignUpTokenDto = signUpTokendto.withPhone(phone);
		tokenService.updateTokenPayload(signUpToken, nextSignUpTokenDto, TokenType.ADMIN_SIGNUP_PROGRESS);

		tokenService.deleteToken(VerificationCodeSmsSuccessDto.TOKENTYPE, verificationToken);
	}


	/**
	 * 관리자 가입 프로필 입력 단계
	 * 
	 * @param signUpRequestDto       관리자 프로필 정보
	 * @param accessSignUpToken      관리자 가입 1차 토큰
	 * @param verificationPhoneToken 휴대폰 인증 토큰
	 * @param clientIp
	 * 
	 * @return
	 */
	@Transactional
	public String signUp(AdminSignUpRequestDto requestDto, String signUpToken, String clientIp) {
		log.debug("* adminSignUp 시작");
		
		AdminSignUpProgressTokenDto signUpTokendto = tokenService.restoreTokenDto(AdminSignUpProgressTokenDto.TOKENTYPE, signUpToken);
        
        // 최종 단계인지 상태 검증
        if (signUpTokendto.getStatus() != AdminSignUpProgressTokenDto.SignUpStatus.PHONE_VERIFIED) {
            throw new IllegalStateException("회원가입 절차가 올바르게 완료되지 않았습니다. 현재 상태: " + signUpTokendto.getStatus());
        }

		String encodedPassoword = passwordEncoder.encode(signUpTokendto.getPassword());
		
		Admin admin = Admin.builder()
				.userName(signUpTokendto.getUsername())
				.name(requestDto.name())
				.phone(signUpTokendto.getPhone())
				.adminStatus(AdminStatus.ACTIVE)
				.email(signUpTokendto.getEmail())
				.password(encodedPassoword)
				.build();
		adminRepository.save(admin);

		adminSignLogService.signAdmin(admin, SignResult.SIGNUP, clientIp);

		tokenService.deleteToken(AdminSignUpProgressTokenDto.TOKENTYPE, signUpToken);

		return requestDto.name();
	}
	
}
