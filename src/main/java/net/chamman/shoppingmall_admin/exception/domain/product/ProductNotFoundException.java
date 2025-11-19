package net.chamman.shoppingmall_admin.exception.domain.product;

import net.chamman.shoppingmall_admin.exception.CustomException;
import net.chamman.shoppingmall_admin.exception.HttpStatusCode;

public class ProductNotFoundException extends CustomException {

	public ProductNotFoundException(Exception e) {
		super(HttpStatusCode.PRODUCT_NOT_FOUND, e);
	}

	public ProductNotFoundException(String message, Exception e) {
		super(HttpStatusCode.PRODUCT_NOT_FOUND, message, e);
	}

	public ProductNotFoundException(String message) {
		super(HttpStatusCode.PRODUCT_NOT_FOUND, message);
	}

	public ProductNotFoundException() {
		super(HttpStatusCode.PRODUCT_NOT_FOUND);
	}
	
}
