package net.chamman.shoppingmall_admin.domain.address;

import java.util.List;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.chamman.shoppingmall_admin.domain.member.MemberRepository;
import net.chamman.shoppingmall_admin.domain.member.dto.MemberStatusEvent;
import net.chamman.shoppingmall_admin.infra.map.impl.DaumMapSearchImpl;
import net.chamman.shoppingmall_admin.security.obfuscation.impl.ObfuscatorImpl;

@Service
@Slf4j
@RequiredArgsConstructor
public class AddressService {

	private final AddressRepository addressRepository;
	private final MemberRepository membersRepository;
	private final DaumMapSearchImpl daumMapClient;
	private final ObfuscatorImpl obfuscator;
	
//	@Transactional
//	public AddressResponseDto addNewAddressToMember(long memberId, AddressRequestDto addressRequestDto) {
//		
//		daumMapClient.validateAddress(addressRequestDto.postcode(), addressRequestDto.mainAddress());
//		
//		Member member = getActiveMember(memberId);
//		
//		Address address = addressRequestDto.toEntity(member);
//		member.getAddresses().add(address);
//		
//		return AddressResponseDto.fromEntity(address, obfuscator);
//	}
	
	
}
