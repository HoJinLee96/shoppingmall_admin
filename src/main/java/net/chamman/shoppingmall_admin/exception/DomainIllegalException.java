package net.chamman.shoppingmall_admin.exception;

public class DomainIllegalException extends CustomException {

	public DomainIllegalException(HttpStatusCode httpStatusCode, Exception e) {
		super(httpStatusCode, e);
	}

	public DomainIllegalException(HttpStatusCode httpStatusCode, String message, Exception e) {
		super(httpStatusCode, message, e);
	}

	public DomainIllegalException(HttpStatusCode httpStatusCode, String message) {
		super(httpStatusCode, message);
	}

	public DomainIllegalException(HttpStatusCode httpStatusCode) {
		super(httpStatusCode);
	}
	
}
