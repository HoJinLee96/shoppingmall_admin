package net.chamman.shoppingmall_admin.exception.domain.returnPayment;

import net.chamman.shoppingmall_admin.exception.CriticalException;

public class ReturnPaymentIntegrityException extends CriticalException{

	public ReturnPaymentIntegrityException() {
		super();
	}

	public ReturnPaymentIntegrityException(Exception e) {
		super(e);
	}

	public ReturnPaymentIntegrityException(String message, Exception e) {
		super(message, e);
	}

	public ReturnPaymentIntegrityException(String message) {
		super(message);
	}

}
