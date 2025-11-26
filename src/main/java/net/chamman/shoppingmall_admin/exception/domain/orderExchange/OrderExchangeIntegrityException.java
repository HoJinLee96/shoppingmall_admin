package net.chamman.shoppingmall_admin.exception.domain.orderExchange;

import net.chamman.shoppingmall_admin.exception.CriticalException;

public class OrderExchangeIntegrityException extends CriticalException{

	public OrderExchangeIntegrityException() {
		super();
	}

	public OrderExchangeIntegrityException(Exception e) {
		super(e);
	}

	public OrderExchangeIntegrityException(String message, Exception e) {
		super(message, e);
	}

	public OrderExchangeIntegrityException(String message) {
		super(message);
	}

}
