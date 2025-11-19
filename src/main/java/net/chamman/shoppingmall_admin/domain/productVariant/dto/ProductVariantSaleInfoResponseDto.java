package net.chamman.shoppingmall_admin.domain.productVariant.dto;

import lombok.Builder;
import net.chamman.shoppingmall_admin.domain.productVariant.ProductVariant;
import net.chamman.shoppingmall_admin.domain.productVariant.ProductVariant.ProductVariantStatus;

@Builder
public record ProductVariantSaleInfoResponseDto(
		
		ProductVariantStatus productVariantStatus,
        int sellingPrice,
        int costPrice,
        int stockQuantity

		) {

    public static ProductVariantSaleInfoResponseDto fromEntity(ProductVariant variant) {
    	return ProductVariantSaleInfoResponseDto.builder()
    			.productVariantStatus(variant.getStatus())
    			.sellingPrice(variant.getSellingPrice())
    			.costPrice(variant.getCostPrice())
    			.stockQuantity(variant.getStockQuantity())
    			.build();
    }
    
}
