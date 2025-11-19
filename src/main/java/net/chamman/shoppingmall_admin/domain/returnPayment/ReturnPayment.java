package net.chamman.shoppingmall_admin.domain.returnPayment;

import java.time.LocalDateTime;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import net.chamman.shoppingmall_admin.domain.orderReturn.OrderReturn;
import net.chamman.shoppingmall_admin.domain.payment.Payment;

@Entity
@Getter @Builder
@NoArgsConstructor
@AllArgsConstructor
@DiscriminatorValue("RETURN")
public class ReturnPayment extends Payment {

	@OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_return_id", nullable = false)
    private OrderReturn orderReturn;
    
	@Builder
    public ReturnPayment(OrderReturn orderReturn, Integer amount, PaymentMethod paymentMethod, PaymentStatus paymentStatus,
                         LocalDateTime requestedAt, LocalDateTime approvedAt, LocalDateTime canceledAt,
                         String paymentKey, String pgProvider) {
        super(amount, paymentMethod, paymentStatus, requestedAt, approvedAt, canceledAt, paymentKey, pgProvider);
        this.orderReturn = orderReturn;
    }
}