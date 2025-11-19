package net.chamman.shoppingmall_admin.domain.productCategory;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.chamman.shoppingmall_admin.domain.productCategory.dto.ProductCategoryCreateRequestDto;
import net.chamman.shoppingmall_admin.domain.productCategory.dto.ProductCategoryResponseDto;
import net.chamman.shoppingmall_admin.domain.productCategory.dto.ProductCategoryUpdateRequestDto;
import net.chamman.shoppingmall_admin.exception.HttpStatusCode;
import net.chamman.shoppingmall_admin.support.util.ApiResponseDto;
import net.chamman.shoppingmall_admin.support.util.ApiResponseFactory;

@Tag(name = "ProductController", description = "상품 관리 API")
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/private/product/category")
public class ProductCategoryController {

	private final ProductCategoryService productCategoryService;
	private final ApiResponseFactory apiResponseFactory;

	@PostMapping("/create")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public ResponseEntity<ApiResponseDto<Void>> createProductCategory(
			@Valid @RequestBody ProductCategoryCreateRequestDto dto) {
		
		productCategoryService.createProductCategory(dto);
		
		return ResponseEntity.ok(apiResponseFactory.success(HttpStatusCode.CREATE_SUCCESS));
	}
	
    @GetMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
	public ResponseEntity<ApiResponseDto<List<ProductCategoryResponseDto>>> getProductCategoryTree() {

		List<ProductCategoryResponseDto> result = productCategoryService.getProductCategoryTree();
		
		return ResponseEntity.ok(apiResponseFactory.success(HttpStatusCode.READ_SUCCESS, result));
	}
    
	@PatchMapping("/{productCategoryId}")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public ResponseEntity<ApiResponseDto<Void>> updateProductCategory(
			@PathVariable Long productCategoryId,
			@Valid @RequestBody ProductCategoryUpdateRequestDto dto) {
		
		productCategoryService.updateProductCategory(productCategoryId, dto);
		
		return ResponseEntity.ok(apiResponseFactory.success(HttpStatusCode.UPDATE_SUCCESS));
	}
	
	@DeleteMapping("/{productCategoryId}")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public ResponseEntity<ApiResponseDto<Void>> updateProductCategory(
			@PathVariable Long productCategoryId) {
		
		productCategoryService.deleteProductCategory(productCategoryId);
		
		return ResponseEntity.ok(apiResponseFactory.success(HttpStatusCode.DELETE_SUCCESS));
	}
}
