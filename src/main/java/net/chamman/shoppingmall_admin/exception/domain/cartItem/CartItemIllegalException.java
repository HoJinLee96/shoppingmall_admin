package net.chamman.shoppingmall_admin.exception.domain.cartItem;

import net.chamman.shoppingmall_admin.exception.CustomException;
import net.chamman.shoppingmall_admin.exception.HttpStatusCode;

public class CartItemIllegalException extends CustomException{

	public CartItemIllegalException(Exception e) {
		super(HttpStatusCode.CARTITEM_ILLEGAL, e);
	}

	public CartItemIllegalException(String message, Exception e) {
		super(HttpStatusCode.CARTITEM_ILLEGAL, message, e);
	}

	public CartItemIllegalException(String message) {
		super(HttpStatusCode.CARTITEM_ILLEGAL, message);
	}

	public CartItemIllegalException() {
		super(HttpStatusCode.CARTITEM_ILLEGAL);
	}
	
}
