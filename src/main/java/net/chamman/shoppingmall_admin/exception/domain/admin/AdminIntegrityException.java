package net.chamman.shoppingmall_admin.exception.domain.admin;

import net.chamman.shoppingmall_admin.exception.IntegrityException;

public class AdminIntegrityException extends IntegrityException{

	public AdminIntegrityException() {
		super();
	}

	public AdminIntegrityException(Exception e) {
		super(e);
	}

	public AdminIntegrityException(String message, Exception e) {
		super(message, e);
	}

	public AdminIntegrityException(String message) {
		super(message);
	}
	
}
