package net.chamman.shoppingmall_admin.exception.domain.product.variant;

import net.chamman.shoppingmall_admin.exception.CustomException;
import net.chamman.shoppingmall_admin.exception.HttpStatusCode;

public class ProductVariantIllegalException extends CustomException{

	public ProductVariantIllegalException(Exception e) {
		super(HttpStatusCode.PRODUCT_VARIANT_ILLEGAL, e);
	}

	public ProductVariantIllegalException(String message, Exception e) {
		super(HttpStatusCode.PRODUCT_VARIANT_ILLEGAL, message, e);
	}

	public ProductVariantIllegalException(String message) {
		super(HttpStatusCode.PRODUCT_VARIANT_ILLEGAL, message);
	}

	public ProductVariantIllegalException() {
		super(HttpStatusCode.PRODUCT_VARIANT_ILLEGAL);
	}

}
