package net.chamman.shoppingmall_admin.exception.domain.payment;

import net.chamman.shoppingmall_admin.exception.CustomException;
import net.chamman.shoppingmall_admin.exception.HttpStatusCode;

public class PaymentIllegalException extends CustomException{

	public PaymentIllegalException(Exception e) {
		super(HttpStatusCode.PAYMENT_ILLEGAL, e);
	}

	public PaymentIllegalException(String message, Exception e) {
		super(HttpStatusCode.PAYMENT_ILLEGAL, message, e);
	}

	public PaymentIllegalException(String message) {
		super(HttpStatusCode.PAYMENT_ILLEGAL, message);
	}

	public PaymentIllegalException() {
		super(HttpStatusCode.PAYMENT_ILLEGAL);
	}

}
