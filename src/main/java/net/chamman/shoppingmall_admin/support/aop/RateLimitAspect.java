package net.chamman.shoppingmall_admin.support.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.chamman.shoppingmall_admin.exception.support.aop.AopArgumentException;
import net.chamman.shoppingmall_admin.infra.rate.RateLimitKey;
import net.chamman.shoppingmall_admin.infra.rate.RateLimitService;
import net.chamman.shoppingmall_admin.support.annotation.RateLimit;
import net.chamman.shoppingmall_admin.support.context.CustomRequestContextHolder;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class RateLimitAspect {

	private final RateLimitService rateLimitService;

    @Before("@annotation(rateLimit)")
    public void checkRateLimit(JoinPoint joinPoint, RateLimit rateLimit) {
    	
        String clientIp = CustomRequestContextHolder.getClientIp();
        
        RateLimitKey rateLimitKey = rateLimit.value();
        if (rateLimitKey.equals(RateLimitKey.REQUEST_CLIENT_IP)) {
        	rateLimitService.checkRequestClientIp(clientIp);
        } else if (rateLimitKey.equals(RateLimitKey.QUESTION_REGISTER)) {
        	rateLimitService.checkQuestionRegister(clientIp);
        } else if (rateLimitKey.equals(RateLimitKey.SIGN_IN_REQUEST)) {
        	rateLimitService.checkSignInRequest(clientIp);
        } else if (rateLimitKey.equals(RateLimitKey.VERIFICATION_CODE_REQUEST)) {
        	rateLimitService.checkVerificationCodeRequest(clientIp);
        }

    }

    private String getParameterValue(JoinPoint joinPoint, String parameterName) {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        String[] parameterNames = methodSignature.getParameterNames();
        Object[] args = joinPoint.getArgs();

        if (parameterNames == null || args == null || parameterNames.length != args.length) {
            throw new AopArgumentException("메서드의 파라미터 정보나 인자 값을 가져올 수 없습니다. JoinPoint: " + joinPoint.getSignature().toShortString());
        }

        for (int i = 0; i < parameterNames.length; i++) {
            if (parameterNames[i].equals(parameterName)) {
                Object arg = args[i];
                if (arg == null) {
                     throw new AopArgumentException("RateLimit Aspect: 파라미터 '" + parameterName + "'의 값이 null입니다. JoinPoint: " + joinPoint.getSignature().toShortString());
                }
                if (arg instanceof String stringValue) {
                    if (!StringUtils.hasText(stringValue)) {
                        throw new AopArgumentException("RateLimit Aspect: 파라미터 '" + parameterName + "'의 값이 비어있습니다. JoinPoint: " + joinPoint.getSignature().toShortString());
                    }
                    return stringValue;
                } else {
                     throw new AopArgumentException("RateLimit Aspect: 파라미터 '" + parameterName + "'의 타입이 String이 아닙니다 (실제 타입: " + arg.getClass().getSimpleName() + "). JoinPoint: " + joinPoint.getSignature().toShortString());
                }
            }
        }

        throw new AopArgumentException("RateLimit Aspect: 메서드 시그니처에서 '" + parameterName + "' 파라미터를 찾을 수 없습니다. JoinPoint: " + joinPoint.getSignature().toShortString());
    }
}