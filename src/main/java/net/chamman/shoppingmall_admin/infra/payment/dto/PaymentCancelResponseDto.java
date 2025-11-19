package net.chamman.shoppingmall_admin.infra.payment.dto;

import java.time.LocalDateTime;
import lombok.Builder;

/**
 * PG사로부터 결제 취소 응답을 받아 정규화(Normalized)한 DTO
 */
@Builder
public record PaymentCancelResponseDto(
    String paymentKey,     // [필수] 원본 결제 키
    String status,         // [필수] PG사에서 응답한 현재 결제 상태 (e.g., "CANCELED", "PARTIAL_CANCELED")
    String orderId,        // [참고] 쇼핑몰 주문 번호
    Integer totalAmount,    // [참고] 원본 결제 금액
    Integer canceledAmount, // [필수] 이 요청을 포함하여 현재까지 취소된 *총* 누적 금액
    String cancelReason,   // 처리된 취소 사유
    LocalDateTime canceledAt // [필수] PG사에서 취소(환불)가 승인된 시각
) {
}