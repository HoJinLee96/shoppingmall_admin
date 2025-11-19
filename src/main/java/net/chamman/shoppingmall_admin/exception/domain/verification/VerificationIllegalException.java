package net.chamman.shoppingmall_admin.exception.domain.verification;

import net.chamman.shoppingmall_admin.exception.CustomException;
import net.chamman.shoppingmall_admin.exception.HttpStatusCode;

public class VerificationIllegalException extends CustomException{

	public VerificationIllegalException(Exception e) {
		super(HttpStatusCode.VERIFICATION_ILLEGAL, e);
	}

	public VerificationIllegalException(String message, Exception e) {
		super(HttpStatusCode.VERIFICATION_ILLEGAL, message, e);
	}

	public VerificationIllegalException(String message) {
		super(HttpStatusCode.VERIFICATION_ILLEGAL, message);
	}

	public VerificationIllegalException() {
		super(HttpStatusCode.VERIFICATION_ILLEGAL);
	}
	
}
