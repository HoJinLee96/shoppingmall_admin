package net.chamman.shoppingmall_admin.exception.domain.admin.status;

import net.chamman.shoppingmall_admin.exception.HttpStatusCode;

public class AdminStatusUnverifiedException extends AdminStatusException{

	public AdminStatusUnverifiedException(Exception e) {
		super(HttpStatusCode.ADMIN_STATUS_UNVERIFIED, e);
	}

	public AdminStatusUnverifiedException(String message, Exception e) {
		super(HttpStatusCode.ADMIN_STATUS_UNVERIFIED, message, e);
	}

	public AdminStatusUnverifiedException(String message) {
		super(HttpStatusCode.ADMIN_STATUS_UNVERIFIED, message);
	}

	public AdminStatusUnverifiedException() {
		super(HttpStatusCode.ADMIN_STATUS_UNVERIFIED);
	}
  
}
