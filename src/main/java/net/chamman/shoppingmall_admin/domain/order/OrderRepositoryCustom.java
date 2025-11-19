package net.chamman.shoppingmall_admin.domain.order;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import net.chamman.shoppingmall_admin.domain.order.dto.OrderSearchCondition;

public interface OrderRepositoryCustom {
	/**
     * 관리자용 주문 목록 동적 검색
     * @param condition 검색 조건
     * @param pageable 페이징 정보
     * @return 페이징된 주문 목록
     */
    Page<Order> searchOrders(OrderSearchCondition condition, Pageable pageable);
}