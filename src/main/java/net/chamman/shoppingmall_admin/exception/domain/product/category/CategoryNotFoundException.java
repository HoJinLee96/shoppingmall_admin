package net.chamman.shoppingmall_admin.exception.domain.product.category;

import net.chamman.shoppingmall_admin.exception.CustomException;
import net.chamman.shoppingmall_admin.exception.HttpStatusCode;

public class CategoryNotFoundException extends CustomException{

	public CategoryNotFoundException(Exception e) {
		super(HttpStatusCode.PRODUCT_CATEGORY_NOT_FOUND, e);
	}

	public CategoryNotFoundException(String message, Exception e) {
		super(HttpStatusCode.PRODUCT_CATEGORY_NOT_FOUND, message, e);
	}

	public CategoryNotFoundException(String message) {
		super(HttpStatusCode.PRODUCT_CATEGORY_NOT_FOUND, message);
	}

	public CategoryNotFoundException() {
		super(HttpStatusCode.PRODUCT_CATEGORY_NOT_FOUND);
	}
	
}
