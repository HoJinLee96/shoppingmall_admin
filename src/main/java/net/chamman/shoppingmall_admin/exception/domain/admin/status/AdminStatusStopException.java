package net.chamman.shoppingmall_admin.exception.domain.admin.status;

import net.chamman.shoppingmall_admin.exception.HttpStatusCode;

public class AdminStatusStopException extends AdminStatusException{

	public AdminStatusStopException(Exception e) {
		super(HttpStatusCode.ADMIN_STATUS_STOP, e);
	}

	public AdminStatusStopException(String message, Exception e) {
		super(HttpStatusCode.ADMIN_STATUS_STOP, message, e);
	}

	public AdminStatusStopException(String message) {
		super(HttpStatusCode.ADMIN_STATUS_STOP, message);
	}

	public AdminStatusStopException() {
		super(HttpStatusCode.ADMIN_STATUS_STOP);
	}

}
