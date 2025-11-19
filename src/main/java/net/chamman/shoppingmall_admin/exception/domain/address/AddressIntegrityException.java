package net.chamman.shoppingmall_admin.exception.domain.address;

import net.chamman.shoppingmall_admin.exception.IntegrityException;

public class AddressIntegrityException extends IntegrityException{
	
	public AddressIntegrityException(Exception e) {
		super(e);
	}

	public AddressIntegrityException(String message, Exception e) {
		super(message, e);
	}

	public AddressIntegrityException(String message) {
		super(message);
	}

	public AddressIntegrityException() {
		super();
	}
	
}
