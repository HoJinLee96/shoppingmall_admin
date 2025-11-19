package net.chamman.shoppingmall_admin.exception.domain.product.variant;

import net.chamman.shoppingmall_admin.exception.CustomException;
import net.chamman.shoppingmall_admin.exception.HttpStatusCode;

public class StockValidationException extends CustomException {

	public StockValidationException(Exception e) {
		super(HttpStatusCode.STOCK_VALIDATION_FAILED, e);
	}

	public StockValidationException(String message, Exception e) {
		super(HttpStatusCode.STOCK_VALIDATION_FAILED, message, e);
	}

	public StockValidationException(String message) {
		super(HttpStatusCode.STOCK_VALIDATION_FAILED, message);
	}

	public StockValidationException() {
		super(HttpStatusCode.STOCK_VALIDATION_FAILED);
	}

	
}
