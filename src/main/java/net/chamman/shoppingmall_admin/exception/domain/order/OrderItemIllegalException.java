package net.chamman.shoppingmall_admin.exception.domain.order;

import net.chamman.shoppingmall_admin.exception.CustomException;
import net.chamman.shoppingmall_admin.exception.HttpStatusCode;

public class OrderItemIllegalException extends CustomException{

	public OrderItemIllegalException(Exception e) {
		super(HttpStatusCode.ORDER_ITEM_ILLEGAL, e);
	}

	public OrderItemIllegalException(String message, Exception e) {
		super(HttpStatusCode.ORDER_ITEM_ILLEGAL, message, e);
	}

	public OrderItemIllegalException(String message) {
		super(HttpStatusCode.ORDER_ITEM_ILLEGAL, message);
	}

	public OrderItemIllegalException() {
		super(HttpStatusCode.ORDER_ITEM_ILLEGAL);
	}

}
