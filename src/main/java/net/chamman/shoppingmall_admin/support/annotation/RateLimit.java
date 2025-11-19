package net.chamman.shoppingmall_admin.support.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import net.chamman.shoppingmall_admin.infra.rate.RateLimitKey;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface RateLimit {
	RateLimitKey value() default RateLimitKey.REQUEST_CLIENT_IP;
    String parameterPath() default ""; 
}
