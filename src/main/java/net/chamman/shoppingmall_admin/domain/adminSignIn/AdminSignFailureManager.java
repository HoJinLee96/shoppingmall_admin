package net.chamman.shoppingmall_admin.domain.adminSignIn;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.chamman.shoppingmall_admin.domain.admin.Admin;
import net.chamman.shoppingmall_admin.domain.admin.AdminRepository;
import net.chamman.shoppingmall_admin.domain.adminSignLog.AdminSignLog.SignResult;
import net.chamman.shoppingmall_admin.domain.adminSignLog.AdminSignLogService;
import net.chamman.shoppingmall_admin.exception.domain.admin.AdminIntegrityException;
import net.chamman.shoppingmall_admin.exception.domain.admin.AdminNotFoundException;
import net.chamman.shoppingmall_admin.exception.domain.admin.sign.SignInFailedOutException;
import net.chamman.shoppingmall_admin.exception.domain.admin.status.AdminStatusException;

@Service
@RequiredArgsConstructor
@Slf4j
public class AdminSignFailureManager {

	private final AdminSignLogService adminSignLogService;
	private final AdminRepository adminRepository;
	
	/**
	 * 1. 비밀번호 틀림으로 로그인 실패 로그 기록
	 * 2. 로그인 실패 로그 10회 이상시 계정 잠금 처리
	 * 
	 * @param admin
	 * @param clientIp
	 * 
	 * @throws AdminIntegrityException   {@link AdminSignInService#getActiveAdminById} 찾을 수 없는 유저
	 * @throws AdminStatusException   {@link AdminSignInService#getActiveAdminById} UserStatus 값이 Active 아닌 경우 
	 */
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void handleSignInFailureByMismatchPassword(long adminId, String clientIp) {
		
		Admin admin = adminRepository.findById(adminId)
				.orElseThrow(() -> new AdminIntegrityException());
		
		// 1. 실패 로그 기록
		adminSignLogService.signAdmin(admin, SignResult.MISMATCH_PASSWORD, clientIp);

		// 2. 계정 잠금 처리
		try {
			adminSignLogService.validSignFailCount(admin.getId() + "");
		} catch (SignInFailedOutException e) {
			admin.changeLockedStatus();
			log.info("* 비밀번호 불일치 10회로 계정 잠금: email={}, ip={}", admin.getEmail(), clientIp);
		}
	}
	
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void handleSignInFailureByAdminStatus(String userName, String clientIp) {
		
		Admin admin = adminRepository.findByUserName(userName)
				.orElseThrow(() -> new AdminNotFoundException("아이디에 일치하는 찾을 수 없는 관리자."));
		SignResult signResult = SignResult.valueOf("ACCOUNT_" + admin.getAdminStatus().name());
		
		adminSignLogService.signAdmin(admin, signResult, clientIp);
	}
	
}