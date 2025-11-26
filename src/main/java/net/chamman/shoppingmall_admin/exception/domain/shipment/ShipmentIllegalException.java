package net.chamman.shoppingmall_admin.exception.domain.shipment;

import net.chamman.shoppingmall_admin.exception.CustomException;
import net.chamman.shoppingmall_admin.exception.HttpStatusCode;

public class ShipmentIllegalException extends CustomException{

	public ShipmentIllegalException(Exception e) {
		super(HttpStatusCode.SHIPMENT_ILLEGAL, e);
	}

	public ShipmentIllegalException(String message, Exception e) {
		super(HttpStatusCode.SHIPMENT_ILLEGAL, message, e);
	}

	public ShipmentIllegalException(String message) {
		super(HttpStatusCode.SHIPMENT_ILLEGAL, message);
	}

	public ShipmentIllegalException() {
		super(HttpStatusCode.SHIPMENT_ILLEGAL);
	}
	
}
