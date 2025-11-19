package net.chamman.shoppingmall_admin.exception.redis;

import net.chamman.shoppingmall_admin.exception.CriticalException;

public class RedisSetException extends CriticalException{

	public RedisSetException(Exception e) {
		super(e);
	}

	public RedisSetException(String message, Exception e) {
		super(message, e);
	}

	public RedisSetException(String message) {
		super(message);
	}

	public RedisSetException() {
		super();
	}

}
