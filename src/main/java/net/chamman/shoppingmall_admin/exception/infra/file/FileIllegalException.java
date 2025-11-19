package net.chamman.shoppingmall_admin.exception.infra.file;

import net.chamman.shoppingmall_admin.exception.CustomException;
import net.chamman.shoppingmall_admin.exception.HttpStatusCode;

public class FileIllegalException extends CustomException{

	public FileIllegalException(Exception e) {
		super(HttpStatusCode.FILE_ILLEGAL, e);
	}

	public FileIllegalException(String message, Exception e) {
		super(HttpStatusCode.FILE_ILLEGAL, message, e);
	}

	public FileIllegalException(String message) {
		super(HttpStatusCode.FILE_ILLEGAL, message);
	}

	public FileIllegalException() {
		super(HttpStatusCode.FILE_ILLEGAL);
	}

}
