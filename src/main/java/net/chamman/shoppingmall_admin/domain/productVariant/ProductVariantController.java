package net.chamman.shoppingmall_admin.domain.productVariant;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.chamman.shoppingmall_admin.domain.productVariant.dto.ProductVariantSaleInfoRequestDto;
import net.chamman.shoppingmall_admin.domain.productVariant.dto.ProductVariantSaleInfoResponseDto;
import net.chamman.shoppingmall_admin.exception.HttpStatusCode;
import net.chamman.shoppingmall_admin.support.util.ApiResponseDto;
import net.chamman.shoppingmall_admin.support.util.ApiResponseFactory;

@Tag(name = "ProductVariantController", description = "상품 옵션 관리 API")
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ProductVariantController {
	
	private final ProductVariantService productVariantService ;
    private final ApiResponseFactory apiResponseFactory;

    @Operation(summary = "상품 옵션 판매 정보 수정", description = "기존 상품의 옵션 중 판매 정보를 수정합니다.")
    @SecurityRequirement(name = "X-Access-Token")
    @PutMapping("/private/product/variant/saleInfo/{productVariantId}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<ApiResponseDto<ProductVariantSaleInfoResponseDto>> putProductVariantSaleInfo(
            @PathVariable Long productVariantId,
            @Valid @RequestBody ProductVariantSaleInfoRequestDto dto) {

        log.info("* 상품 옵션 판매 정보 수정 요청. productVariantId: [{}]", productVariantId);
        ProductVariantSaleInfoResponseDto responseDto = productVariantService.putProductVariantSaleInfo(productVariantId, dto);

        return ResponseEntity.ok(apiResponseFactory.success(HttpStatusCode.UPDATE_SUCCESS, responseDto));
    }
    
    @Operation(summary = "상품 옵션 삭제", description = "기존 상품의 옵션을 삭제합니다.")
    @SecurityRequirement(name = "X-Access-Token")
    @DeleteMapping("/private/product/variant/{productVariantId}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<ApiResponseDto<Void>> deleteProductVariant(
    		@PathVariable Long productVariantId) {
    	
    	log.info("* 상품 옵션 삭제 요청. productVariantId: [{}]", productVariantId);
    	
    	productVariantService.deleteProductVariant(productVariantId);
    	
    	return ResponseEntity.status(HttpStatus.NO_CONTENT).body(apiResponseFactory.success(HttpStatusCode.DELETE_SUCCESS));
    }
    
}
