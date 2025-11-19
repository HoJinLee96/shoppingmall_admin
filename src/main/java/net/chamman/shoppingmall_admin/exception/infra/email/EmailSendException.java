package net.chamman.shoppingmall_admin.exception.infra.email;

import net.chamman.shoppingmall_admin.exception.CriticalException;

public class EmailSendException extends CriticalException{

	public EmailSendException(Exception e) {
		super(e);
	}

	public EmailSendException(String message, Exception e) {
		super(message, e);
	}

	public EmailSendException(String message) {
		super(message);
	}

	public EmailSendException() {
		super();
	}
	
}
