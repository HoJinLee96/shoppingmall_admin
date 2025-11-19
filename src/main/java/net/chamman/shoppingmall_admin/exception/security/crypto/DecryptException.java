package net.chamman.shoppingmall_admin.exception.security.crypto;

import net.chamman.shoppingmall_admin.exception.CriticalException;

public class DecryptException extends CriticalException{

	public DecryptException(Exception e) {
		super(e);

	}

	public DecryptException(String message, Exception e) {
		super(message, e);

	}

	public DecryptException(String message) {
		super(message);

	}

	public DecryptException() {
		super();

	}

}
