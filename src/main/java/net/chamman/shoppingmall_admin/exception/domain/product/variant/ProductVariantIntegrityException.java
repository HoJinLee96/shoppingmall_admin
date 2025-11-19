package net.chamman.shoppingmall_admin.exception.domain.product.variant;

import net.chamman.shoppingmall_admin.exception.CriticalException;

public class ProductVariantIntegrityException extends CriticalException{

	public ProductVariantIntegrityException() {
		super();
	}

	public ProductVariantIntegrityException(Exception e) {
		super(e);
	}

	public ProductVariantIntegrityException(String message, Exception e) {
		super(message, e);
	}

	public ProductVariantIntegrityException(String message) {
		super(message);
	}

}
