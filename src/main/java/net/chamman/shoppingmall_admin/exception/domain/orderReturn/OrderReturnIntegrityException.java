package net.chamman.shoppingmall_admin.exception.domain.orderReturn;

import net.chamman.shoppingmall_admin.exception.IntegrityException;

public class OrderReturnIntegrityException extends IntegrityException{

	public OrderReturnIntegrityException() {
		super();
	}

	public OrderReturnIntegrityException(Exception e) {
		super(e);
	}

	public OrderReturnIntegrityException(String message, Exception e) {
		super(message, e);
	}

	public OrderReturnIntegrityException(String message) {
		super(message);
	}

}
