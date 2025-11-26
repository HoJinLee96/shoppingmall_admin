package net.chamman.shoppingmall_admin.domain.couponPolicy;

import java.time.LocalDateTime;

import org.hibernate.annotations.SQLRestriction;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import net.chamman.shoppingmall_admin.support.BaseEntity;

@Entity
@Getter
@SQLRestriction("deleted = false")
@Table(name = "coupon_policy")
public class CouponPolicy extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    private String code;

    @Column(nullable = false)
    private String name;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CouponType couponType; // 할인 타입 (발급으로부터, 정해진 날짜)

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DiscountType type; // 할인 타입 (정액, 정률)

    @Column(nullable = false)
    private int discountValue; // 할인 값 (1000원, 10%)

    private LocalDateTime validFrom; // 유효기간 시작

    private LocalDateTime validTo; // 유효기간 종료
    
    private int usagePeriodDays; // 사용 기간일

    @Column(nullable = false)
    private int minOrderAmount; // 최소 주문 금액
    
    public enum CouponType {
    	ISSUED_BASED("발급으로부터"), DATE_RANGE("정해진 날짜");
    	
    	private final String label;
    	
    	CouponType(String label) {
    		this.label = label;
    	}
    	
    	public String getLabel() {
    		return label;
    	}
    }

    public enum DiscountType {
    	AMOUNT("정액 할인"), PERCENTAGE("정률 할인");
	    
		private final String label;

		DiscountType(String label) {
			this.label = label;
		}

		public String getLabel() {
			return label;
		}
    }
}