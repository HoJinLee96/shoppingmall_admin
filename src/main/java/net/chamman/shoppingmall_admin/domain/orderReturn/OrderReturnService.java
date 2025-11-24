package net.chamman.shoppingmall_admin.domain.orderReturn;

import java.util.Objects;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import net.chamman.shoppingmall_admin.domain.orderItem.OrderItem;
import net.chamman.shoppingmall_admin.domain.orderReturn.OrderReturn.OrderReturnStatus;
import net.chamman.shoppingmall_admin.domain.orderReturn.OrderReturn.ReturnReason;
import net.chamman.shoppingmall_admin.domain.orderReturn.dto.OrderReturnResponseDto;
import net.chamman.shoppingmall_admin.domain.returnPayment.ReturnPayment;
import net.chamman.shoppingmall_admin.domain.returnPayment.ReturnPaymentService;
import net.chamman.shoppingmall_admin.domain.shipment.Shipment;
import net.chamman.shoppingmall_admin.domain.shipment.dto.ShipmentRequestDto;
import net.chamman.shoppingmall_admin.exception.domain.order.OrderItemIntegrityException;
import net.chamman.shoppingmall_admin.exception.domain.order.OrderReturnIllegalException;
import net.chamman.shoppingmall_admin.security.obfuscation.Obfuscator;

@Service
@RequiredArgsConstructor
public class OrderReturnService {

	private final OrderReturnRepository orderReturnRepository;
	private final ReturnPaymentService returnPaymentService;
	private final Obfuscator obfuscator;

	public OrderReturn findOrderReturnById(Long orderReturnId) {
		return orderReturnRepository.findById(orderReturnId).orElseThrow(
				() -> new OrderItemIntegrityException("반품 정보를 찾을 수 없습니다. ID: " + obfuscator.deobfuscate(orderReturnId)));
	}
	
	/**
	 * 이미 출고로 인한 반품 생성
	 */
	@Transactional
	public OrderReturn createReturnByCancelReturn(OrderItem orderItem) {
		
			OrderReturn orderReturn = OrderReturn.builder()
					.orderItem(orderItem)
					.address(orderItem.getOrder().getAddress())
					.returnCount(orderItem.getCount())
					.returnReason(ReturnReason.SELLER_FAULT)
					.build();
		
		return orderReturn;
	}
	
	/**
	 * 반품 운송장 정보 입력
	 */
	@Transactional
	public OrderReturnResponseDto shipmentStartOrderReturn(Long orderReturnId, ShipmentRequestDto dto) {
		
		OrderReturn orderReturn = findOrderReturnById(orderReturnId);
		
		Shipment shipment= Shipment.builder()
				.shippingCompany(dto.shippingCompany())
				.trackingNumber(dto.trackingNumber())
				.build();
		
		orderReturn.shipmentStart(shipment);
		
		return OrderReturnResponseDto.fromEntity(orderReturn, obfuscator);
	}
	
	/**
	 * 반품 운송장 정보 수정
	 */
	@Transactional
	public OrderReturnResponseDto shipmentUpdateOrderReturn(Long orderReturnId, ShipmentRequestDto dto) {
		
		OrderReturn orderReturn = findOrderReturnById(orderReturnId);
		
		Shipment shipment= Shipment.builder()
				.shippingCompany(dto.shippingCompany())
				.trackingNumber(dto.trackingNumber())
				.build();
		
		orderReturn.shipmentUpdate(shipment);
		
		return OrderReturnResponseDto.fromEntity(orderReturn, obfuscator);
	}
	
	/**
	 * 수동 반품 입고 완료
	 */
	@Transactional
	public OrderReturnResponseDto arrivedOrderReturn(Long orderReturnId) {
		
		OrderReturn orderReturn = findOrderReturnById(orderReturnId);
		
		if(orderReturn.getShipment() == null) {
			Shipment shipment = Shipment.builder()
					.shippingCompany("직접운송")
					.trackingNumber("777777777777")
					.build();
			
			orderReturn.shipmentStart(shipment);
		}

		orderReturn.arrvied();
		
		return OrderReturnResponseDto.fromEntity(orderReturn, obfuscator);
	}

	/**
	 * 반품 요청 환불 승인
	 */
	@Transactional
	public OrderReturnResponseDto refundOrderReturn(Long orderReturnId) {
		
		OrderReturn orderReturn = findOrderReturnById(orderReturnId);
		
		if(!Objects.equals(OrderReturnStatus.RETURN_INSPECTING, orderReturn.getOrderReturnStatus())) {
			throw new OrderReturnIllegalException("반품 완료 및 환불 처리 실패. 반품 상품 상태가 검수중이 아님.");
		}
		
		// 1. 반품 공제 금액 제외한 금액 환불 외부 API 요청
		ReturnPayment returnPayment = returnPaymentService.refundOrderReturn(orderReturn);
		
		// 2. OrderReturn의 returnPayment 추가, 상태 반품 완료로 변경.
		orderReturn.refund(returnPayment);
		
		// 3. ProductVariant 반품 갯수 증가
		orderReturn.getOrderItem().getProductVariant().stockIncreaseByReturnCompleted(orderReturn.getReturnCount());

		return OrderReturnResponseDto.fromEntity(orderReturn, obfuscator);
	}
}
