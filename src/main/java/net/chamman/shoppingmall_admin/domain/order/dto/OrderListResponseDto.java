package net.chamman.shoppingmall_admin.domain.order.dto;

import java.time.LocalDateTime;
import java.util.List;

import net.chamman.shoppingmall_admin.domain.order.Order;
import net.chamman.shoppingmall_admin.domain.orderItem.OrderItem;
import net.chamman.shoppingmall_admin.domain.orderPayment.OrderPayment;
import net.chamman.shoppingmall_admin.security.obfuscation.Obfuscator;

public record OrderListResponseDto(
		
		 Long orderId, // 난독화된 ID
		 String orderNumber,
	     LocalDateTime orderDate,
	     String memberName, // 주문자명
	     String primaryProductName, // 대표 상품명 (예: "상품 A 외 2건")
	     int totalAmount, // 총 결제 금액
	     String primaryOrderStatus // 대표 주문 상태
	    
		) {
	
	public static OrderListResponseDto fromEntity(Order order, Obfuscator obfuscator) {
        List<OrderItem> items = order.getOrderItems();
        String productName = "주문 상품 없음";
        String status = "상태 없음";
        int total = order.getOrderPayments().stream().mapToInt(OrderPayment::getAmount).sum();

        if (items != null && !items.isEmpty()) {
            OrderItem firstItem = items.get(0);
            productName = firstItem.getProductVariant().getName();
            if (items.size() > 1) {
                productName += " 외 " + (items.size() - 1) + "건";
            }
            status = firstItem.getOrderItemStatus().getLabel(); // 첫 번째 아이템의 상태를 대표로
        }
        
        return new OrderListResponseDto(
                obfuscator.obfuscate(order.getId()),
                order.getOrderNumber(),
                order.getCreatedAt(),
                order.getMember().getName(),
                productName,
                total,
                status
        );
    }
}