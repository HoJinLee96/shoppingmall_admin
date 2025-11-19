package net.chamman.shoppingmall_admin.exception.domain.member.status;

import net.chamman.shoppingmall_admin.exception.HttpStatusCode;

public class MemberStatusStopException extends MemberStatusException{

	public MemberStatusStopException(Exception e) {
		super(HttpStatusCode.MEMBER_STATUS_STOP, e);
	}

	public MemberStatusStopException(String message, Exception e) {
		super(HttpStatusCode.MEMBER_STATUS_STOP, message, e);
	}

	public MemberStatusStopException(String message) {
		super(HttpStatusCode.MEMBER_STATUS_STOP, message);
	}

	public MemberStatusStopException() {
		super(HttpStatusCode.MEMBER_STATUS_STOP);
	}

}
