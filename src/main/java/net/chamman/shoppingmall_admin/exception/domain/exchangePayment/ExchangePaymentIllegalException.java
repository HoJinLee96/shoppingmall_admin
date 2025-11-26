package net.chamman.shoppingmall_admin.exception.domain.exchangePayment;

import net.chamman.shoppingmall_admin.exception.CustomException;
import net.chamman.shoppingmall_admin.exception.HttpStatusCode;

public class ExchangePaymentIllegalException extends CustomException{

	public ExchangePaymentIllegalException(Exception e) {
		super(HttpStatusCode.EXCHANGE_PAYMENT_ILLEGAL, e);
	}

	public ExchangePaymentIllegalException(String message, Exception e) {
		super(HttpStatusCode.EXCHANGE_PAYMENT_ILLEGAL, message, e);
	}

	public ExchangePaymentIllegalException(String message) {
		super(HttpStatusCode.EXCHANGE_PAYMENT_ILLEGAL, message);
	}

	public ExchangePaymentIllegalException() {
		super(HttpStatusCode.EXCHANGE_PAYMENT_ILLEGAL);
	}

}
