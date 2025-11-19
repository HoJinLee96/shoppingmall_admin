package net.chamman.shoppingmall_admin.exception.security.jwt;

import net.chamman.shoppingmall_admin.exception.CustomException;
import net.chamman.shoppingmall_admin.exception.HttpStatusCode;

public class JwtRefreshException extends CustomException{

	public JwtRefreshException(Exception e) {
		super(HttpStatusCode.JWT_REFRESH_FIALURE, e);
	}

	public JwtRefreshException(String message, Exception e) {
		super(HttpStatusCode.JWT_REFRESH_FIALURE, message, e);
	}

	public JwtRefreshException(String message) {
		super(HttpStatusCode.JWT_REFRESH_FIALURE, message);
	}

	public JwtRefreshException() {
		super(HttpStatusCode.JWT_REFRESH_FIALURE);
	}
	
}
