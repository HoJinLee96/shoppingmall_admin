package net.chamman.shoppingmall_admin.domain.payment;

import java.time.LocalDateTime;

import org.hibernate.annotations.SQLRestriction;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import net.chamman.shoppingmall_admin.support.BaseEntity;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@SQLRestriction("deleted = false")
@Inheritance(strategy = InheritanceType.JOINED) // JOINED 전략 사용
@DiscriminatorColumn(name = "DTYPE") // 자식 타입을 구분할 컬럼
@Table(name = "payment")
public class Payment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;

    @Column(nullable = false)
    protected Integer amount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    protected PaymentMethod paymentMethod; // 수단

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    protected PaymentStatus paymentStatus; // 상태

    @Column(nullable = false)
    protected LocalDateTime requestedAt; // 요청 시각

    protected LocalDateTime approvedAt; // 승인 시각 (완료 시각)
    
    protected LocalDateTime canceledAt; // 취소 시각 (취소 시각)
    
    // --- PG사 연동을 위한 실무적 컬럼 ---
    @Column(nullable = false, unique = true)
    protected String paymentKey; // PG사가 발급하는 건별 고유 키 (매우 중요)

    @Column(length = 20)
    protected String pgProvider; // PG사 이름 (e.g., "toss_payments")

	@Getter @AllArgsConstructor
	public enum PaymentMethod {
	    CREDIT_CARD("신용카드/체크카드"),
//	    BANK_TRANSFER("계좌이체"),
//	    VIRTUAL_ACCOUNT("가상계좌"),
//	    MOBILE_PAYMENT("휴대폰결제"),
	    NAVER_PAY("네이버페이"),
	    KAKAO_PAY("카카오페이"),
	    TOSS_PAY("토스페이"),
	    PAYCO("페이코");
		
		private final String label;

	}
	
	@Getter @AllArgsConstructor
	public enum PaymentStatus {
	    READY("대기 중"),
	    IN_PROGRESS("진행 중 "),
	    DONE("완료"),
	    CANCELED("취소"),
	    FAILED("실패"),
	    ABORTED("중단");

	    private final String label;

	}
	
	// 자식 클래스의 Builder가 부모 필드를 초기화하기 위한 생성자
    protected Payment(Integer amount, PaymentMethod paymentMethod, PaymentStatus paymentStatus,
                      LocalDateTime requestedAt, LocalDateTime approvedAt, LocalDateTime canceledAt,
                      String paymentKey, String pgProvider) {
        this.amount = amount;
        this.paymentMethod = paymentMethod;
        this.paymentStatus = paymentStatus;
        this.requestedAt = requestedAt;
        this.approvedAt = approvedAt;
        this.canceledAt = canceledAt;
        this.paymentKey = paymentKey;
        this.pgProvider = pgProvider;
    }
	
}