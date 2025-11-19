package net.chamman.shoppingmall_admin.exception.security.token;

import net.chamman.shoppingmall_admin.exception.CriticalException;

public class TokenCreateException extends CriticalException{

	public TokenCreateException(Exception e) {
		super(e);
	}

	public TokenCreateException(String message, Exception e) {
		super(message, e);
	}

	public TokenCreateException(String message) {
		super(message);
	}

	public TokenCreateException() {
		super();
	}
	
}
