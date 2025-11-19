package net.chamman.shoppingmall_admin.exception.domain.product.variant;

import net.chamman.shoppingmall_admin.exception.CustomException;
import net.chamman.shoppingmall_admin.exception.HttpStatusCode;

public class StockNotEnoughException extends CustomException {

	public StockNotEnoughException(Exception e) {
		super(HttpStatusCode.STOCK_NOT_ENOUGH, e);
	}

	public StockNotEnoughException(String message, Exception e) {
		super(HttpStatusCode.STOCK_NOT_ENOUGH, message, e);
	}

	public StockNotEnoughException(String message) {
		super(HttpStatusCode.STOCK_NOT_ENOUGH, message);
	}

	public StockNotEnoughException() {
		super(HttpStatusCode.STOCK_NOT_ENOUGH);
	}
}