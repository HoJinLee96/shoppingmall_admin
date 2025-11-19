package net.chamman.shoppingmall_admin.exception.domain.admin.status;

import net.chamman.shoppingmall_admin.exception.HttpStatusCode;

public class AdminStatusDeleteException extends AdminStatusException{

	public AdminStatusDeleteException(Exception e) {
		super(HttpStatusCode.ADMIN_STATUS_DELETE, e);
	}

	public AdminStatusDeleteException(String message, Exception e) {
		super(HttpStatusCode.ADMIN_STATUS_DELETE, message, e);
	}

	public AdminStatusDeleteException(String message) {
		super(HttpStatusCode.ADMIN_STATUS_DELETE, message);
	}

	public AdminStatusDeleteException() {
		super(HttpStatusCode.ADMIN_STATUS_DELETE);
	}

}
