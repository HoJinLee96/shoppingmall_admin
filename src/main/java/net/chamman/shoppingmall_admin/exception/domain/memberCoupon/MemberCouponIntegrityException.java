package net.chamman.shoppingmall_admin.exception.domain.memberCoupon;

import net.chamman.shoppingmall_admin.exception.IntegrityException;

public class MemberCouponIntegrityException extends IntegrityException{
	
	public MemberCouponIntegrityException(Exception e) {
		super(e);
	}

	public MemberCouponIntegrityException(String message, Exception e) {
		super(message, e);
	}

	public MemberCouponIntegrityException(String message) {
		super(message);
	}

	public MemberCouponIntegrityException() {
		super();
	}
	
}
