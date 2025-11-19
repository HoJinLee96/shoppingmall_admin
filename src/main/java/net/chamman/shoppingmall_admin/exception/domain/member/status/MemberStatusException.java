package net.chamman.shoppingmall_admin.exception.domain.member.status;

import net.chamman.shoppingmall_admin.exception.CustomException;
import net.chamman.shoppingmall_admin.exception.HttpStatusCode;

public class MemberStatusException extends CustomException {

	public MemberStatusException(HttpStatusCode httpStatusCode, Exception e) {
		super(httpStatusCode, e);
	}

	public MemberStatusException(HttpStatusCode httpStatusCode, String message, Exception e) {
		super(httpStatusCode, message, e);
	}

	public MemberStatusException(HttpStatusCode httpStatusCode, String message) {
		super(httpStatusCode, message);
	}

	public MemberStatusException(HttpStatusCode httpStatusCode) {
		super(httpStatusCode);
	}
	
}
