package net.chamman.shoppingmall_admin.domain.orderReturn.dto;

import java.util.List;

import net.chamman.shoppingmall_admin.domain.address.dto.AddressResponseDto;
import net.chamman.shoppingmall_admin.domain.orderReturn.OrderReturn;
import net.chamman.shoppingmall_admin.domain.returnPayment.dto.ReturnPaymentResponseDto;
import net.chamman.shoppingmall_admin.domain.shipment.dto.ShipmentResponseDto;
import net.chamman.shoppingmall_admin.security.obfuscation.Obfuscator;

public record OrderReturnResponseDto(
		
		long orderReturnId,
		AddressResponseDto addressInfo,
		ShipmentResponseDto shipmentInfo,
		int returnCount,
		String returnReason,
		String orderReturnStatus,
		List<ReturnPaymentResponseDto> returnPaymentInfo
		
		) {

	public static OrderReturnResponseDto fromEntity(OrderReturn orderReturn, Obfuscator obfuscator) {
		List<ReturnPaymentResponseDto> returnPaymentInfo = 
				orderReturn.getReturnPayment().stream().map(rp->ReturnPaymentResponseDto.fromEntity(rp, obfuscator)).toList();

		AddressResponseDto addressInfo = 
				AddressResponseDto.fromEntity(orderReturn.getAddress(), obfuscator);
				
		ShipmentResponseDto shipmentInfo = 
				ShipmentResponseDto.fromEntity(orderReturn.getShipment(), obfuscator);
		
		return new OrderReturnResponseDto(
				obfuscator.obfuscate(orderReturn.getId()),
				addressInfo,
				shipmentInfo,
				orderReturn.getReturnCount(),
				orderReturn.getReturnReason().getLabel(),
				orderReturn.getOrderReturnStatus().getLabel(),
				returnPaymentInfo
				);
	}
}
