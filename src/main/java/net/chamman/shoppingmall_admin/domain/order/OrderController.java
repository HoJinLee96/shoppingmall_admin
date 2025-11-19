package net.chamman.shoppingmall_admin.domain.order;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
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
import net.chamman.shoppingmall_admin.domain.order.dto.OrderCancelReturnRequestDto;
import net.chamman.shoppingmall_admin.domain.order.dto.OrderDetailResponseDto;
import net.chamman.shoppingmall_admin.domain.order.dto.OrderListResponseDto;
import net.chamman.shoppingmall_admin.domain.order.dto.OrderSearchCondition;
import net.chamman.shoppingmall_admin.exception.HttpStatusCode;
import net.chamman.shoppingmall_admin.support.util.ApiResponseDto;
import net.chamman.shoppingmall_admin.support.util.ApiResponseFactory;

@Tag(name = "AdminOrderController", description = "관리자 주문 관리 API")
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/private/order")
public class OrderController {
	
	private final OrderService orderService;
    private final ApiResponseFactory apiResponseFactory;

    @Operation(summary = "주문 목록 조회", description = "모든 주문 목록을 페이지네이션하여 조회합니다.")
    @SecurityRequirement(name = "X-Access-Token")
    @GetMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<ApiResponseDto<Page<OrderListResponseDto>>> getOrderList(
    		@Valid @ModelAttribute OrderSearchCondition dto,
            @PageableDefault(size = 10, sort = "createdAt, desc") Pageable pageable) {
        
        log.debug("* 주문 목록 조회 요청. page: {}, size: {}", pageable.getPageNumber(), pageable.getPageSize());
        
        Page<OrderListResponseDto> orderPage = orderService.getOrderList(dto, pageable);
        
        HttpStatusCode statusCode = orderPage.isEmpty() ? HttpStatusCode.READ_SUCCESS_NO_DATA : HttpStatusCode.READ_SUCCESS;
        return ResponseEntity.ok(apiResponseFactory.success(statusCode, orderPage));
    }

    @Operation(summary = "특정 주문 상세 조회")
    @SecurityRequirement(name = "X-Access-Token")
    @GetMapping("/{orderId}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<ApiResponseDto<OrderDetailResponseDto>> getOrderDetail(
            @PathVariable Long orderId) { // 난독화된 ID
        
        log.debug("* 주문 상세 조회 요청. orderId: [{}]", orderId);
        OrderDetailResponseDto orderDetail = orderService.getOrderDetail(orderId);

        return ResponseEntity.ok(apiResponseFactory.success(HttpStatusCode.READ_SUCCESS, orderDetail));
    }

    @Operation(summary = "결제완료 -> 상품준비중 주문 상태 변경")
    @SecurityRequirement(name = "X-Access-Token")
    @PatchMapping("/accept/{orderId}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<ApiResponseDto<Void>> acceptOrder(
            @PathVariable Long orderId) { 

        log.debug("* 결제완료 -> 상품준비중 주문 상태 변경.");
        
        orderService.acceptOrder(orderId);

        return ResponseEntity.ok(apiResponseFactory.success(HttpStatusCode.UPDATE_SUCCESS));
    }
    
    @Operation(summary = "결제완료 -> 상품준비중 주문 상태 변경")
    @SecurityRequirement(name = "X-Access-Token")
    @PatchMapping("/accept")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<ApiResponseDto<Void>> acceptOrders(
    		@RequestBody List<Long> orderIds) { 
    	
    	log.debug("* 결제완료 -> 상품준비중 주문 상태 변경.");
    	orderService.acceptOrders(orderIds);
    	
    	return ResponseEntity.ok(apiResponseFactory.success(HttpStatusCode.UPDATE_SUCCESS));
    }
    
    @Operation(summary = "취소 요청 -> 환불 완료")
    @SecurityRequirement(name = "X-Access-Token")
    @PatchMapping("/cancel/complete/{orderId}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<ApiResponseDto<Void>> cancelOrder(
    		@PathVariable Long orderId) { 
    	
    	log.debug("* 취소 요청 -> 환불 완료");
    	orderService.cancelCompleteOrder(orderId);
    	
    	return ResponseEntity.ok(apiResponseFactory.success(HttpStatusCode.UPDATE_SUCCESS));
    }
	
	@Operation(summary = "취소 요청 -> 취소 반려 (이미출고)")
	@SecurityRequirement(name = "X-Access-Token")
	@PatchMapping("/cancel/return")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public ResponseEntity<ApiResponseDto<Void>> cancelReturnOrder(
			@Valid @RequestBody OrderCancelReturnRequestDto dto) { 
		
		log.debug("* 취소 요청 -> 취소 반려 (이미출고).");
		orderService.cancelReturnOrder(dto);
		
		return ResponseEntity.ok(apiResponseFactory.success(HttpStatusCode.UPDATE_SUCCESS));
	}
    
}