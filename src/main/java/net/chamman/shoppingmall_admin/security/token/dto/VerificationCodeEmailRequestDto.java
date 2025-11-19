package net.chamman.shoppingmall_admin.security.token.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import net.chamman.shoppingmall_admin.security.crypto.AesProvider;
import net.chamman.shoppingmall_admin.security.crypto.Cryptable;
import net.chamman.shoppingmall_admin.security.token.TokenService.TokenType;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class VerificationCodeEmailRequestDto implements Cryptable<VerificationCodeEmailRequestDto> {

	String email;
	String code;

	public static final TokenType TOKENTYPE = TokenType.VERIFICATION_CODE_EMAIL_REQUEST;
	@Override
	public VerificationCodeEmailRequestDto encrypt(AesProvider aesProvider) {
		return new VerificationCodeEmailRequestDto(
				aesProvider.encrypt(email), 
				aesProvider.encrypt(code));
	}

	@Override
	public VerificationCodeEmailRequestDto decrypt(AesProvider aesProvider) {
		return new VerificationCodeEmailRequestDto(
				aesProvider.decrypt(email), 
				aesProvider.decrypt(code));
	}

}
