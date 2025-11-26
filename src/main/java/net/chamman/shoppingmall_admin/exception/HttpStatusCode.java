package net.chamman.shoppingmall_admin.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum HttpStatusCode {
	
	// Success
	SUCCESS(200,"2000","message.success"),
	SUCCESS_NO_DATA(204,"2040","message.success.no_data"),
	READ_SUCCESS(200,"2001","message.success.read"),
	READ_SUCCESS_NO_DATA(204,"2041","message.success.read_no_data"),
	CREATE_SUCCESS(201,"2010","message.success.create"),
	UPDATE_SUCCESS(200,"2002","message.success.update"),
	DELETE_SUCCESS(200,"2003","message.success.delete"),
	
	// Common
	ILLEGAL_REQUEST( 400, "4000", "message.common.illegal_request"), // 요청 이상
	VALIDATION_FAILED( 400, "4002", "message.common.request_body_not_valid"), // 검증 실패
	TOO_MANY_REQUEST( 429,"4290","message.common.too_many_request"), 
	INTERNAL_SERVER_ERROR( 500, "5000", "message.common.internal_server_error"),
	VERSION_MISMATCH( 409,"4091","message.common.version_mismatch"),
	
	// Verification 451
	VERIFICATION_ILLEGAL(400,"45100","message.verification.illegal"),
	VERIFICATION_CODE_MISMATCH(400,"45101","message.verification.code_mismatch"),
	
	// Admin 452
	ADMIN_ILLEGAL( 400,"45200","message.admin.illegal"),
	ADMIN_NOT_FOUND( 404,"45201","message.admin.not_found"),
	ADMIN_PASSWORD_MISMATCH( 404,"45202","message.admin.password_mismatch"),
	ADMIN_SIGNIN_FAILED( 404,"45203","message.sign.signin_failed"),
	ADMIN_SIGNIN_FAILED_OUT(404,"45204","message.sign.signin_failed_out"),
	ADMIN_EMAIL_DUPLICATION( 409,"45205","message.admin.email_duplication"),
	ADMIN_PHONE_DUPLICATION(409,"45206","message.admin.phone_duplication"),
	ADMIN_STATUS_STAY(403,"45207","message.admin.status_stay"),
	ADMIN_STATUS_STOP(401,"45208","message.admin.status_stop"),
	ADMIN_STATUS_DELETE(410,"45209","message.admin.status_delete"),
	ADMIN_STATUS_LOCKED(403,"45210","message.admin.status_locked"),
	ADMIN_STATUS_UNVERIFIED(403,"45211","message.admin.status_unverified"),
	
	// MEMBER 453
	MEMBER_ILLEGAL(400,"45300","message.member.illegal"),
	MEMBER_STATUS_STAY(403,"45301","message.member.status_stay"),
	MEMBER_STATUS_STOP(401,"45302","message.member.status_stop"),
	MEMBER_STATUS_DELETE(410,"45303","message.member.status_delete"),
	MEMBER_STATUS_LOCKED(403,"45304","message.member.status_locked"),
	MEMBER_STATUS_UNVERIFIED(403,"45305","message.member.status_unverified"),
	
	// Address 454
	ADDRESS_ILLEGAL(400, "45400", "message.address.illegal"),
	ADDRESS_INVALID(400, "45401", "message.address.invalid_address"), // Daum 조회 결과 없는 주소
	
	//ProductCategory 455
	PRODUCT_CATEGORY_ILLEGAL( 400,"45500","message.product_category.illegal"),
	PRODUCT_CATEGORY_DELETE_FAILED( 400,"45501","message.product_category.delete_failed"),
	
	//Product 456
	PRODUCT_ILLEGAL( 400,"45600","message.product.illegal"),
	
	//ProductVariant 457
	PRODUCT_VARIANT_ILLEGAL( 400,"45700","message.product_variant.illegal"),
	PRODUCT_VARIANT_STOCK_NOT_ENOUGH(409,"45701","message.product_variant.stock_not_enough"),
	
	//ProductImage 458
	PRODUCT_IMAGE_ILLEGAL( 400,"45800","message.product_image.illegal"),
	
	// Order 459
	ORDER_ILLEGAL( 400,"45900","message.order.illegal"),
	
	// OrderItem 460
	ORDER_ITEM_ILLEGAL( 400,"46000","message.order_item.illegal"),
	
	// OrderReturn 461
	ORDER_RETURN_ILLEGAL( 400,"46100","message.order_return.illegal"),
	
	// OrderExchange 462
	ORDER_EXCHANGE_ILLEGAL( 400,"46200","message.order_exchange.illegal"),
	
	// Payment 463
	PAYMENT_ILLEGAL( 400,"46300","message.payment.illegal"),	
	
	// OrderPayment 464
	ORDER_PAYMENT_ILLEGAL( 400,"46400","message.order_payment.illegal"),	
	
	// ReturnPayment 465
	RETURN_PAYMENT_ILLEGAL( 400,"46500","message.return_payment.illegal"),
	
	// ExchangePayment 466
	EXCHANGE_PAYMENT_ILLEGAL( 400,"46600","message.exchange_payment.illegal"),
	
	// Answer 467
	ANSWER_ILLEGAL(400,"46700","message.answer.illegal"),
	
	// Cartitem 468
	CARTITEM_ILLEGAL(400,"46800","message.cartitem.illegal"),
	
	// CouponPolicy 469
	COUPON_POLICY_ILLEGAL(400,"46900","message.coupon_policy.illegal"),
	
	// MemberCoupon 470
	MEMBER_COUPON_ILLEGAL(400,"47000","message.member_coupon.illegal"),
	
	// Notice 471
	NOTICE_ILLEGAL(400,"47100","message.notice.illegal"),

	// Review 472
	REVIEW_ILLEGAL(400,"47200","message.review.illegal"),

	// WishItem 473
	WISH_ITEM_ILLEGAL(400,"47300","message.wish_item.illegal"),

	// Question 474
	QUESTION_ILLEGAL(400,"47400","message.question.illegal"),
	QUESTION_PASSWORD_MISMATCH(400,"47401","message.question.password_mismatch"),
	
	// Shipment 475
	SHIPMENT_ILLEGAL(400,"47500","message.shipment.illegal"),

	
// SECURITY
	
	// JWT 600
	JWT_ILLEGAL(400,"60000","message.jwt.illegal"), // 토큰 값 부적합 또는 내부 claims 부적합
//	JWT_NOT_FOUND(404,"4571","meesage.jwt.not_found"), // 일치하는 토큰 조회 불가능
	JWT_EXPIRED(403,"60001","message.jwt.expired"), // 토큰 만료
//	JWT_PARSE_FIALED(401,"4573","message.jwt.validate_fail"), // 토큰 파싱 실패
//	JWT_VALIDATE_FIALED(401,"4574","message.jwt.validate_fail"), // 토큰 검증 실패
//	JWT_BLACKLIST_SIGNOUT(401,"4575","message.jwt.blacklist_signout"),
//	JWT_BLACKLIST_UPDATE(401,"4576","message.jwt.blacklist_update"),
//	JWT_CREATE_FIAL(500,"5001","message.jwt.create_fail"), // 토큰 생성 실패
	JWT_REFRESH_FIALURE(400,"60002","message.jwt.refresh_failure"), // 토큰 리프레쉬 실패
//	JWT_REDIS_GET_FIAL(500,"5003","message.common.internal_server_error"), // Redis 토큰 조회 실패
//	JWT_REDIS_SET_FIAL(500,"5004","message.common.internal_server_error"), // Redis 토큰 저장 실패

	// Token 601
	TOKEN_ILLEGAL(400,"60100","message.token.illegal"), // 토큰 값 null 또는 없음
//	TOKEN_NOT_FOUND(404,"4581","meesage.token.not_found"), // 일치하는 토큰 조회 불가능
//	TOKEN_EXPIRED(403,"4582","message.token.expired"), // 토큰 만료
//	TOKEN_PARSE_FIALED(401,"4573","message.token.validate_fail"), // 토큰 파싱 실패
//	TOKEN_VALIDATE_FIALED(401,"4574","message.token.validate_fail"), // 토큰 검증 실패
//	TOKEN_CREATE_FIAL(500,"5001","message.token.create_fail"), // 토큰 생성 실패
//	TOKEN_REDIS_GET_FIAL(500,"5003","message.common.internal_server_error"), // Redis 토큰 조회 실패
//	TOKEN_REDIS_SET_FIAL(500,"5004","message.common.internal_server_error"), // Redis 토큰 저장 실패

	
// INFRA
	
	// File 700
	FILE_ILLEGAL(400,"70000","message.file.illegal")
//	FILE_UPLOAD_FAILED(500,"4610","message.file.upload_fail"),
//	FILE_DELETE_FAILED(500,"4611","message.file.delete_fail"),
	
	// Email 462
//	EMAIL_SEND_FAIL(500,"5006","message.mail.send_fail"),
	
	// Sms 463
//	SMS_SEND_FAIL(500,"5007","message.sms.send_fail"),
	
	// Map Road Search
//	ROAD_SEARCH_FAIL(500,"5008","message.map.road_search_fail"),
	
	; 
	
	private final int status;
	private final String code;
	private final String messageKey;

}
