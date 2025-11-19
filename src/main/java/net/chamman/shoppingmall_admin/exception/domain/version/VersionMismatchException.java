package net.chamman.shoppingmall_admin.exception.domain.version;

import net.chamman.shoppingmall_admin.exception.CustomException;
import net.chamman.shoppingmall_admin.exception.HttpStatusCode;

public class VersionMismatchException extends CustomException {

	public VersionMismatchException(Exception e) {
		super(HttpStatusCode.VERSION_MISMATCH, e);
	}

	public VersionMismatchException(String message, Exception e) {
		super(HttpStatusCode.VERSION_MISMATCH, message, e);
	}

	public VersionMismatchException(String message) {
		super(HttpStatusCode.VERSION_MISMATCH, message);
	}

	public VersionMismatchException() {
		super(HttpStatusCode.VERSION_MISMATCH);
	}
}