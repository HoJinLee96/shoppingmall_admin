package net.chamman.shoppingmall_admin.domain.order.dto;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

import net.chamman.shoppingmall_admin.domain.orderItem.OrderItem.OrderItemStatus;

public record OrderSearchCondition(
		
		@DateTimeFormat(pattern = "yyyy-MM-dd")
	    LocalDate startDate,

	    // 검색 기간 (종료일)
	    @DateTimeFormat(pattern = "yyyy-MM-dd")
	    LocalDate endDate,

	    // 주문 상태 (PAYMENT_COMPLETED, PREPARING 등)
	    OrderItemStatus orderItemStatus,

	    // 검색어 타입 (어떤 것을 검색할지)
	    String searchType, // "ORDER_NUMBER", "MEMBER_NAME", "PRODUCT_NAME"
	    
	    // 검색 키워드
	    String keyword
		
		) {

}
