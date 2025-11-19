package net.chamman.shoppingmall_admin.domain.order;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.chamman.shoppingmall_admin.domain.order.dto.OrderCancelReturnRequestDto;
import net.chamman.shoppingmall_admin.domain.order.dto.OrderDetailResponseDto;
import net.chamman.shoppingmall_admin.domain.order.dto.OrderListResponseDto;
import net.chamman.shoppingmall_admin.domain.order.dto.OrderSearchCondition;
import net.chamman.shoppingmall_admin.domain.orderItem.OrderItem;
import net.chamman.shoppingmall_admin.domain.orderItem.OrderItemService;
import net.chamman.shoppingmall_admin.domain.orderPayment.OrderPaymentService;
import net.chamman.shoppingmall_admin.domain.orderReturn.OrderReturn;
import net.chamman.shoppingmall_admin.domain.orderReturn.OrderReturnService;
import net.chamman.shoppingmall_admin.exception.domain.order.OrderIllegalException;
import net.chamman.shoppingmall_admin.exception.domain.order.OrderIntegrityException;
import net.chamman.shoppingmall_admin.security.obfuscation.Obfuscator;

@Slf4j
@RequiredArgsConstructor
@Service
public class OrderService {

	private final OrderRepository orderRepository;
    private final OrderItemService orderItemService;
    private final OrderReturnService orderReturnService;
    private final OrderPaymentService orderPaymentService;
	private final Obfuscator obfuscator;

    @Transactional(readOnly = true)
	private Order findOrderById(Long orderId) {
		return orderRepository.findById(obfuscator.deobfuscate(orderId))
        .orElseThrow(() -> new OrderIntegrityException("주문 정보를 찾을 수 없습니다. ID: " + obfuscator.deobfuscate(orderId)));
	}
	
    @Transactional(readOnly = true)
    private Order findDetailsById(Long orderId) {
		return orderRepository.findByIdWithDetails(obfuscator.deobfuscate(orderId))
        .orElseThrow(() -> new OrderIntegrityException("주문 정보를 찾을 수 없습니다. ID: " + obfuscator.deobfuscate(orderId)));
	}

    /**
     * 주문 목록 조회 (페이징)
     */
    @Transactional(readOnly = true)
    public Page<OrderListResponseDto> getOrderList(OrderSearchCondition condition, Pageable pageable) {
    	
    	Page<Order> orderPage = orderRepository.searchOrders(condition, pageable);
    	
        return orderPage.map(order -> OrderListResponseDto.fromEntity(order, obfuscator));
    }

    /**
     * 주문 상세 조회
     */
    @Transactional(readOnly = true)
    public OrderDetailResponseDto getOrderDetail(Long orderId) {
        
        Order order = findDetailsById(orderId);
        
        return OrderDetailResponseDto.fromEntity(order, obfuscator);
    }
    
    /**
	 * 결제완료 -> 상품준비중 / 주문 수락
     */
    @Transactional
    public void acceptOrder(Long orderId) {
        
        Order order = findOrderById(orderId);
        
        orderItemService.acceptOrder(order);
    }
    
    /**
	 * 결제완료 -> 상품준비중 / 주문 수락
     */
    @Transactional
    public void acceptOrders(List<Long> orderIds) {
    	
    	List<Long> decodedOrderIds = orderIds.stream().map(id->obfuscator.deobfuscate(id)).toList();
    	
    	List<Order> orders = orderRepository.findAllById(decodedOrderIds);
    	
        orderItemService.acceptOrders(orders);

    }
    
    /**
     * 취소 요청 -> 취소 완료 주문 상태 변경
     */
    @Transactional
    public void cancelCompleteOrder(Long orderId) {
        
        Order order = findOrderById(orderId);
        
        // 1. OrderItem 취소 완료 상태 처리, 취소 상품 갯수 및 쿠폰 복원
        orderItemService.cancelCompleteOrder(order);
        
        // 2. OrderPayment 취소 처리
        orderPaymentService.cancelCompleteOrder(order);
        
    }
    
	/**
	 * 취소 요청 -> 이미 출고 / 취소 요청 반려, 반품 생성
	 */
	@Transactional
	public void cancelReturnOrder(OrderCancelReturnRequestDto dto) {
		
		// 1. Order 검증
		Order order = findOrderById(dto.orderId());
		
		List<Long> orderItemIds = order.getOrderItems().stream().map(orderItem -> orderItem.getId()).toList();
		List<Long> dtoOrderItemIds = dto.orderItems().keySet().stream().map(id->obfuscator.deobfuscate(id)).toList();
		
		if(orderItemIds.size() != dtoOrderItemIds.size()) {
			throw new OrderIllegalException("주문 옵션 갯수와 입력받은 데이터 갯수가 일치 하지 않음.");
		}
		for( Long id : dtoOrderItemIds) {
			if(!orderItemIds.contains(id)) {
				throw new OrderIllegalException("주문 옵션 데이터와 입력받은 데이터가 일치 하지 않음.");
			}
		}
		
		// 2. OrderItemService로 책임 이전
		orderItemService.cancelReturnOrder(dto.orderItems());

		log.debug("* 취소 반려 성공.");
	}
    
}