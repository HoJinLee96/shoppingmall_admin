package net.chamman.shoppingmall_admin.support;

import java.util.Optional;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import net.chamman.shoppingmall_admin.security.principal.AdminDetails;

@Component
@Configuration
public class AuditAwareImpl implements AuditorAware<String> {

    @Override
    public Optional<String> getCurrentAuditor() {
    	
        // 1. Spring Security 컨텍스트에서 인증 정보 가져오기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // 2. 인증 정보가 없거나, 인증되지 않았거나, 익명 사용자인지 확인
        if (authentication == null || !authentication.isAuthenticated() || "anonymousUser".equals(authentication.getPrincipal())) {
            // e.g., 배치 작업이나 시스템 내부 호출일 경우
            return Optional.of("SYSTEM"); 
        }

        // 3. 인증된 사용자 정보(Principal) 가져오기
        Object principal = authentication.getPrincipal();

        // 4. 우리가 사용하는 AdminDetails 타입이 맞는지 확인
        if (principal instanceof AdminDetails) {
            AdminDetails adminDetails = (AdminDetails) principal;
            // AdminDetails에서 userName (이메일) 반환
            return Optional.of(adminDetails.getUsername());
        }

        // AdminDetails가 아닌 다른 타입의 Principal일 경우 (e.g., 테스트)
        return Optional.of(principal.toString());
    }
}