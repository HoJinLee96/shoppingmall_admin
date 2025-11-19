package net.chamman.shoppingmall_admin.domain.address.dto;

import lombok.Builder;
import net.chamman.shoppingmall_admin.domain.address.Address;
import net.chamman.shoppingmall_admin.security.obfuscation.Obfuscator;

@Builder
public record AddressResponseDto(
    
    long addressId,
    String sender,
    String recipient,
    String recipientPhone,
    String postcode,
    String mainAddress,
    String detailAddress,
    String shippingMemo,
    boolean isPrimary
    
    ) {

  public static AddressResponseDto fromEntity(Address address, Obfuscator obfuscator) {
    return AddressResponseDto.builder()
    .addressId(obfuscator.obfuscate(address.getId()))
    .sender(address.getSender())
    .recipient(address.getRecipient())
    .recipientPhone(address.getRecipientPhone())
    .postcode(address.getPostcode())
    .mainAddress(address.getMainAddress())
    .detailAddress(address.getDetailAddress())
    .shippingMemo(address.getShippingMemo())
    .isPrimary(address.isPrimary())
    .build();
  }
}
