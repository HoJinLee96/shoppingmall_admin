package net.chamman.shoppingmall_admin.domain.member.dto;

import net.chamman.shoppingmall_admin.domain.member.Member.MemberStatus;

public record MemberStatusEvent(
		
		Long id,
		MemberStatus memberStatus
		
		) {

}
