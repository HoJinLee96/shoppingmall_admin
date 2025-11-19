package net.chamman.shoppingmall_admin.exception.redis;

import net.chamman.shoppingmall_admin.exception.CriticalException;

public class RedisGetException extends CriticalException{

	public RedisGetException(Exception e) {
		super(e);
	}

	public RedisGetException(String message, Exception e) {
		super(message, e);
	}

	public RedisGetException(String message) {
		super(message);
	}

	public RedisGetException() {
		super();
	}
	
	

}
