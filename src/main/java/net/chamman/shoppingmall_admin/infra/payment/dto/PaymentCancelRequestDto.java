package net.chamman.shoppingmall_admin.infra.payment.dto;

import lombok.Builder;

/**
 * PG사에 결제 취소를 요청할 때 보내는 DTO
 */
@Builder
public record PaymentCancelRequestDto(
    String paymentKey,      // [필수] 취소할 결제의 고유 키
    Integer cancelAmount   // [필수] 취소할 금액.
//    RefundAccount refundAccount // [선택] 가상계좌 환불 시에만 필요한 계좌 정보
) {
    /**
     * 가상계좌 환불 시 필요한 계좌 정보
     */
//    public record RefundAccount(
//        String bank,        // 은행 코드 (e.g., "KB", "004")
//        String accountNumber, // 계좌번호
//        String holderName   // 예금주명
//    ) {}
}