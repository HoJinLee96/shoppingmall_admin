package net.chamman.shoppingmall_admin.exception.domain.review;

import net.chamman.shoppingmall_admin.exception.CustomException;
import net.chamman.shoppingmall_admin.exception.HttpStatusCode;

public class ReviewIllegalException extends CustomException{

	public ReviewIllegalException(Exception e) {
		super(HttpStatusCode.REVIEW_ILLEGAL, e);
	}

	public ReviewIllegalException(String message, Exception e) {
		super(HttpStatusCode.REVIEW_ILLEGAL, message, e);
	}

	public ReviewIllegalException(String message) {
		super(HttpStatusCode.REVIEW_ILLEGAL, message);
	}

	public ReviewIllegalException() {
		super(HttpStatusCode.REVIEW_ILLEGAL);
	}
	
}
