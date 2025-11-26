package net.chamman.shoppingmall_admin.exception.domain.answer;

import net.chamman.shoppingmall_admin.exception.CustomException;
import net.chamman.shoppingmall_admin.exception.HttpStatusCode;

public class AnswerIllegalException extends CustomException{

	public AnswerIllegalException(Exception e) {
		super(HttpStatusCode.ANSWER_ILLEGAL, e);
	}

	public AnswerIllegalException(String message, Exception e) {
		super(HttpStatusCode.ANSWER_ILLEGAL, message, e);
	}

	public AnswerIllegalException(String message) {
		super(HttpStatusCode.ANSWER_ILLEGAL, message);
	}

	public AnswerIllegalException() {
		super(HttpStatusCode.ANSWER_ILLEGAL);
	}
	
}
