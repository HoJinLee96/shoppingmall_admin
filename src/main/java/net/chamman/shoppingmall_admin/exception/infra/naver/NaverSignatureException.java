package net.chamman.shoppingmall_admin.exception.infra.naver;

import net.chamman.shoppingmall_admin.exception.CriticalException;

public class NaverSignatureException extends CriticalException{

	public NaverSignatureException() {
		super();
	}

	public NaverSignatureException(Exception e) {
		super(e);
	}

	public NaverSignatureException(String message, Exception e) {
		super(message, e);
	}

	public NaverSignatureException(String message) {
		super(message);
	}
	
}
