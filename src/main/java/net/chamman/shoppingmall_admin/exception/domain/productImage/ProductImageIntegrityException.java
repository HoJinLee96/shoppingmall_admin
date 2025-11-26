package net.chamman.shoppingmall_admin.exception.domain.productImage;

import net.chamman.shoppingmall_admin.exception.CriticalException;

public class ProductImageIntegrityException extends CriticalException{

	public ProductImageIntegrityException() {
		super();
	}

	public ProductImageIntegrityException(Exception e) {
		super(e);
	}

	public ProductImageIntegrityException(String message, Exception e) {
		super(message, e);
	}

	public ProductImageIntegrityException(String message) {
		super(message);
	}

}
