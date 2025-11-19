package net.chamman.shoppingmall_admin.domain.productVariant.dto;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record ProductVariantCreateRequestDto(
		
		@NotBlank(message = "옵션명은 필수입니다.")
        @Size(max = 50, message = "옵션명은 50자를 넘을 수 없습니다.")
        String name,

        @NotBlank(message = "상품 코드는 필수입니다.")
        @Size(max = 50, message = "상품 코드는 50자를 넘을 수 없습니다.")
        String code, // 상품 코드 (Unique 제약조건 고려 필요)

        @NotNull
		MultipartFile mainImage,

		@NotEmpty
        List<MultipartFile> detailImages,
        
        @NotEmpty
        List<MultipartFile> contentImages,
        
        @Valid
        @NotNull
        ProductVariantSaleInfoRequestDto saleInfo

		) {

}
