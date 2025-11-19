package net.chamman.shoppingmall_admin.exception;

public class IntegrityException extends CriticalException{

	protected IntegrityException(Exception e) {
		super(e);
	}

	protected IntegrityException(String message, Exception e) {
		super(message, e);
	}

	protected IntegrityException(String message) {
		super(message);
	}

	protected IntegrityException() {
		super();
	}
	
	

}
