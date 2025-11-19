package net.chamman.shoppingmall_admin.exception.support.aop;

import net.chamman.shoppingmall_admin.exception.CriticalException;

public class AopArgumentException extends CriticalException{

	public AopArgumentException() {
		super();
	}

	public AopArgumentException(Exception e) {
		super(e);
	}

	public AopArgumentException(String message, Exception e) {
		super(message, e);
	}

	public AopArgumentException(String message) {
		super(message);
	}

}
