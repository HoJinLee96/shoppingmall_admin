package net.chamman.shoppingmall_admin.exception.domain.member.status;

import net.chamman.shoppingmall_admin.exception.HttpStatusCode;

// DB Status == STAY
public class MemberStatusStayException extends MemberStatusException{

	public MemberStatusStayException(Exception e) {
		super(HttpStatusCode.MEMBER_STATUS_STAY, e);
	}

	public MemberStatusStayException(String message, Exception e) {
		super(HttpStatusCode.MEMBER_STATUS_STAY, message, e);
	}

	public MemberStatusStayException(String message) {
		super(HttpStatusCode.MEMBER_STATUS_STAY, message);
	}

	public MemberStatusStayException() {
		super(HttpStatusCode.MEMBER_STATUS_STAY);
	}
  
}
