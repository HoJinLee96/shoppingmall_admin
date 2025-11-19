package net.chamman.shoppingmall_admin.exception.security.jwt;

import net.chamman.shoppingmall_admin.exception.CriticalException;

public class JwtParseException extends CriticalException{

	public JwtParseException(Exception e) {
		super(e);
	}

	public JwtParseException(String message, Exception e) {
		super(message, e);
	}

	public JwtParseException(String message) {
		super(message);
	}

	public JwtParseException() {
		super();
	}
  
}
