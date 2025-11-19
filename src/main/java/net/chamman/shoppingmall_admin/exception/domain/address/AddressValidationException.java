package net.chamman.shoppingmall_admin.exception.domain.address;

import net.chamman.shoppingmall_admin.exception.CustomException;
import net.chamman.shoppingmall_admin.exception.HttpStatusCode;

public class AddressValidationException extends CustomException{

	public AddressValidationException(Exception e) {
		super(HttpStatusCode.ADDRESS_VALIDATION_FAILED, e);
	}

	public AddressValidationException(String message, Exception e) {
		super(HttpStatusCode.ADDRESS_VALIDATION_FAILED, message, e);
	}

	public AddressValidationException(String message) {
		super(HttpStatusCode.ADDRESS_VALIDATION_FAILED, message);
	}

	public AddressValidationException() {
		super(HttpStatusCode.ADDRESS_VALIDATION_FAILED);
	}
	
}
