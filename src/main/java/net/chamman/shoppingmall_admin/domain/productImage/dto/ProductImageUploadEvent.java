package net.chamman.shoppingmall_admin.domain.productImage.dto;

import java.io.IOException;

import org.springframework.web.multipart.MultipartFile;

import net.chamman.shoppingmall_admin.domain.productImage.ProductImage;

public record ProductImageUploadEvent(
		
		Long productImageId, //DB에 저장된 이미지 엔티티 ID
	    byte[] fileData,     //파일의 실제 바이트
	    String contentType,
	    String originalFileName,
	    String productCode,  //S3 경로 만들 때 필요
	    ProductImage.ImageType imageType //S3 경로 만들 때 필요
	    ) {
	public static ProductImageUploadEvent of(ProductImage image, MultipartFile file) throws IOException {
        return new ProductImageUploadEvent(
            image.getId(),
            file.getBytes(), //이때만 메모리에 올림
            file.getContentType(),
            file.getOriginalFilename(),
            image.getProductVariant().getCode(), //엔티티 참조
            image.getImageType() //엔티티 참조
        );
    }
}
