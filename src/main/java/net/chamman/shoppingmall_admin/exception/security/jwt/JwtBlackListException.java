package net.chamman.shoppingmall_admin.exception.security.jwt;

import net.chamman.shoppingmall_admin.exception.CriticalException;

public class JwtBlackListException extends CriticalException{

	public JwtBlackListException() {
		super();
	}

	public JwtBlackListException(Exception e) {
		super(e);
	}

	public JwtBlackListException(String message, Exception e) {
		super(message, e);
	}

	public JwtBlackListException(String message) {
		super(message);
	}
	
}
