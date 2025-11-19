package net.chamman.shoppingmall_admin.exception.domain.product;

import net.chamman.shoppingmall_admin.exception.CustomException;
import net.chamman.shoppingmall_admin.exception.HttpStatusCode;

public class ProductImageIllegalException extends CustomException{

	public ProductImageIllegalException(Exception e) {
		super(HttpStatusCode.PRODUCT_VARIANT_ILLEGAL, e);
	}

	public ProductImageIllegalException(String message, Exception e) {
		super(HttpStatusCode.PRODUCT_VARIANT_ILLEGAL, message, e);
	}

	public ProductImageIllegalException(String message) {
		super(HttpStatusCode.PRODUCT_VARIANT_ILLEGAL, message);
	}

	public ProductImageIllegalException() {
		super(HttpStatusCode.PRODUCT_VARIANT_ILLEGAL);
	}

}
