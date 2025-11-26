package net.chamman.shoppingmall_admin.exception.domain.memberCoupon;

import net.chamman.shoppingmall_admin.exception.CustomException;
import net.chamman.shoppingmall_admin.exception.HttpStatusCode;

public class MemberCouponIllegalException extends CustomException{

	public MemberCouponIllegalException(Exception e) {
		super(HttpStatusCode.MEMBER_COUPON_ILLEGAL, e);
	}

	public MemberCouponIllegalException(String message, Exception e) {
		super(HttpStatusCode.MEMBER_COUPON_ILLEGAL, message, e);
	}

	public MemberCouponIllegalException(String message) {
		super(HttpStatusCode.MEMBER_COUPON_ILLEGAL, message);
	}

	public MemberCouponIllegalException() {
		super(HttpStatusCode.MEMBER_COUPON_ILLEGAL);
	}
	
}
