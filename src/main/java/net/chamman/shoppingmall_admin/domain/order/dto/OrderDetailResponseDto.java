package net.chamman.shoppingmall_admin.domain.order.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import net.chamman.shoppingmall_admin.domain.address.Address;
import net.chamman.shoppingmall_admin.domain.member.Member;
import net.chamman.shoppingmall_admin.domain.order.Order;
import net.chamman.shoppingmall_admin.domain.orderItem.OrderItem;
import net.chamman.shoppingmall_admin.domain.orderPayment.OrderPayment;
import net.chamman.shoppingmall_admin.domain.payment.Payment;
import net.chamman.shoppingmall_admin.domain.shipment.Shipment;
import net.chamman.shoppingmall_admin.security.obfuscation.Obfuscator;

public record OrderDetailResponseDto(
    Long orderId,
    LocalDateTime orderDate,
    MemberInfo memberInfo,
    AddressInfo addressInfo,
    List<PaymentInfo> paymentInfo,
    List<OrderItemInfo> orderItems
) {

    public record MemberInfo(
        String memberName,
        String email,
        String phone
    ) {}

    public record AddressInfo(
        String recipient,
        String recipientPhone,
        String postcode,
        String mainAddress,
        String detailAddress,
        String shippingMemo
    ) {}

    public record PaymentInfo(
        int amount,
        Payment.PaymentMethod paymentMethod,
        Payment.PaymentStatus status,
        LocalDateTime approvedAt
    ) {}

    public record OrderItemInfo( 
        Long orderItemId,
        String productName,
        String productCode,
        int count,
        int finalAmount,
        String orderStatus,
        ShipmentInfo shipmentInfo
    ) {}

    public record ShipmentInfo(
        String shippingCompany,
        String trackingNumber
    ) {}


    // 엔티티를 상세 DTO로 변환 (Builder 대신 생성자 사용)
    public static OrderDetailResponseDto fromEntity(Order order, Obfuscator obfuscator) {
        Member member = order.getMember();
        Address address = order.getAddress();

        return new OrderDetailResponseDto( // Builder -> new
            obfuscator.obfuscate(order.getId()),
            order.getCreatedAt(),
            new MemberInfo( // Builder -> new
                member.getName(),
                member.getEmail(),
                member.getPhone()
            ),
            new AddressInfo( // Builder -> new
                address.getRecipient(),
                address.getRecipientPhone(),
                address.getPostcode(),
                address.getMainAddress(),
                address.getDetailAddress(),
                address.getShippingMemo()
            ),
            order.getOrderPayments().stream()
                    .map(OrderDetailResponseDto::toPaymentInfo)
                    .collect(Collectors.toList()),
            order.getOrderItems().stream()
                    .map(item -> toOrderItemInfo(item, obfuscator))
                    .collect(Collectors.toList())
        );
    }

    private static PaymentInfo toPaymentInfo(OrderPayment payment) {
        return new PaymentInfo(
            payment.getAmount(),
            payment.getPaymentMethod(),
            payment.getPaymentStatus(),
            payment.getApprovedAt()
        );
    }

    private static OrderItemInfo toOrderItemInfo(OrderItem item, Obfuscator obfuscator) {
        Shipment shipment = item.getShipment();
        ShipmentInfo shipmentInfo = null;
        if (shipment != null) {
            shipmentInfo = new ShipmentInfo( 
                shipment.getShippingCompany(),
                shipment.getTrackingNumber()
            );
        }

        return new OrderItemInfo( 
            obfuscator.obfuscate(item.getId()),
            item.getProductVariant().getName(), // 상품명
            item.getProductVariant().getCode(), // 상품코드
            item.getCount(),
            item.getFinalAmount(),
            item.getOrderItemStatus().getLabel(),
            shipmentInfo
        );
    }
}