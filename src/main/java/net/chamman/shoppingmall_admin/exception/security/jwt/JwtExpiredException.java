package net.chamman.shoppingmall_admin.exception.security.jwt;

import net.chamman.shoppingmall_admin.exception.CustomException;
import net.chamman.shoppingmall_admin.exception.HttpStatusCode;

public class JwtExpiredException extends CustomException{

	public JwtExpiredException(Exception e) {
		super(HttpStatusCode.JWT_EXPIRED, e);
	}

	public JwtExpiredException(String message, Exception e) {
		super(HttpStatusCode.JWT_EXPIRED, message, e);
	}

	public JwtExpiredException(String message) {
		super(HttpStatusCode.JWT_EXPIRED, message);
	}

	public JwtExpiredException() {
		super(HttpStatusCode.JWT_EXPIRED);
	}
	
}
