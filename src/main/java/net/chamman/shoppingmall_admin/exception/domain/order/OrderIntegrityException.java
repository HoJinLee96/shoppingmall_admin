package net.chamman.shoppingmall_admin.exception.domain.order;

import net.chamman.shoppingmall_admin.exception.CriticalException;

public class OrderIntegrityException extends CriticalException{

	public OrderIntegrityException() {
		super();
	}

	public OrderIntegrityException(Exception e) {
		super(e);
	}

	public OrderIntegrityException(String message, Exception e) {
		super(message, e);
	}

	public OrderIntegrityException(String message) {
		super(message);
	}

}
