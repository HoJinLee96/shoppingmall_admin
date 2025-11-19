package net.chamman.shoppingmall_admin.domain.productVariant.dto;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record ProductVariantUpdateRequestDto(
		
		@NotBlank
		@Positive
		Long productVariantId,
		
		@NotBlank(message = "옵션명은 필수입니다.")
        @Size(max = 50, message = "옵션명은 50자를 넘을 수 없습니다.")
        String name,

        @NotBlank(message = "상품 코드는 필수입니다.")
        @Size(max = 50, message = "상품 코드는 50자를 넘을 수 없습니다.")
        String code,

		MultipartFile mainImage,

        List<MultipartFile> detailImages,
        
        List<MultipartFile> contentImages,
        
        List<Long> deletedImages,
        
        @Valid
        ProductVariantSaleInfoRequestDto saleInfo

		) {

}
