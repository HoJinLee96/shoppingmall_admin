package net.chamman.shoppingmall_admin.exception.domain.admin.status;

import net.chamman.shoppingmall_admin.exception.CustomException;
import net.chamman.shoppingmall_admin.exception.HttpStatusCode;

public class AdminStatusException extends CustomException {

	public AdminStatusException(HttpStatusCode httpStatusCode, Exception e) {
		super(httpStatusCode, e);
	}

	public AdminStatusException(HttpStatusCode httpStatusCode, String message, Exception e) {
		super(httpStatusCode, message, e);
	}

	public AdminStatusException(HttpStatusCode httpStatusCode, String message) {
		super(httpStatusCode, message);
	}

	public AdminStatusException(HttpStatusCode httpStatusCode) {
		super(httpStatusCode);
	}

}
