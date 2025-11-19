package net.chamman.shoppingmall_admin.exception.infra.sms;

import net.chamman.shoppingmall_admin.exception.CriticalException;

public class SmsSendException extends CriticalException{

	public SmsSendException(Exception e) {
		super(e);
	}

	public SmsSendException(String message, Exception e) {
		super(message, e);
	}

	public SmsSendException(String message) {
		super(message);
	}

	public SmsSendException() {
		super();
	}

}
