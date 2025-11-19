package net.chamman.shoppingmall_admin.exception.domain.order;

import net.chamman.shoppingmall_admin.exception.CriticalException;

public class OrderItemIntegrityException extends CriticalException{

	public OrderItemIntegrityException() {
		super();
	}

	public OrderItemIntegrityException(Exception e) {
		super(e);
	}

	public OrderItemIntegrityException(String message, Exception e) {
		super(message, e);
	}

	public OrderItemIntegrityException(String message) {
		super(message);
	}

}
