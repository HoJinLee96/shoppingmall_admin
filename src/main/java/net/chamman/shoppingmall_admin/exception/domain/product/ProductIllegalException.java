package net.chamman.shoppingmall_admin.exception.domain.product;

import net.chamman.shoppingmall_admin.exception.CustomException;
import net.chamman.shoppingmall_admin.exception.HttpStatusCode;

public class ProductIllegalException extends CustomException{

	public ProductIllegalException(Exception e) {
		super(HttpStatusCode.PRODUCT_ILLEGAL, e);
	}

	public ProductIllegalException(String message, Exception e) {
		super(HttpStatusCode.PRODUCT_ILLEGAL, message, e);
	}

	public ProductIllegalException(String message) {
		super(HttpStatusCode.PRODUCT_ILLEGAL, message);
	}

	public ProductIllegalException() {
		super(HttpStatusCode.PRODUCT_ILLEGAL);
	}

}
