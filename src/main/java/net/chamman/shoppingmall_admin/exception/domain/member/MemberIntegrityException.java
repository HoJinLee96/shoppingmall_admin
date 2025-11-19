package net.chamman.shoppingmall_admin.exception.domain.member;

import net.chamman.shoppingmall_admin.exception.IntegrityException;

public class MemberIntegrityException extends IntegrityException{
	
	public MemberIntegrityException(Exception e) {
		super(e);
	}

	public MemberIntegrityException(String message, Exception e) {
		super(message, e);
	}

	public MemberIntegrityException(String message) {
		super(message);
	}

	public MemberIntegrityException() {
		super();
	}
	
}
