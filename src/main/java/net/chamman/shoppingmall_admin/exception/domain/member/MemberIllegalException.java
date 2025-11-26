package net.chamman.shoppingmall_admin.exception.domain.member;

import net.chamman.shoppingmall_admin.exception.CustomException;
import net.chamman.shoppingmall_admin.exception.HttpStatusCode;

public class MemberIllegalException extends CustomException{

	public MemberIllegalException(Exception e) {
		super(HttpStatusCode.MEMBER_ILLEGAL, e);
	}

	public MemberIllegalException(String message, Exception e) {
		super(HttpStatusCode.MEMBER_ILLEGAL, message, e);
	}

	public MemberIllegalException(String message) {
		super(HttpStatusCode.MEMBER_ILLEGAL, message);
	}

	public MemberIllegalException() {
		super(HttpStatusCode.MEMBER_ILLEGAL);
	}
	
}
