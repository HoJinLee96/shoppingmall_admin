package net.chamman.shoppingmall_admin.exception.domain.address;

import net.chamman.shoppingmall_admin.exception.CustomException;
import net.chamman.shoppingmall_admin.exception.HttpStatusCode;

public class AddressInvalidException extends CustomException{

	public AddressInvalidException(Exception e) {
		super(HttpStatusCode.ADDRESS_INVALID, e);
	}

	public AddressInvalidException(String message, Exception e) {
		super(HttpStatusCode.ADDRESS_INVALID, message, e);
	}

	public AddressInvalidException(String message) {
		super(HttpStatusCode.ADDRESS_INVALID, message);
	}

	public AddressInvalidException() {
		super(HttpStatusCode.ADDRESS_INVALID);
	}
	
}
