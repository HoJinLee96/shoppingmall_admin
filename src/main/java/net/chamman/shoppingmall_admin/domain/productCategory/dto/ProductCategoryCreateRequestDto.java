package net.chamman.shoppingmall_admin.domain.productCategory.dto;

public record ProductCategoryCreateRequestDto(
		Long parentProductCategoryId,
		String name
		) {

}
