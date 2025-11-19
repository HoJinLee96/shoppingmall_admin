package net.chamman.shoppingmall_admin.domain.product.dto;

import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import net.chamman.shoppingmall_admin.domain.productVariant.dto.ProductVariantCreateRequestDto;

public record ProductCreateRequestDto(
		
		@NotNull(message = "카테고리 ID는 필수입니다.")
        Long productCategoryId,

        @NotBlank(message = "전시용 상품명은 필수입니다.")
        @Size(max = 50, message = "전시용 상품명은 50자를 넘을 수 없습니다.")
        String displayName,

        @NotBlank(message = "내부 상품명은 필수입니다.")
        @Size(max = 50, message = "내부 상품명은 50자를 넘을 수 없습니다.")
        String internalName,

        @NotEmpty(message = "상품 옵션은 최소 1개 이상 필요합니다.")
        @Valid // 중첩된 객체 검증
        List<ProductVariantCreateRequestDto> variants
        
		) {

}
