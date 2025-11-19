package net.chamman.shoppingmall_admin.exception.infra.file;

import net.chamman.shoppingmall_admin.exception.CriticalException;
import net.chamman.shoppingmall_admin.exception.CustomException;

public class FileUploadException extends CriticalException{

	public FileUploadException() {
		super();
	}

	public FileUploadException(Exception e) {
		super(e);
	}

	public FileUploadException(String message, Exception e) {
		super(message, e);
	}

	public FileUploadException(String message) {
		super(message);
	}
	
}
