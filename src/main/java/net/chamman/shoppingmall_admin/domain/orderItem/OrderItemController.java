package net.chamman.shoppingmall_admin.domain.orderItem;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.chamman.shoppingmall_admin.domain.orderItem.dto.OrderItemShipmentRequestDto;
import net.chamman.shoppingmall_admin.domain.shipment.dto.ShipmentRequestDto;
import net.chamman.shoppingmall_admin.exception.HttpStatusCode;
import net.chamman.shoppingmall_admin.support.util.ApiResponseDto;
import net.chamman.shoppingmall_admin.support.util.ApiResponseFactory;

@Tag(name = "AdminOrderController", description = "관리자 주문 관리 API")
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/private/order-item")
public class OrderItemController {
	
	private final OrderItemService orderItemService;
    private final ApiResponseFactory apiResponseFactory;
    
    @Operation(summary = "상품준비중 -> 배송 시작 운송장 정보 입력")
	@SecurityRequirement(name = "X-Access-Token")
	@PatchMapping("/shipment/start/{orderItemId}")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public ResponseEntity<ApiResponseDto<Void>> updateOrderItemToShipmentStart(
			@PathVariable Long orderItemId,
			@Valid @RequestBody ShipmentRequestDto dto) {

		log.debug("* 관리자 운송장 등록 요청.");
		orderItemService.shipmentStartOrderItem(orderItemId, dto);

		return ResponseEntity.ok(apiResponseFactory.success(HttpStatusCode.UPDATE_SUCCESS));
	}

    @Operation(summary = "상품준비중 -> 배송 시작 운송장 정보 다량 입력")
	@SecurityRequirement(name = "X-Access-Token")
	@PatchMapping("/shipment/start")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public ResponseEntity<ApiResponseDto<Void>> updateOrderItemsToShipment(
			List<OrderItemShipmentRequestDto> dtoList) {

		log.debug("* 관리자 운송장 다량 등록 요청.");
		orderItemService.shipmentStartOrderItems(dtoList);

		return ResponseEntity.ok(apiResponseFactory.success(HttpStatusCode.UPDATE_SUCCESS));
	}
	
}