package net.chamman.shoppingmall_admin.exception.domain.member.status;

import net.chamman.shoppingmall_admin.exception.HttpStatusCode;

public class MemberStatusUnverifiedException extends MemberStatusException{

	public MemberStatusUnverifiedException(Exception e) {
		super(HttpStatusCode.MEMBER_STATUS_UNVERIFIED, e);
	}

	public MemberStatusUnverifiedException(String message, Exception e) {
		super(HttpStatusCode.MEMBER_STATUS_UNVERIFIED, message, e);
	}

	public MemberStatusUnverifiedException(String message) {
		super(HttpStatusCode.MEMBER_STATUS_UNVERIFIED, message);
	}

	public MemberStatusUnverifiedException() {
		super(HttpStatusCode.MEMBER_STATUS_UNVERIFIED);
	}
  
}
