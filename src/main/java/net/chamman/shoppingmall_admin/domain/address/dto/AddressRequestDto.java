package net.chamman.shoppingmall_admin.domain.address.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record AddressRequestDto(
		
    @NotBlank(message = "validation.address.sender.required")
    @Size(max = 20, message = "validation.address.sender.length")
    String sender,
    
    @NotBlank(message = "validation.address.recipient.required")
    @Size(max = 20, message = "validation.address.recipient.length")
    String recipient,
    
    @NotBlank(message = "validation.address.recipientPhone.required")
    @Size(max = 20, message = "validation.address.recipientPhone.length")
    String recipientPhone,
    
    @NotBlank(message = "validation.address.postcode.required")
    @Size(max = 10, message = "validation.address.invalid")
    String postcode,
    
    @NotBlank(message = "validation.address.main_address.required")
    @Size(max = 250, message = "validation.address.invalid")
    String mainAddress,
    
    @NotBlank(message = "validation.address.detail_address.required")
    @Size(max = 50, message = "validation.address.invalid")
    String detailAddress,
    
    @NotBlank(message = "validation.address.shippingMemo.required")
    @Size(max = 50, message = "validation.address.shippingMemo.length")
    String shippingMemo
    ) {

//  public Address toEntity(Member member) {
//    return Address.builder()
//        .member(member)
//        .sender(sender)
//        .recipient(recipient)
//        .recipientPhone(recipientPhone)
//        .postcode(postcode)
//        .mainAddress(mainAddress)
//        .detailAddress(detailAddress)
//        .shippingMemo(shippingMemo)
//        .build();
//}
}
