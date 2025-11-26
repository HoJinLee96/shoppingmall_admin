package net.chamman.shoppingmall_admin.exception.domain.question;

import net.chamman.shoppingmall_admin.exception.IntegrityException;

public class QuestionIntegrityException extends IntegrityException{
	
	public QuestionIntegrityException(Exception e) {
		super(e);
	}

	public QuestionIntegrityException(String message, Exception e) {
		super(message, e);
	}

	public QuestionIntegrityException(String message) {
		super(message);
	}

	public QuestionIntegrityException() {
		super();
	}
	
}
