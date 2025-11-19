package net.chamman.shoppingmall_admin.exception.domain.member.status;

import net.chamman.shoppingmall_admin.exception.HttpStatusCode;

public class MemberStatusLockedException extends MemberStatusException{

	public MemberStatusLockedException(Exception e) {
		super(HttpStatusCode.MEMBER_STATUS_LOCKED, e);
	}

	public MemberStatusLockedException(String message, Exception e) {
		super(HttpStatusCode.MEMBER_STATUS_LOCKED, message, e);
	}

	public MemberStatusLockedException(String message) {
		super(HttpStatusCode.MEMBER_STATUS_LOCKED, message);
	}

	public MemberStatusLockedException() {
		super(HttpStatusCode.MEMBER_STATUS_LOCKED);
	}

}
