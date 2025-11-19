package net.chamman.shoppingmall_admin.domain.orderPayment;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.chamman.shoppingmall_admin.domain.order.Order;
import net.chamman.shoppingmall_admin.domain.payment.Payment;
import net.chamman.shoppingmall_admin.exception.common.IllegalRequestException;
import net.chamman.shoppingmall_admin.infra.payment.PaymentGateway;
import net.chamman.shoppingmall_admin.infra.payment.dto.PaymentCancelRequestDto;
import net.chamman.shoppingmall_admin.infra.payment.dto.PaymentCancelResponseDto;

@RequiredArgsConstructor
@Slf4j
@Service
public class OrderPaymentService {

	private final PaymentGateway paymentGateway;

	@Transactional
	public void cancelCompleteOrder(Order order) {
        log.debug("* 취소 환불 처리 시작. OrderNumber: {}", order.getOrderNumber());

        // 1. '결제 완료(DONE)' 상태인 결제 건만 필터링
        OrderPayment orderPaymentToCancel = order.getOrderPayments().stream()
                .filter(p -> p.getPaymentStatus() == Payment.PaymentStatus.DONE)
                .findFirst()
                .orElseThrow(() -> new IllegalRequestException("취소 환불 처리 실패. 결제 내역(DONE)을 찾을 수 없습니다."));
        	
        // 2. 결제 수단별 로직 분기
    	PaymentCancelRequestDto dto = PaymentCancelRequestDto.builder()
            .paymentKey(orderPaymentToCancel.getPaymentKey())
            .cancelAmount(null) // 전액 취소
            .build();

    	// 3. PG사 취소 요청
    	PaymentCancelResponseDto response = paymentGateway.cancelPayment(dto);
    	
        // 4. DB 상태 변경
    	orderPaymentToCancel.cancelComplete(response);   
    	
        log.info("* PG 결제 취소 성공. PaymentKey: {}, Canceled Amount: {}", 
				response.paymentKey(), response.canceledAmount());
	}
	
}

