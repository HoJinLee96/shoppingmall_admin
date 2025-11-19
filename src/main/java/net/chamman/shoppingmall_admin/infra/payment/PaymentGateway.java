package net.chamman.shoppingmall_admin.infra.payment;

import net.chamman.shoppingmall_admin.infra.payment.dto.PaymentCancelRequestDto;
import net.chamman.shoppingmall_admin.infra.payment.dto.PaymentCancelResponseDto;
import net.chamman.shoppingmall_admin.infra.payment.dto.PaymentInquiryResponseDto;

/**
 * 결제 게이트웨이(PG사) 연동을 위한 인프라 인터페이스
 * (구현체: TossPaymentGateway, KakaoPaymentGateway, FakePaymentGateway 등)
 */
public interface PaymentGateway {

    /**
     * 결제를 취소(환불)합니다.
     * 이 메서드는 전액 취소와 부분 취소를 모두 처리할 수 있어야 합니다.
     *
     * @param requestDto 결제 취소에 필요한 정보 (paymentKey, 사유, 금액 등)
     * @return 결제 취소 결과
     */
    PaymentCancelResponseDto cancelPayment(PaymentCancelRequestDto requestDto);

    /**
     * PG사에 결제 정보를 조회합니다.
     *
     * @param paymentKey PG사의 결제 고유 키
     * @return 결제 조회 결과
     */
    PaymentInquiryResponseDto inquirePayment(String paymentKey);
}