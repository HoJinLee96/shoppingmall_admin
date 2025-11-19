package net.chamman.shoppingmall_admin.infra.rate;

public enum RateLimitKey {
	
	VERIFICATION_CODE_REQUEST("rate_limit:verification_code_request:", 5, 10),
	QUESTION_REGISTER("rate_limit:question_register:", 5, 10),
	SIGN_IN_REQUEST("rate_limit:sign_in_request:", 10, 10),
	SIGN_OUT_REQUEST("rate_limit:sign_in_request:", 10, 10),
	REQUEST_CLIENT_IP("rate_limit:request_client_ip:", 30, 10);

	private final String prefix;
	private final int maxRequest;
	private final int timeoutMinutes;

	RateLimitKey(String prefix, int maxRequest, int timeoutMinutes) {
		this.prefix = prefix;
		this.maxRequest = maxRequest;
		this.timeoutMinutes = timeoutMinutes;
	}

	public String key(String id) {
		return prefix + id;
	}

	public int getMaxRequest() {
		return maxRequest;
	}

	public int getTimeoutMinutes() {
		return timeoutMinutes;
	}
}
