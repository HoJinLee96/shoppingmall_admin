package net.chamman.shoppingmall_admin.exception.domain.verification;

import net.chamman.shoppingmall_admin.exception.IntegrityException;

public class VerificationIntegrityException extends IntegrityException{
	
	public VerificationIntegrityException(Exception e) {
		super(e);
	}

	public VerificationIntegrityException(String message, Exception e) {
		super(message, e);
	}

	public VerificationIntegrityException(String message) {
		super(message);
	}

	public VerificationIntegrityException() {
		super();
	}
	
}
