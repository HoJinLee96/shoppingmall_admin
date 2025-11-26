package net.chamman.shoppingmall_admin.exception.domain.returnPayment;

import net.chamman.shoppingmall_admin.exception.CustomException;
import net.chamman.shoppingmall_admin.exception.HttpStatusCode;

public class ReturnPaymentIllegalException extends CustomException{

	public ReturnPaymentIllegalException(Exception e) {
		super(HttpStatusCode.RETURN_PAYMENT_ILLEGAL, e);
	}

	public ReturnPaymentIllegalException(String message, Exception e) {
		super(HttpStatusCode.RETURN_PAYMENT_ILLEGAL, message, e);
	}

	public ReturnPaymentIllegalException(String message) {
		super(HttpStatusCode.RETURN_PAYMENT_ILLEGAL, message);
	}

	public ReturnPaymentIllegalException() {
		super(HttpStatusCode.RETURN_PAYMENT_ILLEGAL);
	}

}
