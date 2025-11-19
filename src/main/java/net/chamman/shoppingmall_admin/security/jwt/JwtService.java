package net.chamman.shoppingmall_admin.security.jwt;

import java.time.Duration;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import org.springframework.context.annotation.PropertySource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.chamman.shoppingmall_admin.domain.admin.Admin;
import net.chamman.shoppingmall_admin.domain.adminSignIn.dto.AdminSignInJwtDto;
import net.chamman.shoppingmall_admin.exception.security.crypto.EncryptException;
import net.chamman.shoppingmall_admin.exception.security.jwt.JwtCreateException;
import net.chamman.shoppingmall_admin.exception.security.jwt.JwtExpiredException;
import net.chamman.shoppingmall_admin.exception.security.jwt.JwtIllegalException;
import net.chamman.shoppingmall_admin.exception.security.jwt.JwtParseException;
import net.chamman.shoppingmall_admin.exception.security.jwt.JwtRedisException;
import net.chamman.shoppingmall_admin.security.crypto.AesService;
import net.chamman.shoppingmall_admin.security.jwt.dto.AdminAccessTokenClaimsDto;
import net.chamman.shoppingmall_admin.support.util.LogMaskingUtil;
import net.chamman.shoppingmall_admin.support.util.LogMaskingUtil.MaskLevel;

@Slf4j
@Service
@RequiredArgsConstructor
@PropertySource("classpath:application.properties")
public class JwtService {
	
	private final JwtProvider jwtProvider;
	private final AesService aesService;
	private final RedisTemplate<String, String> redisTemplate;
	
	@Getter
	@AllArgsConstructor
	public enum JwtType {
		
		SIGN_REFRESH("sign:refresh:", Duration.ofDays(14)), 
		SIGN_BLACKLIST("sign:blacklist:", null);
		
		private final String prefix;
		private final Duration ttl;
	}
	
	@Getter
	@AllArgsConstructor
	public enum BlackListReason{
		SIGNOUT("로그아웃"),
		REFRSH("리프레쉬"),
		UPDATE_ROLE("권한 수정"),
		WITHDRAWAL("탈퇴");
		
		private final String label;
	}
	
	/**
	 * @param adminId
	 * @param roles
	 * @param claims
	 * 
	 * @throws EncryptException
	 * @throws JwtCreateException
	 * @throws JwtRedisException
	 * 
	 * @return 액세스토큰, 리프레쉬토큰
	 */
	public AdminSignInJwtDto createSignToken(Admin admin) {
		log.debug("* createSignToken 시작.");
		
		String adminId = admin.getId()+"";
		String email = admin.getEmail();
		String name = admin.getName();
		List<String> roles = List.of("ROLE_ADMIN");
		AdminAccessTokenClaimsDto claimsDto = new AdminAccessTokenClaimsDto(adminId, email, name, roles);
		
		AdminAccessTokenClaimsDto encryptedClaimsDto = aesService.encrypt(claimsDto);
		
		String accessToken = jwtProvider.createAccessToken(encryptedClaimsDto);
		String refreshToken = jwtProvider.createRefreshToken(encryptedClaimsDto.getId());
		setRefreshJwt(admin.getId(), refreshToken);
		
		return new AdminSignInJwtDto(accessToken, refreshToken);
			
	}
	
	/** 액세스 토큰 검증
	 * @param token
	 * @return 복호화된 유저 정보 Claims
	 * 
	 * @throws JwtExpiredException
	 * @throws JwtParseException
	 * 
	 */
	public AdminAccessTokenClaimsDto parseAccessToken(String accessToken) {
		log.debug("* parseAccessToken 시작. AccessToken: [{}]", LogMaskingUtil.maskToken(accessToken, MaskLevel.NONE));
		
		try {
			AdminAccessTokenClaimsDto encryptedClaimsDto = jwtProvider.verifyAccessToken(accessToken);
			return aesService.decrypt(encryptedClaimsDto);
			
		} catch (JwtExpiredException | JwtParseException e) {
			throw e;
		} catch (Exception e) {
			throw new JwtParseException("parseAccessToken 중 익셉션 발생.", e);
		}

	}
	
	/**
	 * 액세스 토큰 블랙리스트 Redis Set
	 * 
	 * @param accessToken
	 * @param result
	 * 
	 * @throws JwtParseException
	 */
	public void setAccessTokenBlackList(String accessToken, BlackListReason reason) {
		log.debug("* setAccessTokenBlacklist 시작. accessToken: [{}], Result: [{}]",
				LogMaskingUtil.maskToken(accessToken, MaskLevel.MEDIUM), reason);
		try {
			long ttl = getAccessTokenRemainingTime(accessToken);
			redisTemplate.opsForValue().set(JwtType.SIGN_BLACKLIST.getPrefix() + accessToken, reason.name(),
					Duration.ofMillis(ttl));
		} catch (JwtExpiredException e) {
			log.info("* 이미 기한 만료된 AccessToken.");
		} catch (JwtParseException e) {
			log.warn("* AccessToken BlackList Set 중 JwtParse 실패. accessToken: [{}]", accessToken);
			throw e;
		}
	}

	/**
	 * @param accessToken
	 * @throws JwtIllegalException
	 */
	public void isExistAccessTokenBlackList(String accessToken) {
		log.debug("* validateBlackList 시작. Token: [{}]", LogMaskingUtil.maskToken(accessToken, MaskLevel.MEDIUM));

		String value = redisTemplate.opsForValue().get(JwtType.SIGN_BLACKLIST.getPrefix() + accessToken);
		BlackListReason reason = BlackListReason.valueOf(value);
		
//		 2-1. 로그아웃한 토큰
		if (BlackListReason.SIGNOUT.equals(reason)) {
			throw new JwtIllegalException("로그아웃 된 AccessToken.");

//		2-2. 유저 정보 업데이트된 토큰
		} else if (BlackListReason.UPDATE_ROLE.equals(reason)) {
			throw new JwtIllegalException("권한 업데이트 된 AccessToken.");
			
//		2-3. 리프레쉬 된 토큰
		} else if (BlackListReason.REFRSH.equals(reason)) {
			throw new JwtIllegalException("리프레쉬 된 AccessToken.");
		} else {
			log.error("* BlackList에 값이 있지만 일치하는 Reason이 없음.");
			throw new JwtIllegalException("BlackList에 값이 있지만 일치하는 Reason이 없음");
		}
	}
	
	/**
	 * 입력받은 RefreshToken과 해당 Id로부터 발급하고 저장해놓은 RefreshToken가 일치하는지 검사.
	 * 
	 * @param reqRefreshToken
	 * 
	 * @return AdminId
	 * 
	 * @throws JwtExpiredException
	 * @throws JwtParseException
	 * @throws JwtIllegalException
	 */
	public long validRefreshToken(String reqRefreshToken) {
		log.debug("* validRefreshToken 시작. RefreshToken: [{}]", LogMaskingUtil.maskToken(reqRefreshToken, MaskLevel.MEDIUM));

		String reqRefreshAdminId = jwtProvider.verifyRefreshToken(reqRefreshToken);

		String path = JwtType.SIGN_REFRESH.getPrefix() + reqRefreshAdminId;
		String redisRefreshToken = redisTemplate.opsForValue().get(path);
		if (!StringUtils.hasText(redisRefreshToken)) {
			log.info(
					"* 입력받은 RefreshToken의 id값을 key로 Redis에 일치하는 값이 없음. reqRefreshToken: [{}], refreshAdminId: [{}]", reqRefreshToken, reqRefreshAdminId);
			throw new JwtIllegalException("입력받은 RefreshToken의 id값을 key로 Redis의 일치하는 리프레쉬 토큰 없음.");
		}
		if (!Objects.equals(reqRefreshToken, redisRefreshToken)) {
			throw new JwtIllegalException("입력받은 reqRefreshToken과 id값을 key로 Redis에 조회한 값 redisRefreshToken이 불일치. reqRefreshToken: " + reqRefreshToken + ", refreshToken: " + reqRefreshToken);
		}
		return Long.parseLong(reqRefreshAdminId);
	}

	/**
	 * SignJwt Redis remove
	 * 
	 * @param type
	 * @param key
	 * 
	 * @return 삭제 여부
	 */
	public boolean deleteJwt(JwtType type, String key) {
		log.debug("* Redis에서 {} JwtType 삭제. JwtType: [{}]", type, LogMaskingUtil.maskToken(key, MaskLevel.MEDIUM));
	
		return redisTemplate.delete(type.getPrefix() + key);
	}

	/**
	 * 리프레쉬 토큰 Redis Set
	 * 
	 * @param adminId
	 * @param refreshToken
	 * 
	 * @throws JwtRedisException
	 */
	private void setRefreshJwt(Long adminId, String refreshToken) {
		log.debug("* RefreshToken Redis에 저장. RefreshToken: [{}]",
				LogMaskingUtil.maskToken(refreshToken, MaskLevel.MEDIUM));

		try {
			redisTemplate.opsForValue().set(JwtType.SIGN_REFRESH.getPrefix() + adminId, refreshToken,
					JwtType.SIGN_REFRESH.getTtl());
		} catch (Exception e) {
			throw new JwtRedisException("RefreshToken Redis 저장 중 오류. " + e.getMessage(), e);
		}
	}

	/** AccessToken 남은 시간 조회
	 * @param token
	 * @return 토큰 남은시간
	 *  
	 * @throws JwtExpiredException
	 * @throws JwtParseException
	 */
	private long getAccessTokenRemainingTime(String accessToken) {
		log.debug("* getAccessTokenRemainingTime 시작. Token: [{}]", LogMaskingUtil.maskToken(accessToken, MaskLevel.NONE));
	
		Date expiration = jwtProvider.getAccessTokenExpirationDate(accessToken);
		
		return expiration.getTime() - System.currentTimeMillis();
	}

	
}
