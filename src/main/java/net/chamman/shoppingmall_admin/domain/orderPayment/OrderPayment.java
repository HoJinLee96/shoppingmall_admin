package net.chamman.shoppingmall_admin.domain.orderPayment;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import net.chamman.shoppingmall_admin.domain.order.Order;
import net.chamman.shoppingmall_admin.domain.payment.Payment;
import net.chamman.shoppingmall_admin.infra.payment.dto.PaymentCancelResponseDto;

@Entity
@Getter @Builder @AllArgsConstructor
@DiscriminatorValue("ORDER")
public class OrderPayment extends Payment {

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "order_id", nullable = false)
	private Order order;
	
	protected void cancelComplete(PaymentCancelResponseDto response) {
		this.canceledAt = response.canceledAt();
		this.paymentStatus=PaymentStatus.CANCELED;
	}
}