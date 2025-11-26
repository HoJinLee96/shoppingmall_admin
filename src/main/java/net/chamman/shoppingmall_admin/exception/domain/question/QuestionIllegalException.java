package net.chamman.shoppingmall_admin.exception.domain.question;

import net.chamman.shoppingmall_admin.exception.CustomException;
import net.chamman.shoppingmall_admin.exception.HttpStatusCode;

public class QuestionIllegalException extends CustomException{

	public QuestionIllegalException(Exception e) {
		super(HttpStatusCode.QUESTION_ILLEGAL, e);
	}

	public QuestionIllegalException(String message, Exception e) {
		super(HttpStatusCode.QUESTION_ILLEGAL, message, e);
	}

	public QuestionIllegalException(String message) {
		super(HttpStatusCode.QUESTION_ILLEGAL, message);
	}

	public QuestionIllegalException() {
		super(HttpStatusCode.QUESTION_ILLEGAL);
	}
	
}
