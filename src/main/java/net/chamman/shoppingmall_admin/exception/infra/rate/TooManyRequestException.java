package net.chamman.shoppingmall_admin.exception.infra.rate;

import net.chamman.shoppingmall_admin.exception.CustomException;
import net.chamman.shoppingmall_admin.exception.HttpStatusCode;

public class TooManyRequestException extends CustomException{

	public TooManyRequestException(Exception e) {
		super(HttpStatusCode.TOO_MANY_REQUEST, e);
	}

	public TooManyRequestException(String message, Exception e) {
		super(HttpStatusCode.TOO_MANY_REQUEST, message, e);
	}

	public TooManyRequestException(String message) {
		super(HttpStatusCode.TOO_MANY_REQUEST, message);
	}

	public TooManyRequestException() {
		super(HttpStatusCode.TOO_MANY_REQUEST);
	}

	
}
