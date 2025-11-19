package net.chamman.shoppingmall_admin.exception.security.jwt;

import net.chamman.shoppingmall_admin.exception.CustomException;
import net.chamman.shoppingmall_admin.exception.HttpStatusCode;

public class JwtIllegalException extends CustomException{

	public JwtIllegalException(Exception e) {
		super(HttpStatusCode.JWT_ILLEGAL, e);
	}

	public JwtIllegalException(String message, Exception e) {
		super(HttpStatusCode.JWT_ILLEGAL, message, e);
	}

	public JwtIllegalException(String message) {
		super(HttpStatusCode.JWT_ILLEGAL, message);
	}

	public JwtIllegalException() {
		super(HttpStatusCode.JWT_ILLEGAL);
	}
  
}
