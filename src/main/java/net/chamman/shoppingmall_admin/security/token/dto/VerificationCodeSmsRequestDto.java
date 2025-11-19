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
public class VerificationCodeSmsRequestDto implements Cryptable<VerificationCodeSmsRequestDto> {

	String phone;
	String code;

	public static final TokenType TOKENTYPE = TokenType.VERIFICATION_CODE_SMS_REQUEST;

	@Override
	public VerificationCodeSmsRequestDto encrypt(AesProvider aesProvider) {
		return new VerificationCodeSmsRequestDto(
				aesProvider.encrypt(phone), 
				aesProvider.encrypt(code));
	}

	@Override
	public VerificationCodeSmsRequestDto decrypt(AesProvider aesProvider) {
		return new VerificationCodeSmsRequestDto(
				aesProvider.decrypt(phone), 
				aesProvider.decrypt(code));
	}

}
