package net.chamman.shoppingmall_admin.domain.returnPayment;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.chamman.shoppingmall_admin.domain.orderPayment.OrderPayment;
import net.chamman.shoppingmall_admin.domain.orderPayment.OrderPaymentRepository;
import net.chamman.shoppingmall_admin.domain.orderReturn.OrderReturn;
import net.chamman.shoppingmall_admin.domain.orderReturn.OrderReturn.ReturnReason;
import net.chamman.shoppingmall_admin.domain.payment.Payment;
import net.chamman.shoppingmall_admin.domain.payment.Payment.PaymentStatus;
import net.chamman.shoppingmall_admin.exception.domain.payment.ReturnPaymentIllegalException;
import net.chamman.shoppingmall_admin.infra.payment.PaymentGateway;
import net.chamman.shoppingmall_admin.infra.payment.dto.PaymentCancelRequestDto;
import net.chamman.shoppingmall_admin.infra.payment.dto.PaymentCancelResponseDto;

@RequiredArgsConstructor
@Slf4j
@Service
public class ReturnPaymentService {

	private final ReturnPaymentRepository returnPaymentRepository;
	private final OrderPaymentRepository orderPaymentRepository;
	private final PaymentGateway paymentGateway;
	
	private static final int CUSTOMER_FAULT_DEDUCTION = 6000;

	/**
     * 주문 '반품'에 대한 환불 처리 (부분/전액 환불)
     * - OrderReturnService의 processReturnRefund에서 호출됩니다.
     */
	@Transactional
    public ReturnPayment refundOrderReturn(OrderReturn orderReturn) {
        
        log.debug("* 반품 환불 처리 시작. OrderReturnId: {}", orderReturn.getId());
        // 1. 환불할 원본 결제(OrderPayment)를 찾음
        OrderPayment originalPayment = orderPaymentRepository.findByOrderReturnIdAndStatusIn(orderReturn.getId(), List.of(PaymentStatus.DONE))
                .orElseThrow(() -> new ReturnPaymentIllegalException("환불할 수 있는 완료된 결제 내역이 없습니다."));

        // 2. 환불 금액 계산 (귀책 사유 반영)
        int refundAmount = calculateRefundAmount(orderReturn);

        // 3. PG사 취소 요청 (금액이 0원이면 요청 스킵)
        LocalDateTime approvedAt = LocalDateTime.now();
        
        if (refundAmount > 0) {
            PaymentCancelRequestDto requestDto = PaymentCancelRequestDto.builder()
                    .paymentKey(originalPayment.getPaymentKey())
                    .cancelAmount(refundAmount)
                    .build();

            // PG사 호출 (실패 시 예외 발생하여 트랜잭션 롤백됨)
            PaymentCancelResponseDto response = paymentGateway.cancelPayment(requestDto);
            
            approvedAt = response.canceledAt();
            
            log.info("* PG 결제 취소 성공. PaymentKey: {}, Refund Amount: {}", response.paymentKey(), refundAmount);
            
        } else {
            log.info("* 환불 금액이 0원이므로 PG사 요청 생략. (반품비 차감 등으로 인한 0원)");
        }

        // 5. ReturnPayment 엔티티 생성 및 저장 (환불 이력 기록)
        ReturnPayment returnPayment = ReturnPayment.builder()
                .orderReturn(orderReturn)
                .amount(refundAmount)
                .paymentMethod(originalPayment.getPaymentMethod())
                .paymentStatus(Payment.PaymentStatus.DONE) // 환불 건 자체는 '완료'됨
                .requestedAt(orderReturn.getCreatedAt())
                .approvedAt(approvedAt)
                .paymentKey("RETURN_" + originalPayment.getPaymentKey() + "_" + orderReturn.getId()) // 유니크 키 생성
                .pgProvider(originalPayment.getPgProvider())
                .build();
        
        return returnPayment;
    }

    private int calculateRefundAmount(OrderReturn orderReturn) {
    	
        // '주문 당시 가격(originalAmount) * 반품 수량'
        int totalItemPrice = orderReturn.getOrderItem().getOriginalAmount() * orderReturn.getReturnCount();
        
        if (orderReturn.getReturnReason() == ReturnReason.SELLER_FAULT) {
            // 판매자 귀책: 전액 환불
            return totalItemPrice;
        } else {
            // 구매자 귀책: 배송비 차감
            int finalAmount = totalItemPrice - CUSTOMER_FAULT_DEDUCTION;
            return Math.max(finalAmount, 0);
        }
    }
    
}
