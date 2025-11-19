package net.chamman.shoppingmall_admin.domain.member;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.chamman.shoppingmall_admin.domain.member.dto.MemberStatusEvent;
import net.chamman.shoppingmall_admin.exception.domain.member.MemberIntegrityException;

@Slf4j
@Component
@RequiredArgsConstructor
public class MemberStatusListener {

	private final MemberRepository memberRepository;

	@Async // 1. 요청 스레드와 분리 (비동기)
	@TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT) // 2. 메인 DB 커밋 *성공* 시에만 실행
	@Transactional(propagation = Propagation.REQUIRES_NEW) // 3. 새 트랜잭션으로 실행 (실패해도 메인 롤백 안 함)
	public void handleMemberStatus(MemberStatusEvent event) {
		
		Member member = memberRepository.findById(event.id()).orElseThrow(()->new MemberIntegrityException());
		member.getAddresses().forEach(a->a.softDelete());

		event.id();
	}
	
}
