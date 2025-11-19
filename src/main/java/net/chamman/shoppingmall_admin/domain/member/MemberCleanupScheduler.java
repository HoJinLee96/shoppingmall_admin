package net.chamman.shoppingmall_admin.domain.member;

import java.time.LocalDateTime;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class MemberCleanupScheduler {

    private final MemberRepository memberRepository;

    // 매일 자정에 실행 (cron = "초 분 시 일 월 요일")
    @Scheduled(cron = "0 0 0 * * ?")
    @Transactional
    public void cleanupDeletedMembers() {
    	
        // 30일 이전의 시간 계산
        LocalDateTime thirtyDaysAgo = LocalDateTime.now().minusDays(30);
        
        // 실제 DB에서 삭제
        int deletedCount = memberRepository.hardDeleteMembersByDeletedAtBefore(thirtyDaysAgo);
        
        log.info(deletedCount + "명의 회원 정보가 영구적으로 삭제되었습니다.");
        
    }
}