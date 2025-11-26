package net.chamman.shoppingmall_admin.domain.productImage;


import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.chamman.shoppingmall_admin.domain.productImage.ProductImage.ImageType;
import net.chamman.shoppingmall_admin.domain.productVariant.ProductVariant;
import net.chamman.shoppingmall_admin.exception.domain.productVariant.ProductVariantIntegrityException;
import net.chamman.shoppingmall_admin.security.obfuscation.Obfuscator;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductImageService {
	
	private final ProductImageRepository productImageRepository;
//	private final FileStorage fileStorage;
	private final Obfuscator obfuscator;
	
	@Transactional
    public ProductImage createPendingMainImage(ProductVariant productVariant) {
    	ProductImage productImage = ProductImage.builder()
    	    .productVariant(productVariant)
            .imagePath(null) //S3 경로는 아직 모름
    	    .imageType(ImageType.MAIN)
            .status(ProductImage.ImageStatus.PENDING_UPLOAD) //PENDING 상태
    	    .build();
        return productImage;
    }
	
	@Transactional
	public ProductImage createPendingDetailImage(ProductVariant productVariant) {
		ProductImage productImage = ProductImage.builder()
				.productVariant(productVariant)
                .imagePath(null)
				.imageType(ImageType.DETAIL)
                .status(ProductImage.ImageStatus.PENDING_UPLOAD)
				.build();
		return productImage;
	}
	
	@Transactional
	public ProductImage createPendingContentImage(ProductVariant productVariant) {
		ProductImage productImage = ProductImage.builder()
				.productVariant(productVariant)
                .imagePath(null)
				.imageType(ImageType.CONTENT)
                .status(ProductImage.ImageStatus.PENDING_UPLOAD)
				.build();
		return productImage;
	}
	
//	@Transactional
//    public ProductImage uploadMainImage(MultipartFile file, ProductVariant productVariant) {
//		
//    	String folderPath = "products/" + productVariant.getCode() + "/main";
//    	String imagePath = fileStorage.uploadFile(file, folderPath);
//
//    	ProductImage productImage = ProductImage.builder()
//    	.productVariant(productVariant)
//    	.imagePath(imagePath)
//    	.imageType(ImageType.MAIN)
//    	.build();
//		return productImage;
//    }
//	
//	@Transactional
//	public ProductImage uploadDetailImage(MultipartFile file, ProductVariant productVariant) {
//		
//		String folderPath = "products/" + productVariant.getCode() + "/detail";
//		String imagePath = fileStorage.uploadFile(file, folderPath);
//		
//		ProductImage productImage = ProductImage.builder()
//				.productVariant(productVariant)
//				.imagePath(imagePath)
//				.imageType(ImageType.DETAIL)
//				.build();
//		return productImage;
//	}
//	
//	@Transactional
//	public ProductImage uploadContentImage(MultipartFile file, ProductVariant productVariant) {
//		
//		String folderPath = "products/" + productVariant.getCode() + "/content";
//		String imagePath = fileStorage.uploadFile(file, folderPath);
//		
//		ProductImage productImage = ProductImage.builder()
//				.productVariant(productVariant)
//				.imagePath(imagePath)
//				.imageType(ImageType.CONTENT)
//				.build();
//		return productImage;
//	}
	
	@Transactional(readOnly = true)
	public ProductImage findProductImage(Long productImageId) {
		return productImageRepository.findById(obfuscator.deobfuscate(productImageId))
				.orElseThrow(()->new ProductVariantIntegrityException());
	}
}
