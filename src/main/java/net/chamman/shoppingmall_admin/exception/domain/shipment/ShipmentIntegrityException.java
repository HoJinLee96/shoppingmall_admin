package net.chamman.shoppingmall_admin.exception.domain.shipment;

import net.chamman.shoppingmall_admin.exception.IntegrityException;

public class ShipmentIntegrityException extends IntegrityException{
	
	public ShipmentIntegrityException(Exception e) {
		super(e);
	}

	public ShipmentIntegrityException(String message, Exception e) {
		super(message, e);
	}

	public ShipmentIntegrityException(String message) {
		super(message);
	}

	public ShipmentIntegrityException() {
		super();
	}
	
}
