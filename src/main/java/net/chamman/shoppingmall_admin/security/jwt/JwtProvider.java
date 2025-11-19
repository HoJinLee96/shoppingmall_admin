package net.chamman.shoppingmall_admin.security.jwt;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import net.chamman.shoppingmall_admin.exception.security.jwt.JwtCreateException;
import net.chamman.shoppingmall_admin.exception.security.jwt.JwtExpiredException;
import net.chamman.shoppingmall_admin.exception.security.jwt.JwtParseException;
import net.chamman.shoppingmall_admin.security.jwt.dto.AdminAccessTokenClaimsDto;
import net.chamman.shoppingmall_admin.support.util.LogMaskingUtil;
import net.chamman.shoppingmall_admin.support.util.LogMaskingUtil.MaskLevel;

@Component
@Slf4j
@PropertySource("classpath:application.properties")
public class JwtProvider {
	
	private final Key signAccessHmacShaKey;
	private final Key signRefreshHmacShaKey;
	
	private final long expiration14Days = 1000 * 60 * 60 * 24 * 14; // 14일
	private final long expiration1Hour = 1000 * 60 * 60; // 1시간
	
	public JwtProvider(
			@Value("${jwt.sign.access.secretKey}") String signAccessSecretKey,
			@Value("${jwt.sign.refresh.secretKey}") String signRefreshSecretKey
			) {
		this.signAccessHmacShaKey = Keys.hmacShaKeyFor(signAccessSecretKey.getBytes(StandardCharsets.UTF_8));
		this.signRefreshHmacShaKey = Keys.hmacShaKeyFor(signRefreshSecretKey.getBytes(StandardCharsets.UTF_8));
	}
	
	/** 액세스 토큰 생성
	 * @param adminId
	 * @param roles
	 * @param claims
	 * @return 액세스 토큰
	 * 
	 * @throws JwtCreateException
	 */
	protected String createAccessToken(AdminAccessTokenClaimsDto encryptedClaimsDto) {
		log.debug("* createAccessToken 시작. claimsDto: [{}]", encryptedClaimsDto);
		
		try {
			return Jwts.builder()
					.setSubject(encryptedClaimsDto.getId())
					.setIssuedAt(new Date())
					.setExpiration(new Date(System.currentTimeMillis() + expiration1Hour))
					.signWith(signAccessHmacShaKey, SignatureAlgorithm.HS256)
					.claim("userName",encryptedClaimsDto.getUserName())
					.claim("name",encryptedClaimsDto.getName())
					.claim("roles",encryptedClaimsDto.getRoles())
					.compact();
			
		} catch (Exception e) {
			log.error("* createAccessToken 중 익셉션 발생.", e);
			throw new JwtCreateException("createAccessToken 중 익셉션 발생. "+e.getMessage(), e);
		}
	}
	
	/** 리프레쉬 토큰 생성
	 * @param adminId
	 * @return 리프레쉬 토큰
	 * 
	 * @throws JwtCreateException
	 */
	protected String createRefreshToken(String encryptedAdminId) {
		log.debug("* createRefreshToken 시작. adminId: [{}]", LogMaskingUtil.maskId(encryptedAdminId, MaskLevel.NONE));
		
		try {
			return Jwts.builder()
					.setSubject(encryptedAdminId)
					.setExpiration(new Date(System.currentTimeMillis() + expiration14Days))
					.signWith(signRefreshHmacShaKey, SignatureAlgorithm.HS256)
					.compact();
		} catch (Exception e) {
			log.error("* createRefreshToken 중 익셉션 발생.", e);
			throw new JwtCreateException("createRefreshToken 중 익셉션 발생. "+e.getMessage(),e);
		}
	}
	
	/**
	 * @param accessToken
	 * @return
	 * 
	 * @throws JwtExpiredException
	 * @throws JwtParseException
	 */
	protected Date getAccessTokenExpirationDate(String accessToken) {
		Claims claims = parseAccessToken(accessToken);
		validateAccessTokenClaims(claims);
		return claims.getExpiration();
	}
	
	/** 액세스 토큰 검증
	 * @param token
	 * @return 복호화된 유저 정보 Claims
	 * 
	 * @throws JwtExpiredException
	 * @throws JwtParseException
	 * 
	 */
	protected AdminAccessTokenClaimsDto verifyAccessToken(String accessToken) {
		log.debug("* parseAccessToken 시작. AccessToken: [{}]", LogMaskingUtil.maskToken(accessToken, MaskLevel.NONE));
		
		Claims claims = parseAccessToken(accessToken);
		
		validateAccessTokenClaims(claims);

		return convertClaimsToAccessTokenDto(claims);
	}
	
	/** 리프레쉬 토큰 검증
	 * @param token
	 * @return adminId
	 * 
	 * @throws JwtExpiredException
	 * @throws JwtParseException
	 */
	protected String verifyRefreshToken(String refreshToken) {
		log.debug("* parseRefreshToken 시작. RefreshToken: [{}]", LogMaskingUtil.maskToken(refreshToken, MaskLevel.NONE));

			Claims claims = parseRefreshToken(refreshToken);
			
			validateAdminId(claims);
			
			return claims.getSubject(); 
	}
	
	/**
	 * @param accessToken
	 * @return
	 * 
	 * @throws JwtExpiredException
	 * @throws JwtParseException
	 */
	private Claims parseAccessToken(String accessToken) {
		try {
			return Jwts.parserBuilder()
					.setSigningKey(signAccessHmacShaKey)
					.build()
					.parseClaimsJws(accessToken)
					.getBody();
		} catch (ExpiredJwtException e) {
			throw new JwtExpiredException("AccessToken 시간 만료.", e);
		} catch (Exception e) {
			log.error("* parseAccessToken 중 익셉션 발생.", e);
			throw new JwtParseException("parseAccessToken 중 익셉션 발생. "+e.getMessage(), e);
		}
	}
	
	private Claims parseRefreshToken(String refreshToken) {
		try {
			return Jwts.parserBuilder()
					.setSigningKey(signRefreshHmacShaKey)
					.build()
					.parseClaimsJws(refreshToken)
					.getBody();
		} catch (ExpiredJwtException e) {
			throw new JwtExpiredException("RefreshToken 시간 만료. "+e.getMessage(), e);
		} catch (Exception e) {
			log.error("* parseRefreshToken 중 익셉션 발생.", e);
			throw new JwtParseException("parseRefreshToken 중 익셉션 발생. "+e.getMessage(), e);
		}
	}
	
	/** Claims -> AdminAccessTokenClaimsDto로 추출
	 * @param claims
	 * @return AdminAccessTokenClaimsDto
	 * 
	 */
	@SuppressWarnings("unchecked")
	private AdminAccessTokenClaimsDto convertClaimsToAccessTokenDto(Claims claims) {
		try {
			return new AdminAccessTokenClaimsDto(
					claims.getSubject(),
					(String)claims.get("userName"),
					(String)claims.get("name"),
					(List<String>)claims.get("roles"));
		} catch (Exception e) {
			log.error("* convertClaimsToAccessTokenDto 중 익셉션 발생.", e);
			throw new JwtParseException("convertClaimsToAccessTokenDto 중 익셉션 발생. "+e.getMessage(), e);
		}
	}
	
	/**
	 * 액세스 토큰의 클레임을 검증합니다.
	 * 각 검증 로jjsdl을 별도 메서드로 분리하여 가독성을 높였습니다.
	 *
	 * @param claims 검증할 JWT Claims
	 * @throws JwtParseException
	 */
	private void validateAccessTokenClaims(Claims claims) {
	    validateAdminId(claims);
	    validateRoles(claims);
	    validateRequiredStringClaim(claims, "userName");
	    validateRequiredStringClaim(claims, "email");
	    validateRequiredStringClaim(claims, "name");
	}

	/**
	 * 'subject' 클레임 (adminId)을 검증합니다.
	 * (사용자 정의에 따라 'subject'를 adminId로 사용)
	 */
	private void validateAdminId(Claims claims) {
	    String subject = claims.get("subject",String.class);
	    if (!StringUtils.hasText(subject)) {
			log.error("* AccessToken을 파싱한 Calims의 Subject값(adminId)이 null 또는 비어있습니다.");
	        throw new JwtParseException("AccessToken을 파싱한 Calims의 Subject값(adminId)이 null 또는 비어있습니다.");
	    }

	    try {
	        long adminId = Long.parseLong(subject);
	        if (adminId <= 0) {
				log.error("* AccessToken을 파싱한 Calims의 Subject값(adminId)이 양수가 아닙니다.");
	            throw new JwtParseException("AccessToken을 파싱한 Calims의 Subject값(adminId)이 양수가 아닙니다.");
	        }
	    } catch (NumberFormatException e) {
			log.error("* AccessToken을 파싱한 Calims의 Subject값(adminId)이 Number 타입이 아닙니다.");
	        throw new JwtParseException("AccessToken을 파싱한 Calims의 Subject값(adminId)이 Number 타입이 아닙니다.", e);
	    }
	}

	/**
	 * 'roles' 클레임이 유효한 'List<String>' 형태인지 검증합니다.
	 */
	private void validateRoles(Claims claims) {
	    Object rolesObj = claims.get("roles");
	    if (!(rolesObj instanceof List<?> roles)) {
			log.error("* AccessToken을 파싱한 Calims의 roles값이 List 타입이 아닙니다.");
	        throw new JwtParseException("AccessToken을 파싱한 Calims의 roles값이 List 타입이 아닙니다.");
	    }
	    
	    boolean allStrings = roles.stream()
	            .allMatch(o -> (o instanceof String s) && StringUtils.hasText(s));
	    
	    if (!allStrings) {
			log.error("* AccessToken을 파싱한 Calims의 roles 내부값이 String이 아니거나 비어있습니다.");
	        throw new JwtParseException("AccessToken을 파싱한 Calims의 roles 내부값이 String이 아니거나 비어있습니다.");
	    }
	}

	/**
	 * 필수 문자열 클레임이 null이거나 비어있지 않은지 검증합니다.
	 *
	 * @param claims    JWT Claims
	 * @param claimName 검증할 클레임 이름 (예: "email", "name")
	 */
	private void validateRequiredStringClaim(Claims claims, String claimName) {
	    String value = claims.get(claimName, String.class);
	    
	    if (!StringUtils.hasText(value)) {
			log.error("* AccessToken을 파싱한 Calims의 {} 내부값이 null이거나 비어있습니다.", claimName);
	        throw new JwtParseException("AccessToken '" + claimName + "' 내부값이 null이거나 비어있습니다");
	    }
	}
}
