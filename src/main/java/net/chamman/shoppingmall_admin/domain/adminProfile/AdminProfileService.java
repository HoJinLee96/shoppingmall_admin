package net.chamman.shoppingmall_admin.domain.adminProfile;

import java.util.Objects;

import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.chamman.shoppingmall_admin.domain.admin.Admin;
import net.chamman.shoppingmall_admin.domain.admin.AdminQueryService;
import net.chamman.shoppingmall_admin.domain.admin.AdminRepository;
import net.chamman.shoppingmall_admin.domain.adminSignLog.AdminSignLog.SignResult;
import net.chamman.shoppingmall_admin.domain.adminSignLog.AdminSignLogService;
import net.chamman.shoppingmall_admin.exception.security.token.TokenIllegalException;
import net.chamman.shoppingmall_admin.security.jwt.JwtService;
import net.chamman.shoppingmall_admin.security.jwt.JwtService.BlackListReason;
import net.chamman.shoppingmall_admin.security.jwt.JwtService.JwtType;
import net.chamman.shoppingmall_admin.security.token.TokenService;
import net.chamman.shoppingmall_admin.security.token.TokenService.TokenPurpose;
import net.chamman.shoppingmall_admin.security.token.dto.VerificationCodeSmsSuccessDto;
import net.chamman.shoppingmall_admin.support.util.LogMaskingUtil;
import net.chamman.shoppingmall_admin.support.util.LogMaskingUtil.MaskLevel;

@Slf4j
@RequiredArgsConstructor
@Service
public class AdminProfileService {

	private final AdminRepository adminRepository;
	private final AdminQueryService adminQueryService;
	private final AdminSignLogService adminSignLogService;
	private final JwtService jwtService;
	private final TokenService tokenService;

	/**
	 * 회원탈퇴 (유저 상태 DELETE로 변경)
	 * 
	 * @param adminId
	 * @param rawPassword
	 * @param accessToken
	 * @param refreshToken
	 * @param clientIp
	 */
	@Transactional
	public void softDeleteAdmin(long adminId, String smsToken, String accessToken, String refreshToken, String clientIp) {
		log.debug("* softDeleteAdmin 시작. AdminId: [{}]", LogMaskingUtil.maskId(adminId, MaskLevel.MEDIUM));

		// 유저 조회
		Admin admin = adminQueryService.getActiveAdminById(adminId);
		
		jwtService.validRefreshToken(refreshToken);
		
		VerificationCodeSmsSuccessDto smsTokenDto = tokenService.restoreTokenDto(VerificationCodeSmsSuccessDto.TOKENTYPE, smsToken);
		if(!TokenPurpose.ADMIN_WITHDRAWAL.equals(smsTokenDto.getTokenPurpose())) { 
			throw new TokenIllegalException("관리자 탈퇴 중 TokenPurpose 불일치. smsTokenDto.getTokenPurpose: "+smsTokenDto.getTokenPurpose());
		}
		String adminPhone = admin.getPhone();
		String smsTokenPhone = smsTokenDto.getPhone();
		if(!Objects.equals(adminPhone, smsTokenPhone)) {
			throw new TokenIllegalException("관리자 탈퇴 중 Token.phone과 Admin.phone 불일치. adminPhone: "+ adminPhone +", smsTokenPhone: "+smsTokenPhone);
		}
		
		// SoftDelete
		admin.softDelete();

		jwtService.setAccessTokenBlackList(accessToken, BlackListReason.WITHDRAWAL);
		jwtService.deleteJwt(JwtType.SIGN_REFRESH, String.valueOf(adminId));
		tokenService.deleteToken(VerificationCodeSmsSuccessDto.TOKENTYPE, smsToken);
		
		// 회원 탈퇴 로그 기록
		adminSignLogService.signAdmin(admin, SignResult.DELETE, clientIp);
	}

}
