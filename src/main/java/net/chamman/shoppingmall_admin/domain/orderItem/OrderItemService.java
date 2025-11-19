package net.chamman.shoppingmall_admin.domain.orderItem;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.chamman.shoppingmall_admin.domain.coupon.MemberCoupon;
import net.chamman.shoppingmall_admin.domain.order.Order;
import net.chamman.shoppingmall_admin.domain.orderItem.OrderItem.OrderItemStatus;
import net.chamman.shoppingmall_admin.domain.orderItem.dto.OrderItemShipmentRequestDto;
import net.chamman.shoppingmall_admin.domain.orderReturn.OrderReturn;
import net.chamman.shoppingmall_admin.domain.orderReturn.OrderReturnService;
import net.chamman.shoppingmall_admin.domain.shipment.Shipment;
import net.chamman.shoppingmall_admin.domain.shipment.dto.ShipmentRequestDto;
import net.chamman.shoppingmall_admin.exception.domain.order.OrderItemIllegalException;
import net.chamman.shoppingmall_admin.exception.domain.order.OrderItemIntegrityException;
import net.chamman.shoppingmall_admin.security.obfuscation.Obfuscator;

@Slf4j
@RequiredArgsConstructor
@Service
public class OrderItemService {

	private final OrderItemRepository orderItemRepository;
	private final OrderReturnService orderReturnService;
	private final Obfuscator obfuscator;

	public OrderItem findOrderItemById(Long orderItemId) {
		return orderItemRepository.findById(obfuscator.deobfuscate(orderItemId))
		        .orElseThrow(() -> new OrderItemIntegrityException("주문 정보를 찾을 수 없습니다. ID: " + obfuscator.deobfuscate(orderItemId)));
	}
	
	/**
	 * 결제완료 -> 상품준비중 / 주문 수락
	 */
	@Transactional
	public void acceptOrder(Order order) {

    	for(OrderItem orderItem : order.getOrderItems()) {
    		orderItem.updateToPreparing();
        }
    	
	}
	
	/**
	 * 결제완료 -> 상품준비중 / 주문 수락
	 */
	@Transactional
	public void acceptOrders(List<Order> orders) {
		
    	for(Order order : orders) {
    		List<OrderItem> orderItems = order.getOrderItems();
    		for(OrderItem orderItem : orderItems) {
    			orderItem.updateToPreparing();
    		}
    	}
		
	}
	
	/**
	 * 상품준비중 -> 배송중 / 배송 정보 입력
	 */
	@Transactional
	public void shipmentStartOrderItem(Long orderItemId, ShipmentRequestDto dto) {

		OrderItem orderItem = findOrderItemById(orderItemId);

        if (orderItem.getOrderItemStatus() != OrderItemStatus.PREPARING) {
            throw new OrderItemIllegalException("상품 준비중 상태가 아니면 배송 처리를 할 수 없습니다.");
        }

		if (orderItem.getShipment() != null) {
			throw new OrderItemIllegalException("이미 운송장 정보가 등록되어 있습니다.");
		}

		// 1. Shipment 엔티티 생성 및 저장
		Shipment shipment = buildShipment(dto);

		// 2. OrderItem에 Shipment 연결
		orderItem.shipmentStart(shipment);
		
		log.debug("* 운송장 등록 및 배송 시작. OrderItemID: [{}], Tracking: [{}]", orderItemId, dto.trackingNumber());
	}
	
	/**
	 * 상품준비중 -> 배송중 / 배송 정보 입력
	 */
	@Transactional
	public void shipmentStartOrderItems(List<OrderItemShipmentRequestDto> dtoList) {
		
		for(OrderItemShipmentRequestDto dto : dtoList) {
			shipmentStartOrderItem(dto.orderItemId(), dto.shipmentRequestDto());
		}
	}
	
	/**
	 * 취소 요청 -> 취소 완료 / 취소 상품 갯수 및 쿠폰 복원
	 */
	@Transactional
	public void cancelCompleteOrder(Order order) {
		
    	for(OrderItem orderItem : order.getOrderItems()) {
    		
    		orderItem.updateToCancelCompleted();
    		
    		// 1. 취소 완료된 상품 옵션 갯수 증가
    		orderItem.getProductVariant().stockIncreaseByCancelCompleted(orderItem.getCount());
    		
    		// 2. 사용된 쿠폰 복원
            MemberCoupon coupon = orderItem.getMemberCoupon();
            if (coupon != null) {
                coupon.restore(); // 쿠폰 상태를 ACTIVE로 변경
                log.info("* 주문 취소로 쿠폰 복원. MemberCoupon ID: {}", coupon.getId());
            }
        }
	}
	
	/**
	 * 취소 요청 -> 이미 출고 / 취소 요청 반려, 반품 생성
	 */
	@Transactional
	public void cancelReturnOrder(Map<Long, ShipmentRequestDto> orderItems) {

		for(Long id : orderItems.keySet()) {
			OrderItem orderItem = findOrderItemById(id);
			
			if (orderItem.getOrderItemStatus() != OrderItemStatus.CANCEL_REQUESTED) {
				throw new OrderItemIllegalException("취소 요청 상태가 아니면 이미 출고를 할 수 없습니다.");
			}
			
			if (orderItem.getShipment() != null) {
				throw new OrderItemIllegalException("이미 운송장 정보가 등록되어 있습니다.");
			}
			
			// 1. Shipment 엔티티 생성 및 저장
			Shipment shipment = buildShipment(orderItems.get(id));
			
			// 2. OrderItem에 Shipment 연결
			orderItem.cancelReturn(shipment);
			
			// 3. OrderReturn 생성
			OrderReturn orderReturn = orderReturnService.createReturnByCancelReturn(orderItem);
			orderItem.cancelReturn(orderReturn);
			
			log.debug("* 취소 반려 진행. 운송장 등록, 배송 시작, 반품 생성.");
		}
		
	}
	
	private Shipment buildShipment(ShipmentRequestDto dto) {
		return Shipment.builder()
				.shippingCompany(dto.shippingCompany())
				.trackingNumber(dto.trackingNumber())
				.build();
	}
}
