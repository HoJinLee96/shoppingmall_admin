package net.chamman.shoppingmall_admin.exception.domain.cartItem;

import net.chamman.shoppingmall_admin.exception.IntegrityException;

public class CartItemIntegrityException extends IntegrityException{
	
	public CartItemIntegrityException(Exception e) {
		super(e);
	}

	public CartItemIntegrityException(String message, Exception e) {
		super(message, e);
	}

	public CartItemIntegrityException(String message) {
		super(message);
	}

	public CartItemIntegrityException() {
		super();
	}
	
}
