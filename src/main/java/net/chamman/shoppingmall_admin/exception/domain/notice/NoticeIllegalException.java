package net.chamman.shoppingmall_admin.exception.domain.notice;

import net.chamman.shoppingmall_admin.exception.CustomException;
import net.chamman.shoppingmall_admin.exception.HttpStatusCode;

public class NoticeIllegalException extends CustomException{

	public NoticeIllegalException(Exception e) {
		super(HttpStatusCode.NOTICE_ILLEGAL, e);
	}

	public NoticeIllegalException(String message, Exception e) {
		super(HttpStatusCode.NOTICE_ILLEGAL, message, e);
	}

	public NoticeIllegalException(String message) {
		super(HttpStatusCode.NOTICE_ILLEGAL, message);
	}

	public NoticeIllegalException() {
		super(HttpStatusCode.NOTICE_ILLEGAL);
	}
	
}
