package net.chamman.shoppingmall_admin.exception.domain.admin.sign;

import net.chamman.shoppingmall_admin.exception.CustomException;
import net.chamman.shoppingmall_admin.exception.HttpStatusCode;

public class SignInFailedException extends CustomException{

	public SignInFailedException(Exception e) {
		super(HttpStatusCode.ADMIN_SIGNIN_FAILED, e);
	}

	public SignInFailedException(String message, Exception e) {
		super(HttpStatusCode.ADMIN_SIGNIN_FAILED, message, e);
	}

	public SignInFailedException(String message) {
		super(HttpStatusCode.ADMIN_SIGNIN_FAILED, message);
	}

	public SignInFailedException() {
		super(HttpStatusCode.ADMIN_SIGNIN_FAILED);
	}
	
}
