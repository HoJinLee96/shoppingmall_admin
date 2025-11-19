package net.chamman.shoppingmall_admin.exception.security.jwt;

import net.chamman.shoppingmall_admin.exception.CriticalException;

public class JwtRedisException extends CriticalException{

	public JwtRedisException() {
		super();
	}

	public JwtRedisException(Exception e) {
		super(e);
	}

	public JwtRedisException(String message, Exception e) {
		super(message, e);
	}

	public JwtRedisException(String message) {
		super(message);
	}
	
}
