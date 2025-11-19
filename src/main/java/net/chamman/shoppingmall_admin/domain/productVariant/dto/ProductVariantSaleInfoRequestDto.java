package net.chamman.shoppingmall_admin.domain.productVariant.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import net.chamman.shoppingmall_admin.domain.productVariant.ProductVariant.ProductVariantStatus;

public record ProductVariantSaleInfoRequestDto(
		
		@NotNull
		ProductVariantStatus productVariantStatus,
		
        @Positive(message = "판매 가격은 0 초과이어야 합니다.")
        int sellingPrice,

        @Positive(message = "소비자 가격은 0 초과이어야 합니다.")
        int costPrice,

        @PositiveOrZero(message = "재고 수량은 0 이상이어야 합니다.")
        int stockQuantity

		) {

}
