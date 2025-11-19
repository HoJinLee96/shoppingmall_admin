package net.chamman.shoppingmall_admin.exception.domain.admin;

import net.chamman.shoppingmall_admin.exception.CustomException;
import net.chamman.shoppingmall_admin.exception.HttpStatusCode;

public class AdminNotFoundException extends CustomException{

	public AdminNotFoundException(Exception e) {
		super(HttpStatusCode.ADMIN_NOT_FOUND, e);
	}

	public AdminNotFoundException(String message, Exception e) {
		super(HttpStatusCode.ADMIN_NOT_FOUND, message, e);
	}

	public AdminNotFoundException(String message) {
		super(HttpStatusCode.ADMIN_NOT_FOUND, message);
	}

	public AdminNotFoundException() {
		super(HttpStatusCode.ADMIN_NOT_FOUND);
	}

	
}
