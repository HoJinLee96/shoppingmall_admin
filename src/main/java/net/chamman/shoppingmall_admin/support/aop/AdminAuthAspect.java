package net.chamman.shoppingmall_admin.support.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import net.chamman.shoppingmall_admin.domain.admin.AdminQueryService;
import net.chamman.shoppingmall_admin.exception.support.aop.AopArgumentException;
import net.chamman.shoppingmall_admin.support.annotation.ActiveAdminOnly;

@Aspect
@Component
@RequiredArgsConstructor
public class AdminAuthAspect {
    
    private final AdminQueryService adminQueryService;

    // @AdminOnly 어노테이션이 붙은 메서드 실행 전에 이 코드를 실행
    // 매개변수 중 adminId 값을 통해 엔티티를 조회하여 해당 엔티티의 status를 검사 
    @Before("@annotation(activeAdminOnly)")
    public void verifyAdmin(JoinPoint joinPoint, ActiveAdminOnly activeAdminOnly) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();

        String[] parameterNames = signature.getParameterNames();
        
        // 메서드로 들어온 실제 값(인자)들을 가져옵니다. (예: [1, Pageable_객체])
        Object[] args = joinPoint.getArgs();
        
        for (int i = 0; i < parameterNames.length; i++) {
            if ("adminId".equals(parameterNames[i])) {
                Object arg = args[i];
            	if (arg == null) {
                    throw new AopArgumentException("AdminOnly Aspect: 파라미터 adminId의 값이 비어있습니다. JoinPoint: " + joinPoint.getSignature().toShortString());
               }
               if (arg instanceof Long longValue) {
                   if (longValue <= 0) {
                       throw new AopArgumentException("AdminOnly Aspect: 파라미터 adminId의 값이 0보다 작습닏. AdminId: " + longValue);
                   }
                   adminQueryService.getActiveAdminById(longValue);
               } else {
                    throw new AopArgumentException("AdminOnly Aspect: 파라미터 adminId의 타입이 Long이 아닙니다 (실제 타입: " + arg.getClass().getSimpleName() + "). JoinPoint: " + joinPoint.getSignature().toShortString());
               }
            }
        }
        throw new AopArgumentException("AdminOnly Aspect: 메서드 시그니처에서 adminId 파라미터를 찾을 수 없습니다. JoinPoint: " + joinPoint.getSignature().toShortString());
    }
}