package net.chamman.shoppingmall_admin.exception.common;

import net.chamman.shoppingmall_admin.exception.CustomException;
import net.chamman.shoppingmall_admin.exception.HttpStatusCode;

public class IllegalRequestException extends CustomException {

	public IllegalRequestException(Exception e) {
		super(HttpStatusCode.ILLEGAL_REQUEST, e);
	}

	public IllegalRequestException(String message, Exception e) {
		super(HttpStatusCode.ILLEGAL_REQUEST, message, e);
	}

	public IllegalRequestException(String message) {
		super(HttpStatusCode.ILLEGAL_REQUEST, message);
	}

	public IllegalRequestException(HttpStatusCode httpStatusCode) {
		super(HttpStatusCode.ILLEGAL_REQUEST);
	}
}