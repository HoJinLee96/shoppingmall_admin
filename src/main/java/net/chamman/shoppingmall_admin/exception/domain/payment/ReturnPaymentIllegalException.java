package net.chamman.shoppingmall_admin.exception.domain.payment;

import net.chamman.shoppingmall_admin.exception.CustomException;
import net.chamman.shoppingmall_admin.exception.HttpStatusCode;

public class ReturnPaymentIllegalException extends CustomException{

	public ReturnPaymentIllegalException(Exception e) {
		super(HttpStatusCode.ORDER_ITEM_ILLEGAL, e);
	}

	public ReturnPaymentIllegalException(String message, Exception e) {
		super(HttpStatusCode.ORDER_ITEM_ILLEGAL, message, e);
	}

	public ReturnPaymentIllegalException(String message) {
		super(HttpStatusCode.ORDER_ITEM_ILLEGAL, message);
	}

	public ReturnPaymentIllegalException() {
		super(HttpStatusCode.ORDER_ITEM_ILLEGAL);
	}

}
