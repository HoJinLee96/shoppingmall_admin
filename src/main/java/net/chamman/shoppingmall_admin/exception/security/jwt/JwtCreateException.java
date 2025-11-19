package net.chamman.shoppingmall_admin.exception.security.jwt;

import net.chamman.shoppingmall_admin.exception.CriticalException;

public class JwtCreateException extends CriticalException{

	public JwtCreateException(Exception e) {
		super(e);
	}

	public JwtCreateException(String message, Exception e) {
		super(message, e);
	}

	public JwtCreateException(String message) {
		super(message);
	}

	public JwtCreateException() {
		super();
	}
	
	

}
