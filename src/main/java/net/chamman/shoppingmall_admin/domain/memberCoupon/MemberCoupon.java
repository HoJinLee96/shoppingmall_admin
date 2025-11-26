package net.chamman.shoppingmall_admin.domain.memberCoupon;

import java.time.LocalDateTime;

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
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import net.chamman.shoppingmall_admin.domain.couponPolicy.CouponPolicy;
import net.chamman.shoppingmall_admin.domain.member.Member;
import net.chamman.shoppingmall_admin.domain.orderItem.OrderItem;
import net.chamman.shoppingmall_admin.support.BaseEntity;

@Entity
@Getter
@SQLRestriction("deleted = false")
@Table(name = "member_coupon")
public class MemberCoupon extends BaseEntity{

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "coupon_policy_id", nullable = false)
	private CouponPolicy couponPolicy;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "member_id", nullable = false)
	private Member member;
	
	@OneToOne(mappedBy = "memberCoupon")
	private OrderItem orderItem;
	
    @Column(nullable = false)
	private LocalDateTime issuedAt; // 발급된 시점

	private LocalDateTime usedAt; // 사용된 시점

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CouponStatus status; // 쿠폰 상태
    
    public enum CouponStatus {
    	ACTIVE("사용 가능"), USED("사용 완료"), EXPIRED("기간 만료");
	    
		private final String label;

		CouponStatus(String label) {
			this.label = label;
		}

		public String getLabel() {
			return label;
		}
    }
	
    /**
     * 쿠폰 사용 처리
     */
    public void use(OrderItem orderItem) {
        if (!isAvailable()) {
            throw new IllegalStateException("이미 사용했거나 만료된 쿠폰입니다.");
        }
        this.status = CouponStatus.USED;
        this.usedAt = LocalDateTime.now();
        this.orderItem = orderItem;
    }
    
    /**
     * 쿠폰 복원 처리 (주문 취소 시)
     */
    public void restore() {
    	// 이미 사용된 쿠폰이 아니면 복원할 필요 없음
        if (this.status != CouponStatus.USED) {
            return;
        }
        this.status = CouponStatus.ACTIVE;
        this.usedAt = null;
        this.orderItem = null;
    }

    /**
     * 현재 쿠폰을 사용할 수 있는지 확인하는 로직
     */
    public boolean isAvailable() {
        if (this.status != CouponStatus.ACTIVE) {
            return false;
        }
        
        // 쿠폰 정책(coupon)의 타입에 따라 유효기간을 실시간으로 계산
        LocalDateTime now = LocalDateTime.now();
        CouponPolicy couponPolicy = this.couponPolicy;

        if (couponPolicy.getCouponType() == CouponPolicy.CouponType.DATE_RANGE) {
            // 기간 지정 쿠폰의 유효성 검사
            return now.isAfter(couponPolicy.getValidFrom()) && now.isBefore(couponPolicy.getValidTo());
        } else if (couponPolicy.getCouponType() == CouponPolicy.CouponType.ISSUED_BASED) {
            // 발급일 기준 쿠폰의 유효성 검사
            LocalDateTime expiryDate = this.issuedAt.plusDays(couponPolicy.getUsagePeriodDays());
            return now.isBefore(expiryDate);
        }
        
        return false;
    }

}