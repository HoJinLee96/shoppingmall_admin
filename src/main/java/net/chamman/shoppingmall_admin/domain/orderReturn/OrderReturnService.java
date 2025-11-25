package net.chamman.shoppingmall_admin.domain.orderReturn;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import net.chamman.shoppingmall_admin.domain.orderReturn.dto.OrderReturnResponseDto;
import net.chamman.shoppingmall_admin.domain.returnPayment.ReturnPayment;
import net.chamman.shoppingmall_admin.domain.returnPayment.ReturnPaymentService;
import net.chamman.shoppingmall_admin.domain.shipment.Shipment;
import net.chamman.shoppingmall_admin.domain.shipment.dto.ShipmentRequestDto;
import net.chamman.shoppingmall_admin.exception.domain.orderReturn.OrderReturnIntegrityException;
import net.chamman.shoppingmall_admin.security.obfuscation.Obfuscator;

@Service
@RequiredArgsConstructor
public class OrderReturnService {

	private final OrderReturnRepository orderReturnRepository;
	private final ReturnPaymentService returnPaymentService;
	private final Obfuscator obfuscator;
	
	private static final String MANUAL_ARRIVED_SHIPPING_COMPANY = "직접운송";
	private static final String MANUAL_ARRIVED_SHIPPING_TRACKING_NUMBER = "777777777777";

	public OrderReturn findOrderReturnById(Long orderReturnId) {
		return orderReturnRepository.findById(obfuscator.deobfuscate(orderReturnId)).orElseThrow(
				() -> new OrderReturnIntegrityException("반품 정보를 찾을 수 없습니다. ID: " + obfuscator.deobfuscate(orderReturnId)));
	}
	
	/**
	 * 반품 메모 수정
	 */
	@Transactional
	public OrderReturnResponseDto modifyMemo(Long orderReturnId, String memo) {
		
		OrderReturn orderReturn = findOrderReturnById(orderReturnId);
		
		orderReturn.modifyMemo(memo);
		
		return OrderReturnResponseDto.fromEntity(orderReturn, obfuscator);
	}
	
	/**
	 * 반품 운송장 정보 입력
	 */
	@Transactional
	public OrderReturnResponseDto shipmentStartOrderReturn(Long orderReturnId, ShipmentRequestDto dto) {
		
		OrderReturn orderReturn = findOrderReturnById(orderReturnId);
		
		Shipment shipment= createShipmentFromDto(dto);
		
		orderReturn.shipmentStart(shipment);
		
		return OrderReturnResponseDto.fromEntity(orderReturn, obfuscator);
	}
	
	/**
	 * 반품 운송장 정보 수정
	 */
	@Transactional
	public OrderReturnResponseDto shipmentUpdateOrderReturn(Long orderReturnId, ShipmentRequestDto dto) {
		
		OrderReturn orderReturn = findOrderReturnById(orderReturnId);
		
		Shipment shipment= createShipmentFromDto(dto);
		
		orderReturn.shipmentUpdate(shipment);
		
		return OrderReturnResponseDto.fromEntity(orderReturn, obfuscator);
	}
	
	/**
	 * 수동 반품 입고 완료
	 */
	@Transactional
	public OrderReturnResponseDto manualArrivedOrderReturn(Long orderReturnId) {
		
		OrderReturn orderReturn = findOrderReturnById(orderReturnId);
		
		if(orderReturn.getShipment() == null) {
			Shipment shipment = Shipment.builder()
					.shippingCompany(MANUAL_ARRIVED_SHIPPING_COMPANY)
					.trackingNumber(MANUAL_ARRIVED_SHIPPING_TRACKING_NUMBER)
					.build();
			
			orderReturn.shipmentStart(shipment);
		}

		orderReturn.arrived();
		
		return OrderReturnResponseDto.fromEntity(orderReturn, obfuscator);
	}

	/**
	 * 반품 요청 환불 승인
	 */
	@Transactional
	public OrderReturnResponseDto refundOrderReturn(Long orderReturnId) {
		
		OrderReturn orderReturn = findOrderReturnById(orderReturnId);
		
		// 1. 반품 공제 금액 제외한 금액 환불 외부 API 요청
		ReturnPayment returnPayment = returnPaymentService.refundOrderReturn(orderReturn);
		
		// 2. OrderReturn의 returnPayment 추가, 상태 반품 완료로 변경.
		orderReturn.refund(returnPayment);
		
		// 3. ProductVariant 반품 갯수 증가
		orderReturn.getOrderItem().getProductVariant().stockIncreaseByReturnCompleted(orderReturn.getReturnCount());

		return OrderReturnResponseDto.fromEntity(orderReturn, obfuscator);
	}
	
	/**
	 * 반품 반려
	 */
	@Transactional
	public OrderReturnResponseDto unableOrderReturn(Long orderReturnId) {
		
		OrderReturn orderReturn = findOrderReturnById(orderReturnId);
		
		orderReturn.unable();
		
		return OrderReturnResponseDto.fromEntity(orderReturn, obfuscator);
	}
	
	private Shipment createShipmentFromDto(ShipmentRequestDto dto) {
	    return Shipment.builder()
	            .shippingCompany(dto.shippingCompany())
	            .trackingNumber(dto.trackingNumber())
	            .build();
	}
}
