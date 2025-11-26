package net.chamman.shoppingmall_admin.exception.domain.orderExchange;

import net.chamman.shoppingmall_admin.exception.CustomException;
import net.chamman.shoppingmall_admin.exception.HttpStatusCode;

public class OrderExchangeIllegalException extends CustomException{

	public OrderExchangeIllegalException(Exception e) {
		super(HttpStatusCode.ORDER_EXCHANGE_ILLEGAL, e);
	}

	public OrderExchangeIllegalException(String message, Exception e) {
		super(HttpStatusCode.ORDER_EXCHANGE_ILLEGAL, message, e);
	}

	public OrderExchangeIllegalException(String message) {
		super(HttpStatusCode.ORDER_EXCHANGE_ILLEGAL, message);
	}

	public OrderExchangeIllegalException() {
		super(HttpStatusCode.ORDER_EXCHANGE_ILLEGAL);
	}

}
