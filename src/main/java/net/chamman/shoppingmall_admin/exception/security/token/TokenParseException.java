package net.chamman.shoppingmall_admin.exception.security.token;

import net.chamman.shoppingmall_admin.exception.CriticalException;

public class TokenParseException extends CriticalException{

	public TokenParseException(Exception e) {
		super(e);
	}

	public TokenParseException(String message, Exception e) {
		super(message, e);
	}

	public TokenParseException(String message) {
		super(message);
	}

	public TokenParseException() {
		super();
	}

}
