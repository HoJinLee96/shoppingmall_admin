package net.chamman.shoppingmall_admin.exception.domain.admin.status;

import net.chamman.shoppingmall_admin.exception.HttpStatusCode;

// DB Status == STAY
public class AdminStatusStayException extends AdminStatusException{

	public AdminStatusStayException(Exception e) {
		super(HttpStatusCode.ADMIN_STATUS_STAY, e);
	}

	public AdminStatusStayException(String message, Exception e) {
		super(HttpStatusCode.ADMIN_STATUS_STAY, message, e);
	}

	public AdminStatusStayException(String message) {
		super(HttpStatusCode.ADMIN_STATUS_STAY, message);
	}

	public AdminStatusStayException() {
		super(HttpStatusCode.ADMIN_STATUS_STAY);
	}
  
}
