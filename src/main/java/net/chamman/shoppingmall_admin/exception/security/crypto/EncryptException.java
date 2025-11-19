package net.chamman.shoppingmall_admin.exception.security.crypto;

import net.chamman.shoppingmall_admin.exception.CriticalException;

public class EncryptException extends CriticalException{

	public EncryptException(Exception e) {
		super(e);
	}

	public EncryptException(String message, Exception e) {
		super(message, e);

	}

	public EncryptException(String message) {
		super(message);

	}

	public EncryptException() {
		super();

	}

	
}
