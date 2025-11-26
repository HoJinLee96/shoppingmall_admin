package net.chamman.shoppingmall_admin.exception.domain.productVariant;

import net.chamman.shoppingmall_admin.exception.CustomException;
import net.chamman.shoppingmall_admin.exception.HttpStatusCode;

public class ProductVariantStockNotEnoughException extends CustomException {

	public ProductVariantStockNotEnoughException(Exception e) {
		super(HttpStatusCode.PRODUCT_VARIANT_STOCK_NOT_ENOUGH, e);
	}

	public ProductVariantStockNotEnoughException(String message, Exception e) {
		super(HttpStatusCode.PRODUCT_VARIANT_STOCK_NOT_ENOUGH, message, e);
	}

	public ProductVariantStockNotEnoughException(String message) {
		super(HttpStatusCode.PRODUCT_VARIANT_STOCK_NOT_ENOUGH, message);
	}

	public ProductVariantStockNotEnoughException() {
		super(HttpStatusCode.PRODUCT_VARIANT_STOCK_NOT_ENOUGH);
	}
}