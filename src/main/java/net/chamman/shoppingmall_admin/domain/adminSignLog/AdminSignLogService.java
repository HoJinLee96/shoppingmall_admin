package net.chamman.shoppingmall_admin.domain.adminSignLog;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.chamman.shoppingmall_admin.domain.admin.Admin;
import net.chamman.shoppingmall_admin.domain.adminSignLog.AdminSignLog.SignResult;
import net.chamman.shoppingmall_admin.exception.domain.admin.sign.SignInFailedOutException;

@Slf4j
@RequiredArgsConstructor
@Service
public class AdminSignLogService {
	
	private final AdminSignLogRepository adminSignLogRepository;
	public static List<SignResult> failIncludedResults = List.of(SignResult.MISMATCH_PASSWORD);
	
	@Transactional
	public AdminSignLog registerSignLog(AdminSignLog signLog) {
		return adminSignLogRepository.save(signLog);
	}
	
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public AdminSignLog signAdmin(Admin admin, SignResult signResult, String clientIp) {
		return adminSignLogRepository.save(AdminSignLog.createAdminSignLog(admin, signResult, clientIp));
	}
	
	/** 로그인 실패 횟수 조회
	 * @param adminProvider
	 * @param email
	 * 
	 * @throws SignInFailedOutException {@link AdminSignLogService#validSignFailCount}
	 */
	public int validSignFailCount(String adminId) {
		
		int signFailCount = adminSignLogRepository.countUnresolvedWithResults(adminId, failIncludedResults);
		if (signFailCount >= 10) {
			throw new SignInFailedOutException("로그인 실패 10회");
		}
		return signFailCount;
	}
	
	/**
	 * @param admin
	 * @param clientIp
	 */
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void handleSuccessSignIn(Admin admin, String clientIp) {
		
		AdminSignLog adminSignLog = signAdmin(admin, SignResult.SIGNIN, clientIp);
		adminSignLogRepository.resolveUnresolvedLogs(admin.getId()+"", adminSignLog, AdminSignLogService.failIncludedResults);
		
	}
	
	/**
	 * @param admin
	 * @param clientIp
	 */
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void handleResolveByPasswordUpdate(Admin admin, String clientIp) {
	
		AdminSignLog adminSignLog = signAdmin(admin, SignResult.UPDATE_PASSWORD, clientIp);
		adminSignLogRepository.resolveUnresolvedLogs(admin.getId()+"", adminSignLog, AdminSignLogService.failIncludedResults);
		
	}
	
}
