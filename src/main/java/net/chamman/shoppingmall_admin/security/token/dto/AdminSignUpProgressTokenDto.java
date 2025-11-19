package net.chamman.shoppingmall_admin.security.token.dto;

import lombok.Getter;
import net.chamman.shoppingmall_admin.security.crypto.AesProvider;
import net.chamman.shoppingmall_admin.security.crypto.Cryptable;
import net.chamman.shoppingmall_admin.security.token.TokenService.TokenType;

@Getter
public class AdminSignUpProgressTokenDto implements Cryptable<AdminSignUpProgressTokenDto>{
	
	private SignUpStatus status;
    private String username;
    private String password; // 암호화된 상태로 저장
    private String email;
    private String phone;
    
    public static final TokenType TOKENTYPE = TokenType.ADMIN_SIGNUP_PROGRESS;
    
    // 단계별 생성자를 private으로 만들어 외부에서 상태 조작을 막음
    private AdminSignUpProgressTokenDto(String username, String password) {
        this.status = SignUpStatus.USERNAME_PASSWORD_ENTERED;
        this.username = username;
        this.password = password;
    }

    private AdminSignUpProgressTokenDto(SignUpStatus status, String username, String password, String email, String phone) {
        this.status = status;
        this.username = username;
        this.password = password;
        this.email = email;
        this.phone = phone;
    }

    // 초기 단계 생성용 정적 팩토리 메서드
    public static AdminSignUpProgressTokenDto firstStep(String username, String password) {
        return new AdminSignUpProgressTokenDto(username, password);
    }
    
    // 다음 단계로 상태를 전이시키는 메서드
    public AdminSignUpProgressTokenDto withEmail(String email) {
        // 이메일 추가 단계는 USERNAME_PASSWORD_ENTERED 상태에서만 가능
        if (this.status != SignUpStatus.USERNAME_PASSWORD_ENTERED) {
            throw new IllegalStateException("Invalid sign-up status for adding email: " + this.status);
        }
        return new AdminSignUpProgressTokenDto(SignUpStatus.EMAIL_VERIFIED, this.username, this.password, email, this.phone);
    }

    public AdminSignUpProgressTokenDto withPhone(String phone) {
        // 핸드폰 추가 단계는 EMAIL_VERIFIED 상태에서만 가능
        if (this.status != SignUpStatus.EMAIL_VERIFIED) {
            throw new IllegalStateException("Invalid sign-up status for adding phone: " + this.status);
        }
        return new AdminSignUpProgressTokenDto(SignUpStatus.PHONE_VERIFIED, this.username, this.password, this.email, phone);
    }

    // 암호화/복호화 로직 (Cryptable 구현)
    @Override
    public AdminSignUpProgressTokenDto encrypt(AesProvider aesProvider) {
        return new AdminSignUpProgressTokenDto(
            this.status, // status는 암호화하지 않음
            aesProvider.encrypt(this.username),
            aesProvider.encrypt(this.password),
            this.email != null ? aesProvider.encrypt(this.email) : null,
            this.phone != null ? aesProvider.encrypt(this.phone) : null
        );
    }

    @Override
    public AdminSignUpProgressTokenDto decrypt(AesProvider aesProvider) {
         return new AdminSignUpProgressTokenDto(
            this.status,
            aesProvider.decrypt(this.username),
            aesProvider.decrypt(this.password),
            this.email != null ? aesProvider.decrypt(this.email) : null,
            this.phone != null ? aesProvider.decrypt(this.phone) : null
        );
    }

    // 회원가입 진행 상태 Enum
    public enum SignUpStatus {
        USERNAME_PASSWORD_ENTERED, // 아이디/비번 입력 완료
        EMAIL_VERIFIED,            // 이메일 인증 완료
        PHONE_VERIFIED,            // 핸드폰 인증 완료
        COMPLETE                   // 모든 절차 완료 (최종 저장 직전)
    }
	
}
