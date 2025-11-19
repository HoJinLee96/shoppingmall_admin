package net.chamman.shoppingmall_admin.domain.adminSignIn;

import java.util.Objects;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.chamman.shoppingmall_admin.domain.admin.Admin;
import net.chamman.shoppingmall_admin.domain.admin.AdminQueryService;
import net.chamman.shoppingmall_admin.domain.adminSignIn.dto.AdminSignInJwtDto;
import net.chamman.shoppingmall_admin.domain.adminSignIn.dto.AdminSignInRequestDto;
import net.chamman.shoppingmall_admin.domain.adminSignLog.AdminSignLog.SignResult;
import net.chamman.shoppingmall_admin.domain.adminSignLog.AdminSignLogService;
import net.chamman.shoppingmall_admin.exception.domain.admin.AdminIntegrityException;
import net.chamman.shoppingmall_admin.exception.domain.admin.AdminNotFoundException;
import net.chamman.shoppingmall_admin.exception.domain.admin.AdminPasswordMismatchException;
import net.chamman.shoppingmall_admin.exception.domain.admin.sign.SignInFailedException;
import net.chamman.shoppingmall_admin.exception.domain.admin.status.AdminStatusException;
import net.chamman.shoppingmall_admin.exception.domain.admin.status.AdminStatusLockedException;
import net.chamman.shoppingmall_admin.exception.domain.admin.status.AdminStatusStayException;
import net.chamman.shoppingmall_admin.exception.domain.admin.status.AdminStatusStopException;
import net.chamman.shoppingmall_admin.exception.domain.admin.status.AdminStatusUnverifiedException;
import net.chamman.shoppingmall_admin.exception.security.crypto.EncryptException;
import net.chamman.shoppingmall_admin.exception.security.jwt.JwtCreateException;
import net.chamman.shoppingmall_admin.exception.security.jwt.JwtExpiredException;
import net.chamman.shoppingmall_admin.exception.security.jwt.JwtIllegalException;
import net.chamman.shoppingmall_admin.exception.security.jwt.JwtParseException;
import net.chamman.shoppingmall_admin.exception.security.jwt.JwtRedisException;
import net.chamman.shoppingmall_admin.security.jwt.JwtService;
import net.chamman.shoppingmall_admin.security.jwt.JwtService.BlackListReason;
import net.chamman.shoppingmall_admin.security.jwt.JwtService.JwtType;
import net.chamman.shoppingmall_admin.support.util.LogMaskingUtil;
import net.chamman.shoppingmall_admin.support.util.LogMaskingUtil.MaskLevel;

@Slf4j
@RequiredArgsConstructor
@Service
public class AdminSignInService {
//
	private final AdminQueryService adminQueryService;
	private final AdminSignLogService adminSignLogService;
	private final AdminSignFailureManager adminSignFailureManager;
	private final PasswordEncoder passwordEncoder;
	private final JwtService jwtService;

	/**
	 * 관리자 로그인
	 * 
	 * @param signInRequestDto
	 * @param clientIp
	 * 
	 * @return
	 */
	@Transactional
	public AdminSignInJwtDto signInAndCreateJwt(AdminSignInRequestDto signInRequestDto, String clientIp) {

		String userName = signInRequestDto.userName();
		String password = signInRequestDto.password();

		try {
			// 유저 조회
			Admin admin = adminQueryService.getAdminByUserName(userName);

			// 비밀번호 검사 및 불일치시 실패 로그 기록
			validatePassword(admin, password, clientIp);
			
			admin.isActive();

			// 로그인 성공 로그 기록
			adminSignLogService.handleSuccessSignIn(admin, clientIp);

			// jwt 생성 및 반환
			return jwtService.createSignToken(admin);

		} catch (AdminNotFoundException | AdminPasswordMismatchException e) {
			throw new SignInFailedException(e);
		} catch (AdminStatusException e) {
			adminSignFailureManager.handleSignInFailureByAdminStatus(signInRequestDto.userName(), clientIp);
			throw e;
		}
	}

	/**
	 * 관리자 로그아웃
	 * 
	 * @param accessAdminId
	 * @param accessToken
	 * @param refreshToken
	 * @param clientIp
	 * 
	 * @throws AdminIntegrityException
	 * 
	 * @throws JwtParseException
	 * 
	 * @throws JwtExpiredException
	 * @throws JwtParseException
	 * @throws JwtIllegalException
	 * 
	 */
	@Transactional
	public void signOut(long accessAdminId, String accessToken, String refreshToken, String clientIp) {
		log.debug("* signOut 시작. accessAdminId: [{}], accessToken: [{}], refreshToken: [{}]",
				LogMaskingUtil.maskId(accessAdminId, MaskLevel.MEDIUM),
				LogMaskingUtil.maskToken(accessToken, MaskLevel.MEDIUM),
				LogMaskingUtil.maskToken(refreshToken, MaskLevel.MEDIUM));
		
		// 유저 조회
		Admin admin = adminQueryService.getAdminById(accessAdminId);
		
		// accessToken 블랙리스트 등록
		jwtService.setAccessTokenBlackList(accessToken, BlackListReason.SIGNOUT);
		
		// refreshToken 파싱 및 Redis내 RefreshToken 과 비교 검증
		long refreshAdminId = jwtService.validRefreshToken(refreshToken);
		
		// accessAdminId의 RT 삭제
		if (!(jwtService.deleteJwt(JwtType.SIGN_REFRESH, String.valueOf(accessAdminId)))) {
			log.warn(
					"* 로그아웃 중 Redis에서 RefreshToken 삭제 실패. 토큰 도용 의심 accessAdminId: [{}], accessToken: [{}], refreshToken: [{}], clientIp: [{}]",
					accessAdminId, accessToken, refreshToken, clientIp);
		}
		
		// accessAdminId와 refreshId 비교 검증
		if (!Objects.equals(accessAdminId, refreshAdminId)) {
			log.warn(
					"* 로그아웃 중 AccessToken과 RefreshToken의 AdminId 불일치. 토큰 도용 가능성 높음 accessAdminId: [{}], refreshAdminId: [{}], accessToken: [{}], refreshToken: [{}], clientIp: [{}]",
					accessAdminId, refreshAdminId, accessToken, refreshToken, clientIp);
			
            jwtService.deleteJwt(JwtType.SIGN_REFRESH, String.valueOf(refreshAdminId));
		}

		// 로그아웃 로그 기록
		adminSignLogService.signAdmin(admin, SignResult.SIGNOUT, clientIp);
	}

	/**
	 * AccessToken의 만료기한으로 인한 Refresh 경우
	 * 
	 * @param refreshToken
	 * @param clientIp
	 * 
	 * @return
	 * 
	 * {@link JwtService#isExistAccessTokenBlackList}
	 * @throws JwtIllegalException
	 * 
	 * {@link JwtService#validRefreshToken}
	 * @throws JwtExpiredException
	 * @throws JwtParseException
	 * @throws JwtIllegalException
	 * 
	 * {@link AdminQueryService#getActiveAdminById}
	 * @throws AdminIntegrityException
	 * @throws AdminStatusStayException
	 * @throws AdminStatusStopException
	 * @throws AdminStatusLockedException
	 * @throws AdminStatusUnverifiedException
	 * 
	 * {@link JwtService#createSignToken}
	 * @throws EncryptException
	 * @throws JwtCreateException
	 * @throws JwtRedisException
	 * 
	 * {@link JwtService#setAccessTokenBlackList}
	 * @throws JwtParseException
	 */
	@Transactional
	public AdminSignInJwtDto refresh(String accessToken, String refreshToken, String clientIp) {
		
		jwtService.isExistAccessTokenBlackList(accessToken);
		
		// 리프레쉬 토큰 복호화 및 Redis의 값과 일치 검증
		long adminId = jwtService.validRefreshToken(refreshToken);

		Admin admin = adminQueryService.getActiveAdminById(adminId);

		// jwt 생성
		AdminSignInJwtDto dto = jwtService.createSignToken(admin);

		// BlackList 기록
		jwtService.setAccessTokenBlackList(accessToken, BlackListReason.REFRSH);
		
		// Refresh 로그 기록
		adminSignLogService.signAdmin(admin, SignResult.REFRESH, clientIp);

		return dto;
	}

	/**
	 * 비밀번호 검증 및 결과 `SignInLog` Insert
	 * 
	 * @param admin
	 * @param reqPassword
	 * @param clientIp
	 * 
	 */
	private void validatePassword(Admin admin, String reqPassword, String clientIp) {
		if (!passwordEncoder.matches(reqPassword, admin.getPassword())) {
			adminSignFailureManager.handleSignInFailureByMismatchPassword(admin.getId(), clientIp);
			log.debug("* 비밀번호 불일치.");
			throw new AdminPasswordMismatchException("비밀번호 불일치.");
		}
	}

}
