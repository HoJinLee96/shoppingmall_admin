package net.chamman.shoppingmall_admin.exception.domain.member.status;

import net.chamman.shoppingmall_admin.exception.HttpStatusCode;

public class MemberStatusDeleteException extends MemberStatusException{

	public MemberStatusDeleteException(Exception e) {
		super(HttpStatusCode.MEMBER_STATUS_DELETE, e);
	}

	public MemberStatusDeleteException(String message, Exception e) {
		super(HttpStatusCode.MEMBER_STATUS_DELETE, message, e);
	}

	public MemberStatusDeleteException(String message) {
		super(HttpStatusCode.MEMBER_STATUS_DELETE, message);
	}

	public MemberStatusDeleteException() {
		super(HttpStatusCode.MEMBER_STATUS_DELETE);
	}

}
