package net.chamman.shoppingmall_admin.exception.domain.product;

import net.chamman.shoppingmall_admin.exception.CriticalException;

public class ProductIntegrityException extends CriticalException{

	public ProductIntegrityException() {
		super();
	}

	public ProductIntegrityException(Exception e) {
		super(e);
	}

	public ProductIntegrityException(String message, Exception e) {
		super(message, e);
	}

	public ProductIntegrityException(String message) {
		super(message);
	}

}
