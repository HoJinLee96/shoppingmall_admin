package net.chamman.shoppingmall_admin.exception.domain.productCategory;

import net.chamman.shoppingmall_admin.exception.CustomException;
import net.chamman.shoppingmall_admin.exception.HttpStatusCode;

public class ProductCategoryIllegalException extends CustomException{

	public ProductCategoryIllegalException(Exception e) {
		super(HttpStatusCode.PRODUCT_CATEGORY_ILLEGAL, e);
	}

	public ProductCategoryIllegalException(String message, Exception e) {
		super(HttpStatusCode.PRODUCT_CATEGORY_ILLEGAL, message, e);
	}

	public ProductCategoryIllegalException(String message) {
		super(HttpStatusCode.PRODUCT_CATEGORY_ILLEGAL, message);
	}

	public ProductCategoryIllegalException() {
		super(HttpStatusCode.PRODUCT_CATEGORY_ILLEGAL);
	}

}
