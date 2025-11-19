package net.chamman.shoppingmall_admin.domain.product;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
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
import net.chamman.shoppingmall_admin.domain.product.dto.ProductCreateRequestDto;
import net.chamman.shoppingmall_admin.domain.product.dto.ProductResponseDto;
import net.chamman.shoppingmall_admin.domain.product.dto.ProductUpdateRequestDto;
import net.chamman.shoppingmall_admin.exception.HttpStatusCode;
import net.chamman.shoppingmall_admin.security.principal.AdminDetails;
import net.chamman.shoppingmall_admin.support.util.ApiResponseDto;
import net.chamman.shoppingmall_admin.support.util.ApiResponseFactory;

@Tag(name = "ProductController", description = "상품 관리 API")
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/private/product")
public class ProductController {
	
	private final ProductService productService;
    private final ApiResponseFactory apiResponseFactory;

    @Operation(summary = "상품 생성 (관리자)", description = "새로운 상품과 옵션 정보를 등록합니다.")
    @SecurityRequirement(name = "X-Access-Token")
    @PostMapping("/create")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<ApiResponseDto<Void>> createProduct(
            @Valid @RequestBody ProductCreateRequestDto createDto) {
        log.debug("* 상품 생성 요청.");

        productService.createProduct(createDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(apiResponseFactory.success(HttpStatusCode.CREATE_SUCCESS));
    }

    @Operation(summary = "상품 상세 조회 (공개)", description = "특정 상품의 상세 정보를 조회합니다.")
    @GetMapping("/{productId}")
    public ResponseEntity<ApiResponseDto<ProductResponseDto>> getProduct(
            @PathVariable Long id) {
        log.debug("* 상품 상세 조회 요청.");
        
        ProductResponseDto productDto = productService.getProductById(id);

        return ResponseEntity.ok(apiResponseFactory.success(HttpStatusCode.READ_SUCCESS, productDto));
    }

    @Operation(summary = "상품 목록 조회 (공개)", description = "상품 목록을 페이지네이션하여 조회합니다.")
    @GetMapping
    public ResponseEntity<ApiResponseDto<Page<ProductResponseDto>>> getProducts(
            @PageableDefault(size = 10, sort = "createdAt,desc") Pageable pageable) { // 기본 페이지 크기, 정렬 설정

        log.debug("* 상품 목록 조회 요청. page: {}, size: {}", pageable.getPageNumber(), pageable.getPageSize());
        Page<ProductResponseDto> productPage = productService.getAllProducts(pageable);

        HttpStatusCode statusCode = productPage.isEmpty() ? HttpStatusCode.READ_SUCCESS_NO_DATA : HttpStatusCode.READ_SUCCESS;
        return ResponseEntity.ok(apiResponseFactory.success(statusCode, productPage));
    }

    @Operation(summary = "상품 정보 수정 (관리자)", description = "기존 상품의 정보를 수정합니다.")
    @SecurityRequirement(name = "X-Access-Token")
    @PutMapping("/{productId}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<ApiResponseDto<Void>> putProduct(
            @PathVariable Long productId,
            @Valid @RequestBody ProductUpdateRequestDto dto) {

        log.debug("* 상품 수정 요청.");
        productService.putProduct(productId, dto);

        return ResponseEntity.ok(apiResponseFactory.success(HttpStatusCode.UPDATE_SUCCESS));
    }

    @Operation(summary = "상품 삭제 (관리자)", description = "상품을 비활성화(Soft Delete) 처리합니다.")
    @SecurityRequirement(name = "X-Access-Token")
    @DeleteMapping("/{productId}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<ApiResponseDto<Void>> deleteProduct(
            @AuthenticationPrincipal AdminDetails adminDetails,
            @PathVariable Long productId) {

        log.debug("* 상품 삭제 요청.");
        productService.deleteProduct(productId);

        return ResponseEntity.ok(apiResponseFactory.success(HttpStatusCode.DELETE_SUCCESS));
    }
}
