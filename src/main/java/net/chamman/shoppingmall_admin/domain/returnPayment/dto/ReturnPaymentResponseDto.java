package net.chamman.shoppingmall_admin.domain.returnPayment.dto;

import java.time.LocalDateTime;

import net.chamman.shoppingmall_admin.domain.payment.Payment.PaymentMethod;
import net.chamman.shoppingmall_admin.domain.payment.Payment.PaymentStatus;
import net.chamman.shoppingmall_admin.domain.returnPayment.ReturnPayment;
import net.chamman.shoppingmall_admin.security.obfuscation.Obfuscator;

public record ReturnPaymentResponseDto(
		
		Long returnPaymentId,
		Integer amount,
		PaymentMethod paymentMethod,
		PaymentStatus paymentStatus,
		LocalDateTime approvedAt
		
		) {
	
	public static ReturnPaymentResponseDto fromEntity(ReturnPayment returnPayment, Obfuscator obfuscator) {
		return new ReturnPaymentResponseDto(
				obfuscator.obfuscate(returnPayment.getId()), 
				returnPayment.getAmount(), 
				returnPayment.getPaymentMethod(), 
				returnPayment.getPaymentStatus(), 
				returnPayment.getApprovedAt());
	}


}
