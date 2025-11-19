package net.chamman.shoppingmall_admin.exception.domain.admin.status;

import net.chamman.shoppingmall_admin.exception.HttpStatusCode;

public class AdminStatusLockedException extends AdminStatusException{

	public AdminStatusLockedException(Exception e) {
		super(HttpStatusCode.ADMIN_STATUS_LOCKED, e);
	}

	public AdminStatusLockedException(String message, Exception e) {
		super(HttpStatusCode.ADMIN_STATUS_LOCKED, message, e);
	}

	public AdminStatusLockedException(String message) {
		super(HttpStatusCode.ADMIN_STATUS_LOCKED, message);
	}

	public AdminStatusLockedException() {
		super(HttpStatusCode.ADMIN_STATUS_LOCKED);
	}

}
