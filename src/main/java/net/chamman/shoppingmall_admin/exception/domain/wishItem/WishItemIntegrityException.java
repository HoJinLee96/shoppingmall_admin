package net.chamman.shoppingmall_admin.exception.domain.wishItem;

import net.chamman.shoppingmall_admin.exception.IntegrityException;

public class WishItemIntegrityException extends IntegrityException{
	
	public WishItemIntegrityException(Exception e) {
		super(e);
	}

	public WishItemIntegrityException(String message, Exception e) {
		super(message, e);
	}

	public WishItemIntegrityException(String message) {
		super(message);
	}

	public WishItemIntegrityException() {
		super();
	}
	
}
