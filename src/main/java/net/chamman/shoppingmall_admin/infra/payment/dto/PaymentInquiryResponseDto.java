package net.chamman.shoppingmall_admin.infra.payment.dto;

import java.time.LocalDateTime;

import lombok.Builder;
import net.chamman.shoppingmall_admin.domain.payment.Payment;

/**
 * PG사에 결제 정보를 조회한 후 반환받는 표준(Normalized) DTO
 */
@Builder
public record PaymentInquiryResponseDto(
    String paymentKey,                // [필수] PG사 결제 고유 키
    String orderId,                   // [참고] 쇼핑몰 주문 번호
    Payment.PaymentStatus status,     // [필수] 현재 결제 상태 (DONE, CANCELED, PARTIAL_CANCELED 등)
    Payment.PaymentMethod paymentMethod, // [필수] 결제 수단
    Integer totalAmount,              // [필수] 총 결제 금액
    Integer canceledAmount,           // [필수] 현재까지 취소된 총 금액
    LocalDateTime approvedAt,         // [참고] 결제 승인 시각
    LocalDateTime canceledAt          // [선택] 마지막 취소 발생 시각 (부분 취소 포함)
) {
}