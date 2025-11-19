package net.chamman.shoppingmall_admin.exception.infra.map;

import net.chamman.shoppingmall_admin.exception.CriticalException;

public class MapSearchException extends CriticalException{

	public MapSearchException(Exception e) {
		super(e);
	}

	public MapSearchException(String message, Exception e) {
		super(message, e);
	}

	public MapSearchException(String message) {
		super(message);
	}

	public MapSearchException() {
		super();
	}

}
