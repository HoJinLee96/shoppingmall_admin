package net.chamman.shoppingmall_admin.domain.orderItem.dto;

import java.util.List;

import net.chamman.shoppingmall_admin.domain.orderReturn.dto.OrderReturnResponseDto;

public record OrderItemResponseDto(
		
		long orderItemId,
		List<OrderReturnResponseDto> orderReturnInfo
		
		
		) {

}
