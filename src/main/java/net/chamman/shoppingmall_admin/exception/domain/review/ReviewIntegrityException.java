package net.chamman.shoppingmall_admin.exception.domain.review;

import net.chamman.shoppingmall_admin.exception.IntegrityException;

public class ReviewIntegrityException extends IntegrityException{
	
	public ReviewIntegrityException(Exception e) {
		super(e);
	}

	public ReviewIntegrityException(String message, Exception e) {
		super(message, e);
	}

	public ReviewIntegrityException(String message) {
		super(message);
	}

	public ReviewIntegrityException() {
		super();
	}
	
}
