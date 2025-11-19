package net.chamman.shoppingmall_admin.domain.productVariant.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import lombok.Builder;
import net.chamman.shoppingmall_admin.domain.productImage.ProductImage;
import net.chamman.shoppingmall_admin.domain.productImage.dto.ProductImageResponseDto;
import net.chamman.shoppingmall_admin.domain.productVariant.ProductVariant;
import net.chamman.shoppingmall_admin.security.obfuscation.Obfuscator;

@Builder
public record ProductVariantResponseDto(
        Long productVariantId,
        String name,
        String code,
        ProductImageResponseDto mainImage,
        ProductVariantSaleInfoResponseDto saleInfo,
        ProductVariant.ProductVariantStatus status,
        List<ProductImageResponseDto> detailImages,
        List<ProductImageResponseDto> contentImages
) {
    public static ProductVariantResponseDto fromEntity(ProductVariant variant, Obfuscator obfuscator) {
    	List<ProductImage> images = variant.getImages();
    	ProductImage mainImage = null;
    	List<ProductImage> detailImages = new ArrayList<>();
    	List<ProductImage> contentImages = new ArrayList<>();
    	for(ProductImage image : images) {
    		switch (image.getImageType()) {
				case MAIN: {
					mainImage = image;
				}
				case DETAIL: {
					detailImages.add(image);
				}
				case CONTENT: {
					contentImages.add(image);
				}
    		}
    	}
    	
    	ProductVariantSaleInfoResponseDto saleInfo = ProductVariantSaleInfoResponseDto.fromEntity(variant);
    	
        return ProductVariantResponseDto.builder()
                .productVariantId(obfuscator.obfuscate(variant.getId()))
                .name(variant.getName())
                .code(variant.getCode())
                .mainImage(ProductImageResponseDto.fromEntity(mainImage, obfuscator))
                .saleInfo(saleInfo)
                .status(variant.getStatus())
                .detailImages(detailImages.stream()
                        .map(image -> ProductImageResponseDto.fromEntity(image, obfuscator))
                        .collect(Collectors.toList()))
                .contentImages(contentImages.stream()
                		.map(image -> ProductImageResponseDto.fromEntity(image, obfuscator))
                        .collect(Collectors.toList()))
                .build();
    }
}