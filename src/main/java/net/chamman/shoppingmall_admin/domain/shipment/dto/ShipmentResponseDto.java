package net.chamman.shoppingmall_admin.domain.shipment.dto;

import net.chamman.shoppingmall_admin.domain.shipment.Shipment;
import net.chamman.shoppingmall_admin.security.obfuscation.Obfuscator;

public record ShipmentResponseDto(
		
		long shipmentId,
		String shippingCompany,
		String trackingNumber
		
		) {

	public static ShipmentResponseDto fromEntity(Shipment shipment, Obfuscator obfuscator) {
		return new ShipmentResponseDto(
				obfuscator.obfuscate(shipment.getId()),
				shipment.getShippingCompany(),
				shipment.getTrackingNumber());
	}
}
