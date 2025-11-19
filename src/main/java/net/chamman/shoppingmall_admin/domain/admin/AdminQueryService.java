package net.chamman.shoppingmall_admin.domain.admin;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.chamman.shoppingmall_admin.domain.adminSignIn.AdminSignInService;
import net.chamman.shoppingmall_admin.domain.adminSignUp.AdminSignUpService;
import net.chamman.shoppingmall_admin.exception.domain.admin.AdminEmailDuplicationException;
import net.chamman.shoppingmall_admin.exception.domain.admin.AdminIntegrityException;
import net.chamman.shoppingmall_admin.exception.domain.admin.AdminNotFoundException;
import net.chamman.shoppingmall_admin.exception.domain.admin.AdminPhoneDuplicationException;
import net.chamman.shoppingmall_admin.exception.domain.admin.status.AdminStatusDeleteException;
import net.chamman.shoppingmall_admin.exception.domain.admin.status.AdminStatusLockedException;
import net.chamman.shoppingmall_admin.exception.domain.admin.status.AdminStatusStayException;
import net.chamman.shoppingmall_admin.exception.domain.admin.status.AdminStatusStopException;
import net.chamman.shoppingmall_admin.exception.domain.admin.status.AdminStatusUnverifiedException;
import net.chamman.shoppingmall_admin.support.util.LogMaskingUtil;
import net.chamman.shoppingmall_admin.support.util.LogMaskingUtil.MaskLevel;

@Slf4j
@RequiredArgsConstructor
@Service
public class AdminQueryService {
	
	private final AdminRepository repository;
	
	/**
	 * @param adminId
	 * 
	 * @throws AdminIntegrityException {@link AdminSignUpService#getActiveAdminById} 찾을 수 없는 관리자
	 * @throws AdminStatusStayException {@link Admin#isActive} 일시 정지된 계정 
	 * @throws AdminStatusStopException {@link Admin#isActive} 정지된 계정 
	 * @throws AdminStatusDeleteException {@link Admin#isActive} 탈퇴한 계정 
	 * @throws AdminStatusLockedException {@link Admin#isActive} 잠긴 계정
	 * @throws AdminStatusUnverifiedException {@link Admin#isActive} 인증이 필요한 계정

	 * @return
	 */
	public Admin getActiveAdminById(long adminId) {
		Admin admin = repository.findById(adminId)
				.orElseThrow(() -> new AdminIntegrityException());
		admin.isActive();
		return admin;
	}
	
	/**
	 * @param adminId
	 * 
	 * @throws AdminIntegrityException

	 * @return
	 */
	public Admin getAdminById(long adminId) {
		Admin admin = repository.findById(adminId)
				.orElseThrow(() -> new AdminIntegrityException());
		return admin;
	}
	
	/**
	 * @param userName
	 * 
	 * @throws AdminNotFoundException {@link AdminSignInService#getActiveAdminByEmail} 찾을 수 없는 관리자
	 * 
	 * @return
	 */
	public Admin getAdminByUserName(String userName) {
		Admin admin = repository.findByUserName(userName)
				.orElseThrow(() -> new AdminNotFoundException("아이디에 일치하는 찾을 수 없는 관리자."));
		return admin;
	}
	
	/**
	 * @param email
	 * 
	 * @throws AdminNotFoundException {@link AdminSignInService#getActiveAdminByEmail} 찾을 수 없는 관리자
	 * @throws AdminStatusStayException {@link Admin#isActive} 일시 정지된 계정 
	 * @throws AdminStatusStopException {@link Admin#isActive} 정지된 계정 
	 * @throws AdminStatusDeleteException {@link Admin#isActive} 탈퇴한 계정 
	 * @throws AdminStatusLockedException {@link Admin#isActive} 잠긴 계정
	 * @throws AdminStatusUnverifiedException {@link Admin#isActive} 인증이 필요한 계정
	 * 
	 * @return
	 */
	public Admin getActiveAdminByEmail(String email) {
		Admin admin = repository.findByEmail(email)
				.orElseThrow(() -> new AdminNotFoundException("이메일에 일치하는 찾을 수 없는 관리자."));
		admin.isActive();
		return admin;
	}
	
	/**
	 * 이메일로 관리자 계정 중복 검사.
	 *
	 * @param email
	 * 
	 * @throws AdminEmailDuplicationException 해당 이메일로 가입된 계정이 있는 경우
	 * 
	 */
	@Transactional(readOnly = true)
	public void isAdminUserNameExist(String userName) {
		log.debug("* 아이디로 관리자를 조회하여 중복 검사. email: {}", LogMaskingUtil.maskUsername(userName, MaskLevel.MEDIUM));
		
		boolean isExist = repository.existsByUserName(userName);
		if (isExist) {
			throw new AdminEmailDuplicationException();
		}
	}
	
	/**
	 * 이메일로 관리자 계정 중복 검사.
	 *
	 * @param email
	 * 
	 * @throws AdminEmailDuplicationException 해당 이메일로 가입된 계정이 있는 경우
	 * 
	 */
	@Transactional(readOnly = true)
	public void isAdminEmailExist(String email) {
		log.debug("* 이메일로 관리자를 조회하여 중복 검사. email: {}", LogMaskingUtil.maskEmail(email, MaskLevel.MEDIUM));

		boolean isExist = repository.existsByEmail(email);
		if (isExist) {
			throw new AdminEmailDuplicationException();
		}
	}
	
	/**
	 * 휴대폰 번호로 관리자를 조회하여 중복 검사.
	 *
	 * @param phone 
	 * 
	 * @throws AdminPhoneDuplicationException 해당 휴대폰 번호로 가입된 계정이 있는 경우
	 */
	@Transactional(readOnly = true)
	public void isAdminPhoneExist(String phone) {
		log.debug("* isPhoneExist. Phone Number: {}", LogMaskingUtil.maskPhone(phone, MaskLevel.MEDIUM));

		boolean isExist = repository.existsByPhone(phone);
		if (isExist) {
			throw new AdminPhoneDuplicationException();
		}
	}

}
