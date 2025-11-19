package net.chamman.shoppingmall_admin.domain.product.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import lombok.Builder;
import net.chamman.shoppingmall_admin.domain.product.Product;
import net.chamman.shoppingmall_admin.domain.productVariant.dto.ProductVariantResponseDto;
import net.chamman.shoppingmall_admin.security.obfuscation.Obfuscator;

@Builder
public record ProductResponseDto(
        Long productId,
        String categoryName,
        String displayName,
        String internalName,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        List<ProductVariantResponseDto> variants
) {
    public static ProductResponseDto fromEntity(Product product, Obfuscator obfuscator) {
        return ProductResponseDto.builder()
                .productId(obfuscator.obfuscate(product.getId()))
                .categoryName(product.getProductCategory() != null ? product.getProductCategory().getName() : null)
                .displayName(product.getDisplayName())
                .internalName(product.getInternalName())
                .createdAt(product.getCreatedAt())
                .updatedAt(product.getUpdatedAt())
                .variants(product.getProductVariants().stream()
                        .map(v->ProductVariantResponseDto.fromEntity(v, obfuscator))
                        .collect(Collectors.toList()))
                .build();
    }
}