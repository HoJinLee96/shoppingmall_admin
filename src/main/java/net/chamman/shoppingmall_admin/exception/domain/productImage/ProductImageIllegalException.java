package net.chamman.shoppingmall_admin.exception.domain.productImage;

import net.chamman.shoppingmall_admin.exception.CustomException;
import net.chamman.shoppingmall_admin.exception.HttpStatusCode;

public class ProductImageIllegalException extends CustomException{

	public ProductImageIllegalException(Exception e) {
		super(HttpStatusCode.PRODUCT_IMAGE_ILLEGAL, e);
	}

	public ProductImageIllegalException(String message, Exception e) {
		super(HttpStatusCode.PRODUCT_IMAGE_ILLEGAL, message, e);
	}

	public ProductImageIllegalException(String message) {
		super(HttpStatusCode.PRODUCT_IMAGE_ILLEGAL, message);
	}

	public ProductImageIllegalException() {
		super(HttpStatusCode.PRODUCT_IMAGE_ILLEGAL);
	}

}
