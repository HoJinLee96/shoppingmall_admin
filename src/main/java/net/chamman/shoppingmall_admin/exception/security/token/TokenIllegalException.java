package net.chamman.shoppingmall_admin.exception.security.token;

import net.chamman.shoppingmall_admin.exception.CustomException;
import net.chamman.shoppingmall_admin.exception.HttpStatusCode;

public class TokenIllegalException extends CustomException{

	public TokenIllegalException(Exception e) {
		super(HttpStatusCode.TOKEN_ILLEGAL, e);
	}

	public TokenIllegalException(String message, Exception e) {
		super(HttpStatusCode.TOKEN_ILLEGAL, message, e);
	}

	public TokenIllegalException(String message) {
		super(HttpStatusCode.TOKEN_ILLEGAL, message);
	}

	public TokenIllegalException() {
		super(HttpStatusCode.TOKEN_ILLEGAL);
	}
	
}
