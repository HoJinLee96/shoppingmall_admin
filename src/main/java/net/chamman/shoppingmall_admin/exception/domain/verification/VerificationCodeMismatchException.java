package net.chamman.shoppingmall_admin.exception.domain.verification;

import net.chamman.shoppingmall_admin.exception.CustomException;
import net.chamman.shoppingmall_admin.exception.HttpStatusCode;

public class VerificationCodeMismatchException extends CustomException{

	public VerificationCodeMismatchException(Exception e) {
		super(HttpStatusCode.VERIFICATION_CODE_MISMATCH, e);
	}

	public VerificationCodeMismatchException(String message, Exception e) {
		super(HttpStatusCode.VERIFICATION_CODE_MISMATCH, message, e);
	}

	public VerificationCodeMismatchException(String message) {
		super(HttpStatusCode.VERIFICATION_CODE_MISMATCH, message);
	}

	public VerificationCodeMismatchException() {
		super(HttpStatusCode.VERIFICATION_CODE_MISMATCH);
	}

}
