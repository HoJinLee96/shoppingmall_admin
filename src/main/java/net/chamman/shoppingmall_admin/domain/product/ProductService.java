package net.chamman.shoppingmall_admin.domain.product;

import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.chamman.shoppingmall_admin.domain.product.dto.ProductCreateRequestDto;
import net.chamman.shoppingmall_admin.domain.product.dto.ProductResponseDto;
import net.chamman.shoppingmall_admin.domain.product.dto.ProductUpdateRequestDto;
import net.chamman.shoppingmall_admin.domain.productCategory.ProductCategory;
import net.chamman.shoppingmall_admin.domain.productCategory.ProductCategoryService;
import net.chamman.shoppingmall_admin.domain.productVariant.ProductVariant;
import net.chamman.shoppingmall_admin.domain.productVariant.ProductVariantService;
import net.chamman.shoppingmall_admin.domain.productVariant.dto.ProductVariantUpdateRequestDto;
import net.chamman.shoppingmall_admin.exception.domain.product.ProductIntegrityException;
import net.chamman.shoppingmall_admin.exception.domain.product.variant.ProductVariantIllegalException;
import net.chamman.shoppingmall_admin.security.obfuscation.Obfuscator;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductCategoryService productCategoryService;
    private final ProductVariantService productVariantService;
    private final Obfuscator obfuscator;

    /**
     * 상품 생성
     */
	@Transactional
	public ProductResponseDto createProduct(ProductCreateRequestDto dto) {
		log.debug("* 상품 생성 시작.");

		ProductCategory category = productCategoryService.findProductCategory(dto.productCategoryId());

		Product product = Product.builder()
				.productCategory(category)
				.displayName(dto.displayName())
				.internalName(dto.internalName())
				.build();

		category.addProduct(product);

		dto.variants().forEach(variantDto -> productVariantService.createProductVariant(product, variantDto));

		Product savedProduct = productRepository.save(product);
		
		log.info("* 상품 생성 완료. productId: [{}]", savedProduct.getId());
		return ProductResponseDto.fromEntity(savedProduct, obfuscator);
		
	}

    /**
     * 상품 단건 조회 (Variant, Image 포함)
     */
    @Transactional(readOnly = true)
    public Product findProductById(Long productId) {
    	log.debug("* 상품 조회 시작. productId: [{}]", productId);
    	// fetch join 변경 고려
    	return productRepository.findById(obfuscator.deobfuscate(productId))
    			.orElseThrow(() -> new ProductIntegrityException("상품을 찾을 수 없습니다. ID: " + obfuscator.deobfuscate(productId)));
    }
    
//    @Transactional(readOnly = true)
//    public Product findByIdWithVariants(Long productId) {
//    	log.debug("* 상품 조회 시작. productId: [{}]", productId);
//        return productRepository.findByIdWithVariants(obfuscator.deobfuscate(productId))
//        		.orElseThrow(() -> new ProductIntegrityException("상품을 찾을 수 없습니다. ID: " + obfuscator.deobfuscate(productId)));
//    }
    
    /**
     * 상품 단건 조회 (Variant, Image 포함)
     */
    @Transactional(readOnly = true)
    public ProductResponseDto getProductById(Long productId) {
        log.debug("* 상품 조회 시작. productId: [{}]", productId);
        // fetch join 변경 고려
        Product product = findProductById(productId);
        
        return ProductResponseDto.fromEntity(product, obfuscator);
    }

    /**
     * 상품 목록 조회 (페이징)
     */
    @Transactional(readOnly = true)
    public Page<ProductResponseDto> getAllProducts(Pageable pageable) {
        log.debug("* 상품 목록 조회 시작. page: {}, size: {}", pageable.getPageNumber(), pageable.getPageSize());

        // fetch join 변경 고려
        Page<Product> productPage = productRepository.findAll(pageable);
        return productPage.map(p -> ProductResponseDto.fromEntity(p, obfuscator));
    }

    /**
     * 상품 정보 수정 (Product 기본 정보만)
     */
    @Transactional
    public void putProduct(Long productId, ProductUpdateRequestDto dto) {
        log.debug("* 상품 수정 시작. productId: [{}]", productId);
        
        Product product = findProductById(productId);
		ProductCategory category = productCategoryService.findProductCategory(dto.productCategoryId());

		// 카테고리 수정
        product.modifyProductCategory(category);
        
        // 노출상품명, 등록상품명 수정
        product.modifyName(dto.displayName(), dto.internalName());
        
        // 상품 옵션별 수정 (판매정보, 이미지)
    	Map<Long, ProductVariant> variantMap = 
    			product.getProductVariants().stream()
    			.collect(Collectors.toMap(ProductVariant::getId, v -> v));
    	
    	for(ProductVariantUpdateRequestDto variantDto : dto.variants()) {
    		
    		ProductVariant variant = variantMap.get(obfuscator.deobfuscate(variantDto.productVariantId()));
    		
    		if(variant == null) {
    			log.info("* 등록 되어 있지 않은 상품 옵션을 수정하려고 함. ProductVariantId : ", obfuscator.deobfuscate(variantDto.productVariantId()));
    			throw new ProductVariantIllegalException("등록 되어 있지 않은 상품 옵션을 수정하력고 함.");
    		}
    		
    		productVariantService.putProductVariants(variant, variantDto);
    	}
        
        // 새로운 상품 옵션 추가
		dto.newVariants().forEach(variantDto -> productVariantService.createProductVariant(product, variantDto));
		
        log.info("* 상품 수정 완료. productId: [{}]", productId);
    }

    /**
     * 상품 삭제 (Soft Delete)
     */
    @Transactional
    public void deleteProduct(Long productId) {
        log.debug("* 상품 삭제 시작.");
        
        Product product = findProductById(productId);
        
        product.softDelete();

        product.getProductVariants().forEach(variant -> {
            variant.softDelete();
            variant.getImages().forEach(img -> {
                 img.softDelete();
            });
        });

        log.info("* 상품 삭제 완료 (Soft Delete). productId: [{}]", obfuscator.deobfuscate(productId));
    }
}