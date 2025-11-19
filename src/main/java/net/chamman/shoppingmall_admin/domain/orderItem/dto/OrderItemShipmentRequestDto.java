package net.chamman.shoppingmall_admin.domain.orderItem.dto;

import net.chamman.shoppingmall_admin.domain.shipment.dto.ShipmentRequestDto;

public record OrderItemShipmentRequestDto(
		
		Long orderItemId,
		ShipmentRequestDto shipmentRequestDto
		
		) {

}
