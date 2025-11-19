package net.chamman.shoppingmall_admin.domain.oauth;

import java.util.List;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import net.chamman.shoppingmall_admin.domain.member.dto.MemberStatusEvent;

@Service
@RequiredArgsConstructor
public class OauthService {

	private final OauthRepository oauthRepository;

	
}
