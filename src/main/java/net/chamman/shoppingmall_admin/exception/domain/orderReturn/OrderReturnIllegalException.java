package net.chamman.shoppingmall_admin.exception.domain.orderReturn;

import net.chamman.shoppingmall_admin.exception.CustomException;
import net.chamman.shoppingmall_admin.exception.HttpStatusCode;

public class OrderReturnIllegalException extends CustomException{

	public OrderReturnIllegalException(Exception e) {
		super(HttpStatusCode.ORDER_RETURN_ILLEGAL, e);
	}

	public OrderReturnIllegalException(String message, Exception e) {
		super(HttpStatusCode.ORDER_RETURN_ILLEGAL, message, e);
	}

	public OrderReturnIllegalException(String message) {
		super(HttpStatusCode.ORDER_RETURN_ILLEGAL, message);
	}

	public OrderReturnIllegalException() {
		super(HttpStatusCode.ORDER_RETURN_ILLEGAL);
	}

}
