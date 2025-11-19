package net.chamman.shoppingmall_admin.exception.domain.order;

import net.chamman.shoppingmall_admin.exception.CustomException;
import net.chamman.shoppingmall_admin.exception.HttpStatusCode;

public class OrderReturnIllegalException extends CustomException{

	public OrderReturnIllegalException(Exception e) {
		super(HttpStatusCode.ORDER_ITEM_ILLEGAL, e);
	}

	public OrderReturnIllegalException(String message, Exception e) {
		super(HttpStatusCode.ORDER_ITEM_ILLEGAL, message, e);
	}

	public OrderReturnIllegalException(String message) {
		super(HttpStatusCode.ORDER_ITEM_ILLEGAL, message);
	}

	public OrderReturnIllegalException() {
		super(HttpStatusCode.ORDER_ITEM_ILLEGAL);
	}

}
