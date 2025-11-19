package net.chamman.shoppingmall_admin.infra.rate;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.chamman.shoppingmall_admin.exception.infra.rate.TooManyRequestException;
import net.chamman.shoppingmall_admin.support.util.LogMaskingUtil;
import net.chamman.shoppingmall_admin.support.util.LogMaskingUtil.MaskLevel;

@Component
@RequiredArgsConstructor
@Slf4j
public class RateLimitService {

    private final RateLimiter rateLimiter;

    /**
     * @param recipient
     * @param clientIp
     * 
	 *@throws TooManyRequestException {@link RateLimiter#isAllowed}
     */
    public void checkVerificationCodeRequest(String clientIp) {
    	log.debug("* 인증 코드 요청 횟수 체크 clientIp: [{}]", LogMaskingUtil.maskRecipient(clientIp, MaskLevel.MEDIUM));
    	
    	checkRequestClientIp(clientIp);
    	
    	rateLimiter.isAllowed(
            RateLimitKey.VERIFICATION_CODE_REQUEST.key(clientIp),
            RateLimitKey.VERIFICATION_CODE_REQUEST.getMaxRequest(),
            RateLimitKey.VERIFICATION_CODE_REQUEST.getTimeoutMinutes()
        );
    }

    /**
     * @param clientIp
     * 
	 *@throws TooManyRequestException {@link RateLimiter#isAllowed}
     */
    public void checkSignInRequest(String clientIp) {
    	log.debug("* 로그인 요청 횟수 체크 clientIp: [{}]",LogMaskingUtil.maskIp(clientIp, MaskLevel.MEDIUM));

    	checkRequestClientIp(clientIp);

    	rateLimiter.isAllowed(
            RateLimitKey.SIGN_IN_REQUEST.key(clientIp),
            RateLimitKey.SIGN_IN_REQUEST.getMaxRequest(),
            RateLimitKey.SIGN_IN_REQUEST.getTimeoutMinutes()
        );
    }
    
    /**
     * @param clientIp
     * 
	 *@throws TooManyRequestException {@link RateLimiter#isAllowed}
     */
    public void checkQuestionRegister(String clientIp) {
    	log.debug("* 질문 등록 요청 횟수 체크 clientIp: [{}]",LogMaskingUtil.maskIp(clientIp, MaskLevel.MEDIUM));
    
    	checkRequestClientIp(clientIp);

    	rateLimiter.isAllowed(
            RateLimitKey.QUESTION_REGISTER.key(clientIp),
            RateLimitKey.QUESTION_REGISTER.getMaxRequest(),
            RateLimitKey.QUESTION_REGISTER.getTimeoutMinutes()
        );
    }
    
    /**
     * @param clientIp
     * 
	 *@throws TooManyRequestException {@link RateLimiter#isAllowed}
     */
    public void checkRequestClientIp(String clientIp) {
    	log.debug("* 요청 횟수 체크 clientIp: [{}]",LogMaskingUtil.maskIp(clientIp, MaskLevel.MEDIUM));

    	rateLimiter.isAllowed(
            RateLimitKey.REQUEST_CLIENT_IP.key(clientIp),
            RateLimitKey.REQUEST_CLIENT_IP.getMaxRequest(),
            RateLimitKey.REQUEST_CLIENT_IP.getTimeoutMinutes()
        );
    }
}
