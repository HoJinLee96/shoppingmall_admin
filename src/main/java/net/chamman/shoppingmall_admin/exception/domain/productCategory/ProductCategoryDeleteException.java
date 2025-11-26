package net.chamman.shoppingmall_admin.exception.domain.productCategory;

import net.chamman.shoppingmall_admin.exception.CustomException;
import net.chamman.shoppingmall_admin.exception.HttpStatusCode;

public class ProductCategoryDeleteException extends CustomException{

	public ProductCategoryDeleteException(Exception e) {
		super(HttpStatusCode.PRODUCT_CATEGORY_DELETE_FAILED, e);
	}

	public ProductCategoryDeleteException(String message, Exception e) {
		super(HttpStatusCode.PRODUCT_CATEGORY_DELETE_FAILED, message, e);
	}

	public ProductCategoryDeleteException(String message) {
		super(HttpStatusCode.PRODUCT_CATEGORY_DELETE_FAILED, message);
	}

	public ProductCategoryDeleteException() {
		super(HttpStatusCode.PRODUCT_CATEGORY_DELETE_FAILED);
	}
	
}
