package net.chamman.shoppingmall_admin.infra.rate;

public interface RateLimiter {
    boolean isAllowed(String key, int maxCount, int timeoutMinutes);
}
