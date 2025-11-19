package net.chamman.shoppingmall_admin.infra.rate.impl;

import java.util.concurrent.TimeUnit;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.chamman.shoppingmall_admin.exception.infra.rate.TooManyRequestException;
import net.chamman.shoppingmall_admin.infra.rate.RateLimiter;
import net.chamman.shoppingmall_admin.support.context.CustomRequestContextHolder;

@Component
@RequiredArgsConstructor
@Slf4j
public class RedisRateLimiter implements RateLimiter {

	private final RedisTemplate<String, String> redisTemplate;

	/**
	 *@param key
	 *@param maxCount
	 *@param timeoutMinutes
	 *
	 *@throws TooManyRequestException {@link RateLimiter#isAllowed}
	 */
	@Override
	public boolean isAllowed(String key, int maxCount, int timeoutMinutes) {
		ValueOperations<String, String> ops = redisTemplate.opsForValue();
		Long reqCount = ops.increment(key, 1);
		log.debug("* Limit 조회. key: [{}], reqCount: [{}], maxCount: [{}], timeoutMinutes: [{}]", key, reqCount, maxCount, timeoutMinutes);

		if (reqCount == 1) {
			redisTemplate.expire(key, timeoutMinutes, TimeUnit.MINUTES);
		}

		if (reqCount > maxCount) {
			String clientIp = CustomRequestContextHolder.getClientIp();
			log.info("* TooManyRequestsException발생. clientIp: [{}]", clientIp);
			throw new TooManyRequestException("요청 횟수 초과.");
		}


		return true;
	}

}
