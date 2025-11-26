package net.chamman.shoppingmall_admin.exception.domain.wishItem;

import net.chamman.shoppingmall_admin.exception.CustomException;
import net.chamman.shoppingmall_admin.exception.HttpStatusCode;

public class WishItemIllegalException extends CustomException{

	public WishItemIllegalException(Exception e) {
		super(HttpStatusCode.WISH_ITEM_ILLEGAL, e);
	}

	public WishItemIllegalException(String message, Exception e) {
		super(HttpStatusCode.WISH_ITEM_ILLEGAL, message, e);
	}

	public WishItemIllegalException(String message) {
		super(HttpStatusCode.WISH_ITEM_ILLEGAL, message);
	}

	public WishItemIllegalException() {
		super(HttpStatusCode.WISH_ITEM_ILLEGAL);
	}
	
}
