package net.chamman.shoppingmall_admin.domain.productVariant;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.chamman.shoppingmall_admin.domain.product.Product;
import net.chamman.shoppingmall_admin.domain.productImage.ProductImage;
import net.chamman.shoppingmall_admin.domain.productImage.ProductImage.ImageType;
import net.chamman.shoppingmall_admin.domain.productImage.ProductImageService;
import net.chamman.shoppingmall_admin.domain.productImage.dto.ProductImageUploadEvent;
import net.chamman.shoppingmall_admin.domain.productVariant.ProductVariant.ProductVariantStatus;
import net.chamman.shoppingmall_admin.domain.productVariant.dto.ProductVariantCreateRequestDto;
import net.chamman.shoppingmall_admin.domain.productVariant.dto.ProductVariantResponseDto;
import net.chamman.shoppingmall_admin.domain.productVariant.dto.ProductVariantSaleInfoRequestDto;
import net.chamman.shoppingmall_admin.domain.productVariant.dto.ProductVariantSaleInfoResponseDto;
import net.chamman.shoppingmall_admin.domain.productVariant.dto.ProductVariantUpdateRequestDto;
import net.chamman.shoppingmall_admin.exception.domain.productImage.ProductImageIllegalException;
import net.chamman.shoppingmall_admin.exception.domain.productVariant.ProductVariantIntegrityException;
import net.chamman.shoppingmall_admin.exception.infra.file.FileIllegalException;
import net.chamman.shoppingmall_admin.security.obfuscation.Obfuscator;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductVariantService {

    private final ProductVariantRepository productVariantRepository;
    private final ProductImageService productImageService;
    private final ApplicationEventPublisher eventPublisher;
    private final Obfuscator obfuscator;

    /**
     * 상품 생성
     */
    @Transactional
    public ProductVariantResponseDto createProductVariant(Product product, ProductVariantCreateRequestDto dto) {
        log.debug("* 상품 옵션 생성 시작.");
    
        ProductVariant variant = ProductVariant.builder()
                .product(product) 
                .name(dto.name())
                .code(dto.code())
                .sellingPrice(dto.saleInfo().sellingPrice())
                .costPrice(dto.saleInfo().costPrice())
                .stockQuantity(dto.saleInfo().stockQuantity())
                .status(ProductVariantStatus.DRAFT)
                .build();
        product.addProductVariant(variant);
        
        List<ProductImage> imagesToPublish = new ArrayList<>();
        List<MultipartFile> imageFiles = new ArrayList<>();
        
        // PENDING 상태로 엔티티만 생성
        ProductImage mainImage = productImageService.createPendingMainImage(variant);
        variant.addImage(mainImage);
        imagesToPublish.add(mainImage);
        imageFiles.add(dto.mainImage());

        for (MultipartFile file : dto.detailImages()) {
            ProductImage detailImage =  productImageService.createPendingDetailImage(variant);
            variant.addImage(detailImage);
            imagesToPublish.add(detailImage);
            imageFiles.add(file);
        }
        
        for (MultipartFile file : dto.contentImages()) {
            ProductImage contentImage =  productImageService.createPendingContentImage(variant);
            variant.addImage(contentImage);
            imagesToPublish.add(contentImage);
            imageFiles.add(file);
        }
            
        productVariantRepository.save(variant);
        
        try {
        	for (int i = 0; i < imagesToPublish.size(); i++) {
        		eventPublisher.publishEvent(ProductImageUploadEvent.of(imagesToPublish.get(i), imageFiles.get(i)));
            }
        } catch (IOException e) {
            throw new FileIllegalException("파일을 읽는 중 오류 발생", e);
        }

        log.info("* 상품 옵션 생성 완료.");
        return ProductVariantResponseDto.fromEntity(variant, obfuscator);
    }
    
    public ProductVariant findProductVariant(Long productVariantId) {
    	return productVariantRepository.findById(obfuscator.deobfuscate(productVariantId))
    			.orElseThrow(()->new ProductVariantIntegrityException("상품 옵션을 찾을 수 없습니다. ID: " + obfuscator.deobfuscate(productVariantId)));
    }
    
    public ProductVariantResponseDto getProductVariant(Long productVariantId) {
    	ProductVariant pv = findProductVariant(productVariantId);
    	return ProductVariantResponseDto.fromEntity(pv, obfuscator);
    }
    
    @Transactional
    public ProductVariantSaleInfoResponseDto putProductVariantSaleInfo(Long productVariantId, ProductVariantSaleInfoRequestDto dto) {
    	
    	ProductVariant variant = findProductVariant(productVariantId);
    	
    	variant.modifySaleInfo(dto.productVariantStatus(), dto.sellingPrice(), dto.costPrice(), dto.stockQuantity());
    	
    	return ProductVariantSaleInfoResponseDto.fromEntity(variant);
    }
    
    @Transactional
	public void putProductVariants(ProductVariant variant, ProductVariantUpdateRequestDto dto) {
		
		variant.modifyName(dto.name(), dto.code());
		variant.modifySaleInfo(
				dto.saleInfo().productVariantStatus(),
				dto.saleInfo().sellingPrice(),
				dto.saleInfo().costPrice(),
				dto.saleInfo().stockQuantity());
		
		// 기존 이미지 맵 생성
		Map<Long, ProductImage> imageMap = variant.getImages().stream()
		        .collect(Collectors.toMap(ProductImage::getId, img -> img));

		List<Long> deletedImageIds = dto.deletedImages().stream()
		        .map(id -> obfuscator.deobfuscate(id))
		        .toList();
		        
		for (Long deletedId : deletedImageIds) {
		    ProductImage imageToDelete = imageMap.get(deletedId);
		    if (imageToDelete != null) {
		        imageToDelete.softDelete();
		        imageMap.remove(deletedId);
		    }
		}

		int currentMainCount = 0;
        int currentDetailCount = 0;
        int currentContentCount = 0;
        for (ProductImage img : imageMap.values()) {
        	if (img.getImageType() == ImageType.MAIN) currentMainCount++;
            if (img.getImageType() == ImageType.DETAIL) currentDetailCount++;
            if (img.getImageType() == ImageType.CONTENT) currentContentCount++;
        }

        int newMainCount = (dto.mainImage() != null) ? 1 : 0;
        int newDetailCount = (dto.detailImages() != null) ? dto.detailImages().size() : 0;
        int newContentCount = (dto.contentImages() != null) ? dto.contentImages().size() : 0;

        if (currentMainCount + newMainCount > 1) {
            throw new ProductImageIllegalException("등록 가능한 메인 이미지 갯수(1개)를 초과합니다.");
        }
        if (currentDetailCount + newDetailCount > 10) {
            throw new ProductImageIllegalException("등록 가능한 상품 디테일 이미지 갯수(10개)를 초과합니다.");
        }
        if (currentContentCount + newContentCount > 20) {
            throw new ProductImageIllegalException("등록 가능한 상품 내용 이미지 갯수(20개)를 초과합니다.");
        }
        
        List<ProductImage> imagesToPublish = new ArrayList<>();
        List<MultipartFile> imageFiles = new ArrayList<>();
        
		if (dto.mainImage() != null && !dto.mainImage().isEmpty()) {
			ProductImage newMainImage = productImageService.createPendingMainImage(variant);
		    variant.addImage(newMainImage);
            imagesToPublish.add(newMainImage);
            imageFiles.add(dto.mainImage());

		}
		
		if (newDetailCount > 0) {
    		for(MultipartFile file : dto.detailImages()) {
		        ProductImage newDetailImage = productImageService.createPendingDetailImage(variant);
		        variant.addImage(newDetailImage);
	            imagesToPublish.add(newDetailImage);
	            imageFiles.add(file);
    		}
		}
        
		if (newContentCount > 0) {
    		for(MultipartFile file : dto.contentImages()) {
				ProductImage newContentImage = productImageService.createPendingContentImage(variant);
				variant.addImage(newContentImage);
	            imagesToPublish.add(newContentImage);
	            imageFiles.add(file);
			}
		}
		
        productVariantRepository.save(variant);

		try {
        	for (int i = 0; i < imagesToPublish.size(); i++) {
        		eventPublisher.publishEvent(ProductImageUploadEvent.of(imagesToPublish.get(i), imageFiles.get(i)));
            }
        } catch (IOException e) {
             throw new FileIllegalException("파일을 읽는 중 오류 발생", e);
        }
	}
    
    @Transactional
    public void deleteProductVariant(Long productVariantId) {
    	ProductVariant productVariant = findProductVariant(productVariantId);
    	productVariant.softDelete();
    }
    
}