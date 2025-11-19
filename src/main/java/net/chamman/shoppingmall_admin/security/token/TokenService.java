package net.chamman.shoppingmall_admin.security.token;

import java.time.Duration;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.chamman.shoppingmall_admin.exception.security.token.TokenCreateException;
import net.chamman.shoppingmall_admin.exception.security.token.TokenIllegalException;
import net.chamman.shoppingmall_admin.exception.security.token.TokenParseException;
import net.chamman.shoppingmall_admin.security.crypto.Cryptable;
import net.chamman.shoppingmall_admin.security.token.dto.AdminSignUpProgressTokenDto;
import net.chamman.shoppingmall_admin.security.token.dto.VerificationCodeEmailRequestDto;
import net.chamman.shoppingmall_admin.security.token.dto.VerificationCodeEmailSuccessDto;
import net.chamman.shoppingmall_admin.security.token.dto.VerificationCodeSmsRequestDto;
import net.chamman.shoppingmall_admin.security.token.dto.VerificationCodeSmsSuccessDto;
import net.chamman.shoppingmall_admin.support.util.LogMaskingUtil;
import net.chamman.shoppingmall_admin.support.util.LogMaskingUtil.MaskLevel;

@Service
@Slf4j
@RequiredArgsConstructor
public class TokenService {
	
	private final TokenProvider tokenProvider;
	private final RedisTemplate<String, String> redisTemplate;
	
	@AllArgsConstructor
	@Getter
	public enum TokenPurpose {
		ADMIN_UPDATE_PASSWORD("비밀번호 변경"),
		ADMIN_WITHDRAWAL("회원 탈퇴"),
		ADMIN_SIGNUP("회원 가입");
		
		private final String prefix;
	}
	
	@AllArgsConstructor
	@Getter
	public enum TokenType {
		VERIFICATION_CODE_SMS_REQUEST("verification:code:sms:request", Duration.ofMinutes(3), VerificationCodeSmsRequestDto.class),
		VERIFICATION_CODE_SMS_SUCCESS("verification:code:sms:success", Duration.ofMinutes(5), VerificationCodeSmsSuccessDto.class),
		VERIFICATION_CODE_EMAIL_REQUEST("verification:code:email:request", Duration.ofMinutes(3), VerificationCodeEmailRequestDto.class),
		VERIFICATION_CODE_EMAIL_SUCCESS("verification:code:email:success", Duration.ofMinutes(5), VerificationCodeEmailSuccessDto.class),
		ADMIN_SIGNUP_PROGRESS("admin:signup_progress", Duration.ofMinutes(10), AdminSignUpProgressTokenDto.class);

		private final String prefix;
		private final Duration ttl;
		private final Class<? extends Cryptable<?>> dtoType;

	}
	
	/**
	 * DTO를 페이로드로 사용하여 토큰 생성 및 Redis.set
	 * 
	 * @param <T>       Encryptable를 구현한 DTO 타입
	 * @param dto       토큰에 담을 데이터 DTO 객체
	 * @param tokenType 토큰 타입
	 * 
	 * @return 토큰
	 * 
	 * @throws TokenCreateException {@link TokenService#issueToken} 토큰 생성 실패
	 * 
	 */
	public <T extends Cryptable<T>> String issueToken(T dto, TokenType type) {
		log.debug("*{} 타입 토큰 발행.", type);

		if (dto.getClass() != type.getDtoType()) {
			throw new TokenCreateException("TokenType과 Dto 타입이 일치하지 않습니다. " + "입력 받은 타입: "
					+ type.getDtoType().getSimpleName() + ", 입력 받은 Dto 타입: " + dto.getClass().getSimpleName());
		}

		String token = UUID.randomUUID().toString();
		
		try {
		// 암호화 및 직렬화
		String json = tokenProvider.createTokenValue(dto);

		// Redis에 저장
		redisTemplate.opsForValue().set(type.getPrefix() + token, json, type.getTtl());
		
		} catch (Exception e) {
			throw new TokenCreateException("토큰 발행 중 오류. " + e.getMessage(), e);
		}

		return token;
	}
	
	public <T extends Cryptable<T>> void updateTokenPayload(String token, T newPayload, TokenType type) {
        log.debug("* {} 타입 토큰 페이로드 업데이트.", type);

		if (!StringUtils.hasText(token)) {
			throw new TokenIllegalException(type.name() + " Token null 또는 비어있음.");
		}
		
        String key = type.getPrefix() + token;
        
        // 기존 TTL 가져오기 (만료 시간 유지)
        Long expire = redisTemplate.getExpire(key, TimeUnit.SECONDS);
        if (expire == null || expire <= 0) {
            throw new TokenIllegalException("유효하지 않거나 만료된 토큰의 페이로드는 업데이트할 수 없습니다.");
        }

        try {
            String newJson = tokenProvider.createTokenValue(newPayload);
            redisTemplate.opsForValue().set(key, newJson, type.getTtl());
        } catch (Exception e) {
            throw new TokenCreateException("토큰 페이로드 업데이트 중 오류.", e);
        }
    }
	

	/**
	 * Redis에서 토큰 조회
	 * 
	 * @param tokenType
	 * @param token
	 * 
	 * @return 복호화된 데이터가 담긴 DTO 객체
	 * 
	 * @throws TokenIllegalException {@link TokenService#getPayload} 토큰 값 null 또는 비어있음 
	 * @throws TokenParseException {@link TokenService#getPayload} value 파싱 중 익셉션 발생
	 * 
	 */
	public <T extends Cryptable<T>> T restoreTokenDto(TokenType type, String token) {
		log.debug("* 토큰 검증. token: [{}]", type, LogMaskingUtil.maskToken(token, MaskLevel.MEDIUM));

		if (!StringUtils.hasText(token)) {
			throw new TokenIllegalException(type.name() + " Token null 또는 비어있음.");
		}
		String key = type.getPrefix() + token;
		String value = redisTemplate.opsForValue().get(key);

		if (!StringUtils.hasText(value)) {
			throw new TokenIllegalException(type.name() + " 토큰에 경로에 일치하는 값이 없음.");
		}
		
		try {
			// 역질력화 및 복호화
			return tokenProvider.parseTokenValue(type, value);
		} catch (Exception e) {
			throw new TokenParseException("파싱 중 익셉션 발생.", e);
		}
	}
	
	/**
	 * 토큰 삭제
	 * 
	 * @param type
	 * @param key
	 * 
	 * @return 삭제 여부
	 */
	public boolean deleteToken(TokenType type, String key) {
		log.debug("* Redis에서 {} 타입 토큰 삭제 조회. type: [{}]", type, LogMaskingUtil.maskToken(key, MaskLevel.MEDIUM));

		return redisTemplate.delete(type.getPrefix() + key);
	}
	

	/**
	 * Redis에 토큰 존재 여부 확인
	 * 
	 * @param type
	 * @param key
	 * 
	 * @return 존재 여부
	 */
	public boolean isExist(TokenType type, String key) {
		return Boolean.TRUE.equals(redisTemplate.hasKey(type.getPrefix() + key));
	}
	
	public boolean validateTokenPurpose(TokenPurpose tokenPurpose) {
		return false;
	}
	
}
