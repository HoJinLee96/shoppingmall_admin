package net.chamman.shoppingmall_admin.exception.domain.admin;

import net.chamman.shoppingmall_admin.exception.CustomException;
import net.chamman.shoppingmall_admin.exception.HttpStatusCode;

public class AdminPasswordMismatchException extends CustomException{

	public AdminPasswordMismatchException(Exception e) {
		super(HttpStatusCode.ADMIN_PASSWORD_MISMATCH, e);
	}

	public AdminPasswordMismatchException(String message, Exception e) {
		super(HttpStatusCode.ADMIN_PASSWORD_MISMATCH, message, e);
	}

	public AdminPasswordMismatchException(String message) {
		super(HttpStatusCode.ADMIN_PASSWORD_MISMATCH, message);
	}

	public AdminPasswordMismatchException() {
		super(HttpStatusCode.ADMIN_PASSWORD_MISMATCH);
	}

}
