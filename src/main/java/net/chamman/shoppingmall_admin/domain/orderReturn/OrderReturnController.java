package net.chamman.shoppingmall_admin.domain.orderReturn;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.chamman.shoppingmall_admin.domain.orderReturn.dto.OrderReturnResponseDto;
import net.chamman.shoppingmall_admin.exception.HttpStatusCode;
import net.chamman.shoppingmall_admin.support.util.ApiResponseDto;
import net.chamman.shoppingmall_admin.support.util.ApiResponseFactory;

@Tag(name = "AdminOrderController", description = "관리자 주문 관리 API")
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/private/order-return")
public class OrderReturnController {
	
	private final OrderReturnService orderReturnService;
    private final ApiResponseFactory apiResponseFactory;
    
    @Operation(summary = "반품 요청 환불 승인")
    @SecurityRequirement(name = "X-Access-Token")
    @PatchMapping("/refund/{orderReturnId}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<ApiResponseDto<OrderReturnResponseDto>> refundOrderReturn(
    		@PathVariable Long orderReturnId) { 
    	
    	log.debug("* 반품 요청 환불.");
    	OrderReturnResponseDto responseDto = orderReturnService.refundOrderReturn(orderReturnId);
    	
    	return ResponseEntity.ok(apiResponseFactory.success(HttpStatusCode.UPDATE_SUCCESS, responseDto));
	}
    
}