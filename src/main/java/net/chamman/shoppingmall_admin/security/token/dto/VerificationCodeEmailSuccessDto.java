package net.chamman.shoppingmall_admin.security.token.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import net.chamman.shoppingmall_admin.security.crypto.AesProvider;
import net.chamman.shoppingmall_admin.security.crypto.Cryptable;
import net.chamman.shoppingmall_admin.security.token.TokenService.TokenPurpose;
import net.chamman.shoppingmall_admin.security.token.TokenService.TokenType;

@Getter
@ToString
@AllArgsConstructor
public class VerificationCodeEmailSuccessDto implements Cryptable<VerificationCodeEmailSuccessDto> {

	private final TokenPurpose tokenPurpose;
	private final String email;

	public static final TokenType TOKENTYPE = TokenType.VERIFICATION_CODE_EMAIL_SUCCESS;

	@Override
	public VerificationCodeEmailSuccessDto encrypt(AesProvider aesProvider) {
		return new VerificationCodeEmailSuccessDto(
				tokenPurpose,
				aesProvider.encrypt(email));
	}

	@Override
	public VerificationCodeEmailSuccessDto decrypt(AesProvider aesProvider) {
		return new VerificationCodeEmailSuccessDto(
				tokenPurpose,
				aesProvider.decrypt(email));
	}

}
