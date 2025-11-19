package net.chamman.shoppingmall_admin.security.token.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import net.chamman.shoppingmall_admin.security.crypto.AesProvider;
import net.chamman.shoppingmall_admin.security.crypto.Cryptable;
import net.chamman.shoppingmall_admin.security.token.TokenService.TokenPurpose;
import net.chamman.shoppingmall_admin.security.token.TokenService.TokenType;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class VerificationCodeSmsSuccessDto implements Cryptable<VerificationCodeSmsSuccessDto> {

	TokenPurpose tokenPurpose;
	String phone;

	public static final TokenType TOKENTYPE = TokenType.VERIFICATION_CODE_SMS_SUCCESS;

	@Override
	public VerificationCodeSmsSuccessDto encrypt(AesProvider aesProvider) {
		return new VerificationCodeSmsSuccessDto(
				tokenPurpose,
				aesProvider.encrypt(phone));
	}

	@Override
	public VerificationCodeSmsSuccessDto decrypt(AesProvider aesProvider) {
		return new VerificationCodeSmsSuccessDto(
				tokenPurpose,
				aesProvider.decrypt(phone));
	}

}
