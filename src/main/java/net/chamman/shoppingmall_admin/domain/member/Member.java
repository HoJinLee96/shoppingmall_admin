package net.chamman.shoppingmall_admin.domain.member;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.SQLRestriction;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import net.chamman.shoppingmall_admin.domain.address.Address;
import net.chamman.shoppingmall_admin.domain.cartItem.CartItem;
import net.chamman.shoppingmall_admin.domain.coupon.MemberCoupon;
import net.chamman.shoppingmall_admin.domain.oauth.Oauth;
import net.chamman.shoppingmall_admin.domain.order.Order;
import net.chamman.shoppingmall_admin.domain.question.entity.Question;
import net.chamman.shoppingmall_admin.domain.review.Review;
import net.chamman.shoppingmall_admin.domain.wishlist.Wishlist;
import net.chamman.shoppingmall_admin.exception.domain.member.status.MemberStatusDeleteException;
import net.chamman.shoppingmall_admin.exception.domain.member.status.MemberStatusLockedException;
import net.chamman.shoppingmall_admin.exception.domain.member.status.MemberStatusStayException;
import net.chamman.shoppingmall_admin.exception.domain.member.status.MemberStatusStopException;
import net.chamman.shoppingmall_admin.exception.domain.member.status.MemberStatusUnverifiedException;
import net.chamman.shoppingmall_admin.support.BaseEntity;

@Entity
@Getter
@SQLRestriction("deleted = false")
@Table(name = "member")
public class Member extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private MemberProvider memberProvider;

	@Column(nullable = false, unique = true, length = 15)
	private String username;

	@Column(length = 60)
	private String password;

	@Column(nullable = false, length = 20)
	private String name;

	@Column(unique = true, length = 14)
	private String phone;

	@Column(nullable = false, unique = true, length = 50)
	private String email;

	@Column(nullable = false)
	private boolean smsMarketingReceivedStatus;

	@Column(nullable = false)
	private boolean emailMarketingReceivedStatus;
	
	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private MemberStatus memberStatus;
	
	@Column(length = 50)
	private String statusReason;

    @BatchSize(size = 100)
    @OneToMany(mappedBy = "member", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Oauth> oauths = new ArrayList<>();

    @BatchSize(size = 100)
    @OneToMany(mappedBy = "member", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Address> addresses = new ArrayList<>();

    @BatchSize(size = 100)
    @OneToMany(mappedBy = "member", fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH})
    private List<Order> orders = new ArrayList<>();

    @BatchSize(size = 100)
    @OneToMany(mappedBy = "member", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MemberCoupon> memberCoupons = new ArrayList<>();

    @BatchSize(size = 100)
    @OneToMany(mappedBy = "member", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CartItem> cartItems = new ArrayList<>();

    @BatchSize(size = 100)
    @OneToMany(mappedBy = "member", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Wishlist> wishlists = new ArrayList<>();

    @BatchSize(size = 100)
    @OneToMany(mappedBy = "member", fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH})
    private List<Question> questions = new ArrayList<>();

    @BatchSize(size = 100)
    @OneToMany(mappedBy = "member", fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH})
    private List<Review> reviews = new ArrayList<>();

	public enum MemberProvider {
		LOCAL("통합 회원"), OAUTH("소셜 회원");

		private final String label;

		MemberProvider(String label) {
			this.label = label;
		}

		public String getLabel() {
			return label;
		}
	}
	
	public enum MemberStatus {
		
		ACTIVE("활성"), 
		STAY("일시 정지"), // 로그인 불가능, 구매 불가능, 활성화 과정 진행 (휴면 계정 등)
		STOP("중지"), // 로그인 불가능, 구매 불가능, 상담 통해 로그인 (악의 적인 계정인 경우 등)
		LOCKED("잠금"), // 로그인 불가능, 구매 불가능, 인증 통해 비밀번호 변경 후 로그인 (비밀번호 10회 틀린 경우, 해외 IP 접속 등)
		UNVERIFIED("미인증"), // 로그인 가능, 구매 불가능, 인증 통해 로그인 (단순 인증 문제인 경우 등)
		DELETE("탈퇴"); 
		
		private final String label;

		MemberStatus(String label) {
			this.label = label;
		}

		public String getLabel() {
			return label;
		}
	}
	
	@SuppressWarnings("incomplete-switch")
	public void isActive() {
		switch (this.memberStatus) {
		case STAY -> throw new MemberStatusStayException("일시 정지된 계정. admin.id: " + this.id);
		case STOP -> throw new MemberStatusStopException("정지된 계정. admin.id: " + this.id);
		case LOCKED -> throw new MemberStatusLockedException("잠긴 계정. admin.id: " + this.id);
		case UNVERIFIED -> throw new MemberStatusUnverifiedException("인증이 필요한 계정. admin.id: " + this.id);
		case DELETE -> throw new MemberStatusDeleteException("탈퇴한 계정. admin.id: " + this.id);
		}
	}
	
	// 회원 탈퇴 이후 복구 가능 및 데이터 기록을 위한 softDelete 처리
	public void softDelete() {
		this.memberStatus = MemberStatus.DELETE;
		this.statusReason = "사용자 요청에 의한 탈퇴";
		this.smsMarketingReceivedStatus = false;
		this.emailMarketingReceivedStatus = false;

	}

	// 정보 익명화
	public void anonymize() {
		
		String anonymized = "_deleted_" + this.id + "_" + UUID.randomUUID().toString().substring(0, 8);
		String[] emailSplit = email.split("@");
		this.username = username.substring(0, 3) + "****" + anonymized;
		this.name = name.substring(0, 1) + "****" + anonymized;
		this.phone = null;
		this.email = emailSplit[0].substring(0, 3) + "****" + emailSplit[1] + anonymized;

	}
	
}
