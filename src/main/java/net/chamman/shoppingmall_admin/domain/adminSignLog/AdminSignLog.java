package net.chamman.shoppingmall_admin.domain.adminSignLog;

import org.hibernate.annotations.SQLRestriction;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import net.chamman.shoppingmall_admin.domain.admin.Admin;
import net.chamman.shoppingmall_admin.support.BaseEntity;

@Entity
@Getter
@SQLRestriction("deleted = false")
@Table(name="admin_sign_log")
public class AdminSignLog extends BaseEntity{
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name ="admin_sign_log_id")
	private int signLogId;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "admin_id", nullable = true)
	private Admin admin;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "sign_result", nullable = false)
	private SignResult signResult;
	
	@Column(name ="reason", length=100)
	private String reason;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "resolve_by", referencedColumnName = "admin_sign_log_id")
	private AdminSignLog resolveBy;
	
	public enum SignResult {
		
		SIGNUP("회원가입"),
		SIGNOUT("로그아웃"), 
		SIGNIN("로그인"),
		DELETE("회원 탈퇴"), 
		CONFIRM_PASSWORD("비밀번호 확인"),
		UPDATE_PASSWORD("비밀번호 업데이트"),
		MISMATCH_EMAIL("이메일 불일치"),	
		MISMATCH_PASSWORD("비밀번호 불일치"), 
		IP_BLOCKED("IP 차단"),
		REFRESH("리프레쉬"),
		REFRESH_FAIL("리프레쉬 실패"),
		BLACKLIST_TOKEN("블랙리스트 토큰"),
		ACCOUNT_STAY("계정 STAY 상태"),
		ACCOUNT_STOP("계정 STOP 상태"),
//		ACCOUNT_DELETE("계정 DELETE 상태"),
		ACCOUNT_LOCKED("계정 LOCKED 상태"),
		ACCOUNT_UNVERIFIED("계정 UNVERIFIED 상태"),
		SERVER_ERROR("서버 에러");
		
		private final String label;

		SignResult(String label) {
			this.label = label;
		}

		public String getLabel() {
			return label;
		}
	}
	
	public static AdminSignLog createAdminSignLog(Admin admin, SignResult signResult, String clientIp) {
		AdminSignLog adminSignLog = new AdminSignLog();
		adminSignLog.admin = admin;
		adminSignLog.signResult = signResult;
		adminSignLog.clientIp = clientIp;
		return adminSignLog;
	}
}
