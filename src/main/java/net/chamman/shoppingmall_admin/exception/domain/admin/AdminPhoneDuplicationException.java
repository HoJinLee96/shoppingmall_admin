package net.chamman.shoppingmall_admin.exception.domain.admin;

import net.chamman.shoppingmall_admin.exception.CustomException;
import net.chamman.shoppingmall_admin.exception.HttpStatusCode;

public class AdminPhoneDuplicationException extends CustomException{

	public AdminPhoneDuplicationException(Exception e) {
		super(HttpStatusCode.ADMIN_PHONE_DUPLICATION, e);
	}

	public AdminPhoneDuplicationException(String message, Exception e) {
		super(HttpStatusCode.ADMIN_PHONE_DUPLICATION, message, e);
	}

	public AdminPhoneDuplicationException(String message) {
		super(HttpStatusCode.ADMIN_PHONE_DUPLICATION, message);
	}

	public AdminPhoneDuplicationException() {
		super(HttpStatusCode.ADMIN_PHONE_DUPLICATION);
	}

	
}
