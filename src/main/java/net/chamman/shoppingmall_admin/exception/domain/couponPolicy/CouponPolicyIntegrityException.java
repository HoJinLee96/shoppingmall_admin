package net.chamman.shoppingmall_admin.exception.domain.couponPolicy;

import net.chamman.shoppingmall_admin.exception.IntegrityException;

public class CouponPolicyIntegrityException extends IntegrityException{
	
	public CouponPolicyIntegrityException(Exception e) {
		super(e);
	}

	public CouponPolicyIntegrityException(String message, Exception e) {
		super(message, e);
	}

	public CouponPolicyIntegrityException(String message) {
		super(message);
	}

	public CouponPolicyIntegrityException() {
		super();
	}
	
}
