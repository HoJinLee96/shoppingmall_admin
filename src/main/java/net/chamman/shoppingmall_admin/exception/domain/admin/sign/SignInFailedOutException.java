package net.chamman.shoppingmall_admin.exception.domain.admin.sign;

import net.chamman.shoppingmall_admin.exception.CustomException;
import net.chamman.shoppingmall_admin.exception.HttpStatusCode;

public class SignInFailedOutException extends CustomException{

	public SignInFailedOutException(Exception e) {
		super(HttpStatusCode.ADMIN_SIGNIN_FAILED_OUT, e);
	}

	public SignInFailedOutException(String message, Exception e) {
		super(HttpStatusCode.ADMIN_SIGNIN_FAILED_OUT, message, e);
	}

	public SignInFailedOutException(String message) {
		super(HttpStatusCode.ADMIN_SIGNIN_FAILED_OUT, message);
	}

	public SignInFailedOutException() {
		super(HttpStatusCode.ADMIN_SIGNIN_FAILED_OUT);
	}
	
}
