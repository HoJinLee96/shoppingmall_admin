package net.chamman.shoppingmall_admin.domain.productImage.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.Builder;

@Builder
public record ProductImageRequestDto(
		
		@NotBlank
		@Positive
        Long productImageId
) {
}