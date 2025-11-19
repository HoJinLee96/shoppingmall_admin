package net.chamman.shoppingmall_admin.exception.common;

import net.chamman.shoppingmall_admin.exception.CustomException;
import net.chamman.shoppingmall_admin.exception.HttpStatusCode;

public class ValidationException  extends CustomException {

	public ValidationException(Exception e) {
		super(HttpStatusCode.VALIDATION_FAILED, e);
	}

	public ValidationException(String message, Exception e) {
		super(HttpStatusCode.VALIDATION_FAILED, message, e);
	}

	public ValidationException(String message) {
		super(HttpStatusCode.VALIDATION_FAILED, message);
	}

	public ValidationException() {
		super(HttpStatusCode.VALIDATION_FAILED);
	}
}