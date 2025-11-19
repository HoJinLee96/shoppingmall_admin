package net.chamman.shoppingmall_admin.domain.verification;

import java.util.Random;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.chamman.shoppingmall_admin.exception.domain.verification.VerificationCodeMismatchException;
import net.chamman.shoppingmall_admin.exception.domain.verification.VerificationIllegalException;
import net.chamman.shoppingmall_admin.exception.infra.email.EmailSendException;
import net.chamman.shoppingmall_admin.exception.infra.rate.TooManyRequestException;
import net.chamman.shoppingmall_admin.exception.infra.sms.SmsSendException;
import net.chamman.shoppingmall_admin.exception.security.token.TokenCreateException;
import net.chamman.shoppingmall_admin.exception.security.token.TokenIllegalException;
import net.chamman.shoppingmall_admin.exception.security.token.TokenParseException;
import net.chamman.shoppingmall_admin.infra.email.EmailSender;
import net.chamman.shoppingmall_admin.infra.rate.RateLimitService;
import net.chamman.shoppingmall_admin.infra.rate.RateLimiter;
import net.chamman.shoppingmall_admin.infra.sms.SmsSender;
import net.chamman.shoppingmall_admin.infra.sms.impl.NaverSmsSenderImpl;
import net.chamman.shoppingmall_admin.security.token.TokenService;
import net.chamman.shoppingmall_admin.security.token.TokenService.TokenPurpose;
import net.chamman.shoppingmall_admin.security.token.TokenService.TokenType;
import net.chamman.shoppingmall_admin.security.token.dto.VerificationCodeEmailRequestDto;
import net.chamman.shoppingmall_admin.security.token.dto.VerificationCodeEmailSuccessDto;
import net.chamman.shoppingmall_admin.security.token.dto.VerificationCodeSmsRequestDto;
import net.chamman.shoppingmall_admin.security.token.dto.VerificationCodeSmsSuccessDto;
import net.chamman.shoppingmall_admin.support.util.LogMaskingUtil;
import net.chamman.shoppingmall_admin.support.util.LogMaskingUtil.MaskLevel;

@Service
@Slf4j
@RequiredArgsConstructor
@PropertySource("classpath:application.properties")
public class VerificationService {

	private final SmsSender smsSender;
	private final EmailSender emailSender;
	private final TokenService tokenService;
	@Value("${shopping_mall.name}")
	String shoppingMallName;

	/**
	 * 인증 번호 sms 전송
	 * 
	 * @param phone
	 * @return
	 * 
	 * {@link RateLimitService#checkVerificationCodeRequest}
	 * @throws TooManyRequestException {@link RateLimiter#isAllowed}
	 *
	 * {@link SmsSender#sendSms}
	 * @throws SmsSendException {@link NaverSmsSenderImpl#sendSms}
	 * 
	 * {@link TokenService#issueToken}
	 * @throws TokenCreateException {@link TokenService#issueToken} 토큰 생성 실패
	 * 
	 * @throws 
	 */
	public String sendCodeSms(String phone) {
		
		String code = generateVerificationCode();
		String message = "[" + shoppingMallName + " 인증 요청]\n인증번호 [" + code + "]를 입력해주세요.";
		
		smsSender.sendSms(phone, message);
		
		VerificationCodeSmsRequestDto dto = new VerificationCodeSmsRequestDto(phone, code);
		
		return tokenService.issueToken(dto, TokenType.VERIFICATION_CODE_SMS_REQUEST);
	}
	
	/**
	 * 인증 번호 email 전송
	 * 
	 * @param email
	 * 
	 * @return
	 * 
	 * {@link RateLimitService#checkVerificationCodeRequest}
	 * @throws TooManyRequestException {@link RateLimiter#isAllowed}
	 *
	 * {@link EmailSender#sendEmail}
	 * @throws EmailSendException {@link EmailSender#sendEmail}
	 * 
	 * {@link TokenService#issueToken}
	 * @throws TokenCreateException {@link TokenService#issueToken} 토큰 생성 실패
	 * 
	 * @throws 
	 */
	public String sendCodeEmail(String email) {
		
		String code = generateVerificationCode();
		String title = "[" + shoppingMallName + " 인증 요청]";
		String message = "[" + shoppingMallName + " 인증 요청]\n인증번호 [" + code + "]를 입력해주세요.";
		
		emailSender.sendEmail(email, title, message);
		
		VerificationCodeEmailRequestDto dto = new VerificationCodeEmailRequestDto(email, code);
		
		return tokenService.issueToken(dto, TokenType.VERIFICATION_CODE_EMAIL_REQUEST);
	}

	/**
	 * 인증번호 sms 검증
	 * 
	 * @param tokenPurpose
	 * @param token
	 * @param phone
	 * @param reqCode
	 * 
	 * {@link RateLimitService#checkVerificationCodeRequest}
	 * @throws TooManyRequestException {@link RateLimiter#isAllowed}
	 * 
	 * {@link TokenService#getDto}
	 * @throws TokenIllegalException {@link TokenService#getDto} 토큰 값 null 또는 비어있음 
	 * @throws TokenParseException {@link TokenService#getDto} value 파싱 중 익셉션 발생
	 * 
	 * @throws VerificationIllegalException {@link VerificationService#compareSmsCode} 수신자 불일치
	 * @throws VerificationCodeMismatchException {@link VerificationService#compareSmsCode} 인증 번호 불일치
	 * 
	 * {@link TokenService#issueToken}
	 * @throws TokenCreateException {@link TokenService#issueToken} 토큰 생성 실패
	 * 
	 * @return 휴대폰 인증 완료 토큰
	 */
	@Transactional
	public String compareCodeSms(TokenPurpose tokenPurpose, String token, String phone, String reqCode) {
		log.debug("* 인증번호 검증. token: [{}], recipient: [{}], reqCode:[{}]",
				LogMaskingUtil.maskToken(token, MaskLevel.MEDIUM),
				LogMaskingUtil.maskPhone(phone, MaskLevel.MEDIUM),
				LogMaskingUtil.maskVerificationCode(phone, MaskLevel.MEDIUM));
		
		VerificationCodeSmsRequestDto dto = tokenService.restoreTokenDto(TokenType.VERIFICATION_CODE_SMS_REQUEST, token);

		// 수신자 검사
		if (!dto.getPhone().equals(phone)) {
			throw new VerificationIllegalException("수신자 불일치. VerificationCodeSmsRequestDto.recipient : " + dto.getPhone() + ", phone: " + phone);
		}

		// 인증번호 일치 검사
		if (!dto.getCode().equals(reqCode)) {
			throw new VerificationCodeMismatchException("인증 번호 불일치. reqCode: " + reqCode);
		}		
		VerificationCodeSmsSuccessDto successDto = new VerificationCodeSmsSuccessDto(tokenPurpose, phone);
		
		return tokenService.issueToken(successDto, VerificationCodeSmsSuccessDto.TOKENTYPE);
	}
	
	@Transactional
	public String compareCodeEmail(TokenPurpose tokenPurpose, String token, String email, String reqCode) {
		log.debug("* 인증번호 검증. token: [{}], recipient: [{}], reqCode:[{}], clientIp: [{}]",
				LogMaskingUtil.maskToken(token, MaskLevel.MEDIUM),
				LogMaskingUtil.maskPhone(email, MaskLevel.MEDIUM),
				LogMaskingUtil.maskVerificationCode(reqCode, MaskLevel.MEDIUM));
		
		VerificationCodeEmailRequestDto dto = tokenService.restoreTokenDto(TokenType.VERIFICATION_CODE_EMAIL_REQUEST, token);

		// 수신자 검사
		if (!dto.getEmail().equals(email)) {
			throw new VerificationIllegalException("수신자 불일치. VerificationCodeEmailRequestDto.email : " + dto.getEmail() + ", email: " + email);
		}

		// 인증번호 일치 검사
		if (!dto.getCode().equals(reqCode)) {
			throw new VerificationCodeMismatchException("인증 번호 불일치. reqCode: " + reqCode);
		}		
		
		VerificationCodeEmailSuccessDto successDto = new VerificationCodeEmailSuccessDto(tokenPurpose, email);
		
		return tokenService.issueToken(successDto, TokenType.VERIFICATION_CODE_EMAIL_SUCCESS);
	}

	/**
	 * 랜덤 인증번호 생성
	 * 
	 * @return 인증번호
	 */
	private String generateVerificationCode() {
		return String.format("%06d", new Random().nextInt(1000000)); // 6자리 인증번호 생성
	}
	
}
