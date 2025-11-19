package net.chamman.shoppingmall_admin.domain.shipment.dto;

import jakarta.validation.constraints.NotBlank;

public record ShipmentRequestDto(
		
		@NotBlank(message = "택배사명은 필수입니다.")
	    String shippingCompany,

	    @NotBlank(message = "운송장 번호는 필수입니다.")
	    String trackingNumber
	    
	    ) {

}
