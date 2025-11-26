package net.chamman.shoppingmall_admin.exception.domain.order;

import net.chamman.shoppingmall_admin.exception.CustomException;
import net.chamman.shoppingmall_admin.exception.HttpStatusCode;

public class OrderIllegalException extends CustomException{

	public OrderIllegalException(Exception e) {
		super(HttpStatusCode.ORDER_ILLEGAL, e);
	}

	public OrderIllegalException(String message, Exception e) {
		super(HttpStatusCode.ORDER_ILLEGAL, message, e);
	}

	public OrderIllegalException(String message) {
		super(HttpStatusCode.ORDER_ILLEGAL, message);
	}

	public OrderIllegalException() {
		super(HttpStatusCode.ORDER_ILLEGAL);
	}

}
