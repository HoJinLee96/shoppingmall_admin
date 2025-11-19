package net.chamman.shoppingmall_admin.domain.orderExchange;

import java.time.LocalDateTime;

import org.springframework.context.event.EventListener;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import net.chamman.shoppingmall_admin.domain.member.dto.MemberStatusEvent;

@Service
@RequiredArgsConstructor
public class OrderExchangeService {

	private final OrderExchangeRepository orderExchangeRepository;

//	@Transactional
//	@EventListener
//	public void handleMemberWithdrawal(MemberStatusEvent event) {
//		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//		String currentUsername = authentication != null ? authentication.getName() : "SYSTEM";
//		
//		orderExchangeRepository.softDeleteByMemberId(event.getMemberId(), LocalDateTime.now(), currentUsername);
//	}
}
