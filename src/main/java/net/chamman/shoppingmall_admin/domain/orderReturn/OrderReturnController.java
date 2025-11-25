package net.chamman.shoppingmall_admin.domain.orderReturn;
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
import net.chamman.shoppingmall_admin.domain.orderReturn.dto.OrderReturnResponseDto;
import net.chamman.shoppingmall_admin.domain.shipment.dto.ShipmentRequestDto;
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
    
    @Operation(summary = "반품 메모 수정")
    @SecurityRequirement(name = "X-Access-Token")
    @PatchMapping("/memo/{orderReturnId}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<ApiResponseDto<OrderReturnResponseDto>> modifyMemo(
    		@PathVariable Long orderReturnId,
    		@RequestBody String memo) { 
    	
    	log.debug("* 반품 메모 수정.");
    	OrderReturnResponseDto responseDto = orderReturnService.modifyMemo(orderReturnId, memo);
    	
    	return ResponseEntity.ok(apiResponseFactory.success(HttpStatusCode.UPDATE_SUCCESS, responseDto));
    }
    
    @Operation(summary = "반품 운송장 정보 입력")
    @SecurityRequirement(name = "X-Access-Token")
    @PatchMapping("/shipment/start/{orderReturnId}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<ApiResponseDto<OrderReturnResponseDto>> shipmentStartOrderReturn(
    		@PathVariable Long orderReturnId,
    		@Valid @RequestBody ShipmentRequestDto dto) { 
    	
    	log.debug("* 반품 운송장 정보 입력.");
    	OrderReturnResponseDto responseDto = orderReturnService.shipmentStartOrderReturn(orderReturnId, dto);
    	
    	return ResponseEntity.ok(apiResponseFactory.success(HttpStatusCode.UPDATE_SUCCESS, responseDto));
    }
    
    @Operation(summary = "반품 운송장 정보 수정")
    @SecurityRequirement(name = "X-Access-Token")
    @PatchMapping("/shipment/update/{orderReturnId}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<ApiResponseDto<OrderReturnResponseDto>> shipmentUpdateOrderReturn(
    		@PathVariable Long orderReturnId,
    		@Valid @RequestBody ShipmentRequestDto dto) { 
    	
    	log.debug("* 반품 운송장 정보 수정.");
    	OrderReturnResponseDto responseDto = orderReturnService.shipmentUpdateOrderReturn(orderReturnId, dto);
    	
    	return ResponseEntity.ok(apiResponseFactory.success(HttpStatusCode.UPDATE_SUCCESS, responseDto));
    }
    
    @Operation(summary = "수동 반품 입고 완료")
    @SecurityRequirement(name = "X-Access-Token")
    @PatchMapping("/arrived/{orderReturnId}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<ApiResponseDto<OrderReturnResponseDto>> arrivedOrderReturn(
    		@PathVariable Long orderReturnId) { 
    	
    	log.debug("* 수동 반품 입고 완료.");
    	OrderReturnResponseDto responseDto = orderReturnService.manualArrivedOrderReturn(orderReturnId);
    	
    	return ResponseEntity.ok(apiResponseFactory.success(HttpStatusCode.UPDATE_SUCCESS, responseDto));
    }
    
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
    
    @Operation(summary = "반품 반려")
    @SecurityRequirement(name = "X-Access-Token")
    @PatchMapping("/unable/{orderReturnId}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<ApiResponseDto<OrderReturnResponseDto>> unableOrderReturn(
    		@PathVariable Long orderReturnId) { 
    	
    	log.debug("* 반품 반려.");
    	OrderReturnResponseDto responseDto = orderReturnService.unableOrderReturn(orderReturnId);
    	
    	return ResponseEntity.ok(apiResponseFactory.success(HttpStatusCode.UPDATE_SUCCESS, responseDto));
    }
    
}