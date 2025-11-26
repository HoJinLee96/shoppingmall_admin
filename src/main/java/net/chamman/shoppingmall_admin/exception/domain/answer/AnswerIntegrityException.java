package net.chamman.shoppingmall_admin.exception.domain.answer;

import net.chamman.shoppingmall_admin.exception.IntegrityException;

public class AnswerIntegrityException extends IntegrityException{
	
	public AnswerIntegrityException(Exception e) {
		super(e);
	}

	public AnswerIntegrityException(String message, Exception e) {
		super(message, e);
	}

	public AnswerIntegrityException(String message) {
		super(message);
	}

	public AnswerIntegrityException() {
		super();
	}
	
}
