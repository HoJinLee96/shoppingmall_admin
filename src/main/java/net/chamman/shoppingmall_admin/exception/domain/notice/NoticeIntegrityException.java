package net.chamman.shoppingmall_admin.exception.domain.notice;

import net.chamman.shoppingmall_admin.exception.IntegrityException;

public class NoticeIntegrityException extends IntegrityException{
	
	public NoticeIntegrityException(Exception e) {
		super(e);
	}

	public NoticeIntegrityException(String message, Exception e) {
		super(message, e);
	}

	public NoticeIntegrityException(String message) {
		super(message);
	}

	public NoticeIntegrityException() {
		super();
	}
	
}
