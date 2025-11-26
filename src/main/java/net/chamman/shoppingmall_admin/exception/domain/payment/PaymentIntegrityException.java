package net.chamman.shoppingmall_admin.exception.domain.payment;

import net.chamman.shoppingmall_admin.exception.CriticalException;

public class PaymentIntegrityException extends CriticalException{

	public PaymentIntegrityException() {
		super();
	}

	public PaymentIntegrityException(Exception e) {
		super(e);
	}

	public PaymentIntegrityException(String message, Exception e) {
		super(message, e);
	}

	public PaymentIntegrityException(String message) {
		super(message);
	}

}
