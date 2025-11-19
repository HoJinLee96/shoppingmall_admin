package net.chamman.shoppingmall_admin.security.jwt;

import java.util.Collection;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import net.chamman.shoppingmall_admin.security.principal.AdminDetails;

public class JwtAuthenticationToken extends AbstractAuthenticationToken {

    private final Object principal; // 인증 전: accessToken(String), 인증 후: AdminDetails
    private Object credentials;     // 인증 전: null, 인증 후: null (비밀번호 같은거 안씀)

    /**
     * 인증 전에 사용되는 생성자 (인증 요청용)
     *
     * @param accessToken 원본 JWT 문자열
     */
    public JwtAuthenticationToken(String accessToken) {
        super(null); // 아직 권한 없음
        this.principal = accessToken;
        this.credentials = null;
        setAuthenticated(false); // 아직 인증 안됨
    }

    /**
     * 인증 후에 사용되는 생성자 (인증 완료)
     *
     * @param principal   AdminDetails 객체
     * @param authorities 권한 목록
     */
    public JwtAuthenticationToken(AdminDetails principal, Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.principal = principal;
        this.credentials = null; // 비번은 사용 후 제거 (우린 안썼지만)
        setAuthenticated(true); // 인증 완료
    }

    @Override
    public Object getCredentials() {
        return this.credentials;
    }

    @Override
    public Object getPrincipal() {
        return this.principal;
    }
}