package net.chamman.shoppingmall_admin.exception.domain.exchangePayment;

import net.chamman.shoppingmall_admin.exception.CriticalException;

public class ExchangePaymentIntegrityException extends CriticalException{

	public ExchangePaymentIntegrityException() {
		super();
	}

	public ExchangePaymentIntegrityException(Exception e) {
		super(e);
	}

	public ExchangePaymentIntegrityException(String message, Exception e) {
		super(message, e);
	}

	public ExchangePaymentIntegrityException(String message) {
		super(message);
	}

}
