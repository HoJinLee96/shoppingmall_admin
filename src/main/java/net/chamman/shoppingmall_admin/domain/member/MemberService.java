package net.chamman.shoppingmall_admin.domain.member;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import net.chamman.shoppingmall_admin.domain.member.Member.MemberStatus;
import net.chamman.shoppingmall_admin.domain.member.dto.MemberStatusEvent;
import net.chamman.shoppingmall_admin.exception.domain.member.MemberIntegrityException;

@Service
@RequiredArgsConstructor
public class MemberService {

	private final MemberRepository memberRepository;
	private final ApplicationEventPublisher eventPublisher;
	
}
