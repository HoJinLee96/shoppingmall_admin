package net.chamman.shoppingmall_admin.security.token;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.chamman.shoppingmall_admin.exception.security.crypto.DecryptException;
import net.chamman.shoppingmall_admin.exception.security.crypto.EncryptException;
import net.chamman.shoppingmall_admin.security.crypto.AesProvider;
import net.chamman.shoppingmall_admin.security.crypto.AesService;
import net.chamman.shoppingmall_admin.security.crypto.Cryptable;
import net.chamman.shoppingmall_admin.security.token.TokenService.TokenType;
import net.chamman.shoppingmall_admin.support.util.LogMaskingUtil;
import net.chamman.shoppingmall_admin.support.util.LogMaskingUtil.MaskLevel;

@Component
@Slf4j
@RequiredArgsConstructor
public class TokenProvider {

	private final AesService aesService;
	private ObjectMapper objectMapper = new ObjectMapper();


	/**
	 * DTO 암호화 및 직렬화
	 * 
	 * @param <T> Encryptable 인터페이스 구현체
	 * @param dto 암호화 될 DTO 객체
	 * 
	 * @return 토큰
	 * 
	 * @throws EncryptException{@link #ENCRYPT_FAIL} {@link AesProvider#encrypt} 암호화 실패
	 * @throws JsonProcessingException
	 */
	public <T extends Cryptable<T>> String createTokenValue(T dto) throws JsonProcessingException {

		// 1. DTO에 정의된 방식대로 암호화 수행
		T encryptedDto = aesService.encrypt(dto);

		// 2. 암호화된 DTO 객체를 JSON 문자열로 직렬화
		return objectMapper.writeValueAsString(encryptedDto);

	}

	/**
	 * DTO 역직렬화 및 복호화
	 * 
	 * @param <T> Encryptable 인터페이스 구현체
	 * @param type 역직렬화할 DTO의 Type
	 * @param value 역직렬화할 값
	 * 
	 * @return 복호화된 데이터가 담긴 DTO 객체
	 * 
	 * @throws DecryptException{@link #DECRYPT_FAIL} {@link AesProvider#decrypt} 복호화 실패
	 * @throws JsonProcessingException
	 */
	public <T extends Cryptable<T>> T parseTokenValue(TokenType type, String value) throws JsonProcessingException{
		log.debug("* {} 타입 토큰 검증. Token: [{}]", type, LogMaskingUtil.maskToken(value, MaskLevel.MEDIUM));

		@SuppressWarnings("unchecked")
		T encryptedDto = (T) objectMapper.readValue(value, type.getDtoType());

		return aesService.decrypt(encryptedDto);

	}

}