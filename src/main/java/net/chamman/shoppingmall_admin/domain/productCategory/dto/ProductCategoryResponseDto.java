package net.chamman.shoppingmall_admin.domain.productCategory.dto;

import java.util.ArrayList;
import java.util.List;

import lombok.Builder;
import net.chamman.shoppingmall_admin.domain.productCategory.ProductCategory;
import net.chamman.shoppingmall_admin.security.obfuscation.Obfuscator;

@Builder
public record ProductCategoryResponseDto(
		
        Long productCategoryId,
        String name,
        List<ProductCategoryResponseDto> children
		
		) {

    public static ProductCategoryResponseDto fromEntity(ProductCategory productCategory, Obfuscator obfuscator) {
        return ProductCategoryResponseDto.builder()
                .productCategoryId(obfuscator.obfuscate(productCategory.getId()))
                .name(productCategory.getName())
                .children(new ArrayList<>())
                .build();
    }
    
    public void addChildren(ProductCategoryResponseDto childDto) {
    	this.children.add(childDto);
    }
}
