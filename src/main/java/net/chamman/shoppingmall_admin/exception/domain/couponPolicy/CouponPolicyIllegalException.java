package net.chamman.shoppingmall_admin.exception.domain.couponPolicy;

import net.chamman.shoppingmall_admin.exception.CustomException;
import net.chamman.shoppingmall_admin.exception.HttpStatusCode;

public class CouponPolicyIllegalException extends CustomException{

	public CouponPolicyIllegalException(Exception e) {
		super(HttpStatusCode.COUPON_POLICY_ILLEGAL, e);
	}

	public CouponPolicyIllegalException(String message, Exception e) {
		super(HttpStatusCode.COUPON_POLICY_ILLEGAL, message, e);
	}

	public CouponPolicyIllegalException(String message) {
		super(HttpStatusCode.COUPON_POLICY_ILLEGAL, message);
	}

	public CouponPolicyIllegalException() {
		super(HttpStatusCode.COUPON_POLICY_ILLEGAL);
	}
	
}
