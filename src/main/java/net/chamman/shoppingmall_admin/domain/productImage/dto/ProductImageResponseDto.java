package net.chamman.shoppingmall_admin.domain.productImage.dto;

import lombok.Builder;
import net.chamman.shoppingmall_admin.domain.productImage.ProductImage;
import net.chamman.shoppingmall_admin.security.obfuscation.Obfuscator;

@Builder
public record ProductImageResponseDto(
        Long productImageId,
        String imagePath
) {
    public static ProductImageResponseDto fromEntity(ProductImage productImg, Obfuscator obfuscator) {
        return ProductImageResponseDto.builder()
                .productImageId(obfuscator.obfuscate(productImg.getId()))
                .imagePath(productImg.getImagePath())
                .build();
    }
}