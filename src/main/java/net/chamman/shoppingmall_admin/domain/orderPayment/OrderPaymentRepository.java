package net.chamman.shoppingmall_admin.domain.orderPayment;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import net.chamman.shoppingmall_admin.domain.payment.Payment.PaymentStatus;

public interface OrderPaymentRepository extends JpaRepository<OrderPayment, Long> {

	// 특정 주문의 '결제 완료' 또는 '부분 취소' 상태인 유효한 결제 내역 조회
    // 1개의 주문에 유효한 결제는 1건이라고 가정 (복합 결제 시 List로 변경 필요)
    @Query("SELECT op FROM OrderPayment op " +
           "WHERE op.order.id = :orderId " +
           "AND op.paymentStatus IN :statuses")
    Optional<OrderPayment> findByOrderIdAndStatusIn(
            @Param("orderId") Long orderId, 
            @Param("statuses") List<PaymentStatus> statuses);
    
    /**
	 * OrderReturn의 OrderItem을 통해 원본 OrderPayment 조회
	 * JPQL을 사용하여 N+1 없이 한 번에 조회합니다. (OrderReturn -> OrderItem -> Order -> OrderPayment)
	 * @param orderReturnId OrderReturn의 ID
	 * @param statuses 유효한 결제 상태 (DONE, IN_PROGRESS)
	 * @return
	 */
	@Query("SELECT op FROM OrderPayment op " +
		   "JOIN op.order o " +
		   "JOIN o.orderItems oi " +
		   "JOIN oi.orderReturns ort " +
		   "WHERE ort.id = :orderReturnId " +
		   "AND op.paymentStatus IN :statuses")
	Optional<OrderPayment> findByOrderReturnIdAndStatusIn(
			@Param("orderReturnId") Long orderReturnId, 
			@Param("statuses") List<PaymentStatus> statuses);
	
}
