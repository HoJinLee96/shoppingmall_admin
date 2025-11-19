package net.chamman.shoppingmall_admin.exception.domain.product.category;

import net.chamman.shoppingmall_admin.exception.CriticalException;

public class ProductCategoryIntegrityException extends CriticalException{

	public ProductCategoryIntegrityException() {
		super();
	}

	public ProductCategoryIntegrityException(Exception e) {
		super(e);
	}

	public ProductCategoryIntegrityException(String message, Exception e) {
		super(message, e);
	}

	public ProductCategoryIntegrityException(String message) {
		super(message);
	}

}
