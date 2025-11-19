package net.chamman.shoppingmall_admin.exception.domain.admin;

import net.chamman.shoppingmall_admin.exception.CustomException;
import net.chamman.shoppingmall_admin.exception.HttpStatusCode;

public class AdminEmailDuplicationException extends CustomException{

	public AdminEmailDuplicationException(Exception e) {
		super(HttpStatusCode.ADMIN_EMAIL_DUPLICATION, e);
	}

	public AdminEmailDuplicationException(String message, Exception e) {
		super(HttpStatusCode.ADMIN_EMAIL_DUPLICATION, message, e);
	}

	public AdminEmailDuplicationException(String message) {
		super(HttpStatusCode.ADMIN_EMAIL_DUPLICATION, message);
	}

	public AdminEmailDuplicationException() {
		super(HttpStatusCode.ADMIN_EMAIL_DUPLICATION);
	}
	
}
