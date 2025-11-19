package net.chamman.shoppingmall_admin.domain.admin;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.SQLRestriction;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import net.chamman.shoppingmall_admin.domain.answer.Answer;
import net.chamman.shoppingmall_admin.exception.domain.admin.status.AdminStatusDeleteException;
import net.chamman.shoppingmall_admin.exception.domain.admin.status.AdminStatusLockedException;
import net.chamman.shoppingmall_admin.exception.domain.admin.status.AdminStatusStayException;
import net.chamman.shoppingmall_admin.exception.domain.admin.status.AdminStatusStopException;
import net.chamman.shoppingmall_admin.exception.domain.admin.status.AdminStatusUnverifiedException;
import net.chamman.shoppingmall_admin.support.BaseEntity;

@Entity
@Getter @Builder
@NoArgsConstructor
@AllArgsConstructor
@SQLRestriction("deleted = false")
@Table(name = "admin")
public class Admin extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false, unique = true, length = 15)
	private String userName;

	@Column(length = 60)
	private String password;

	@Column(nullable = false, length = 20)
	private String name;

	@Column(unique = true, length = 14)
	private String phone;

	@Column(nullable = false, unique = true, length = 50)
	private String email;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private AdminStatus adminStatus;

	@Column(length = 50)
	private String statusReason;
	
	@BatchSize(size = 100)
	@OneToMany(mappedBy = "admin")
	@Builder.Default
	private List<Answer> answers = new ArrayList<>();

	public enum AdminStatus {
		
		ACTIVE("활성"), 
		STAY("일시 정지"), // 로그인 불가능, 작업 불가능, 활성화 과정 진행 (휴면 계정 등)
		STOP("중지"), // 로그인 불가능, 작업 불가능, 상담 통해 로그인 (악의 적인 계정인 경우 등)
		LOCKED("잠금"), // 로그인 불가능, 작업 불가능, 인증 통해 비밀번호 변경 후 로그인 (비밀번호 10회 틀린 경우, 해외 IP 접속 등)
		UNVERIFIED("미인증"), // 로그인 가능, 작업 불가능, 인증 통해 로그인 (단순 인증 문제인 경우 등)
		DELETE("탈퇴"); 
		
		private final String label;

		AdminStatus(String label) {
			this.label = label;
		}

		public String getLabel() {
			return label;
		}
	}
	
	@SuppressWarnings("incomplete-switch")
	public void isActive() {
		switch (this.adminStatus) {
		case STAY -> throw new AdminStatusStayException("일시 정지된 계정. admin.id: " + this.id);
		case STOP -> throw new AdminStatusStopException("정지된 계정. admin.id: " + this.id);
		case LOCKED -> throw new AdminStatusLockedException("잠긴 계정. admin.id: " + this.id);
		case UNVERIFIED -> throw new AdminStatusUnverifiedException("인증이 필요한 계정. admin.id: " + this.id);
		case DELETE -> throw new AdminStatusDeleteException("탈퇴한 계정. admin.id: " + this.id);
		}
	}
	
	// 회원 탈퇴 이후 복구 가능 및 데이터 기록을 위한 softDelete 처리
	public void softDelete() {
		this.adminStatus = AdminStatus.DELETE;
		this.statusReason = "사용자 요청에 의한 탈퇴";
		this.deleted = true;
		this.deletedAt = LocalDateTime.now();
	}
	
	public void changeLockedStatus() {
		this.adminStatus = AdminStatus.LOCKED;
	}
}
