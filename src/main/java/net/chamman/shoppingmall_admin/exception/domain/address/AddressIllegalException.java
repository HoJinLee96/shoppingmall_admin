package net.chamman.shoppingmall_admin.exception.domain.address;

import net.chamman.shoppingmall_admin.exception.CustomException;
import net.chamman.shoppingmall_admin.exception.HttpStatusCode;

public class AddressIllegalException extends CustomException{

	public AddressIllegalException(Exception e) {
		super(HttpStatusCode.ADDRESS_ILLEGAL, e);
	}

	public AddressIllegalException(String message, Exception e) {
		super(HttpStatusCode.ADDRESS_ILLEGAL, message, e);
	}

	public AddressIllegalException(String message) {
		super(HttpStatusCode.ADDRESS_ILLEGAL, message);
	}

	public AddressIllegalException() {
		super(HttpStatusCode.ADDRESS_ILLEGAL);
	}
	
}
