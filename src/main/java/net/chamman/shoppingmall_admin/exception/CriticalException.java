package net.chamman.shoppingmall_admin.exception;

public abstract class CriticalException extends CustomException{

	protected CriticalException(Exception e) {
		super(HttpStatusCode.INTERNAL_SERVER_ERROR, e);
	}

	protected CriticalException(String message, Exception e) {
		super(HttpStatusCode.INTERNAL_SERVER_ERROR, message, e);
	}

	protected CriticalException(String message) {
		super(HttpStatusCode.INTERNAL_SERVER_ERROR, message);
	}

	protected CriticalException() {
		super(HttpStatusCode.INTERNAL_SERVER_ERROR);
	}

	
}
