package net.chamman.shoppingmall_admin.domain.order.dto;

import java.util.Map;

import net.chamman.shoppingmall_admin.domain.shipment.dto.ShipmentRequestDto;

public record OrderCancelReturnRequestDto(
		
		Long orderId,
		Map<Long, ShipmentRequestDto> orderItems
		) {

}
