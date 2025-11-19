package net.chamman.shoppingmall_admin.exception.infra.file;

import net.chamman.shoppingmall_admin.exception.CriticalException;

public class FileDeleteException extends CriticalException{

	public FileDeleteException() {
		super();
	}

	public FileDeleteException(Exception e) {
		super(e);
	}

	public FileDeleteException(String message, Exception e) {
		super(message, e);
	}

	public FileDeleteException(String message) {
		super(message);
	}

}
