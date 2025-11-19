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
//	AUTHORIZATION_FAILED(401,"4030","message.common.authorization_failed"), // 권한 이상
//	METHOD_NOT_ALLOWED(405, "4050", "message.common.method_not_allowed"), // 요청 url 이상
	TOO_MANY_REQUEST( 429,"4290","message.common.too_many_request"), 
	INTERNAL_SERVER_ERROR( 500, "5000", "message.common.internal_server_error"),
	VERSION_MISMATCH( 409,"4091","message.common.version_mismatch"),
//	CLIENTIP_MISMATCH(409,"4091","message.common.version_mismatch"),
	
	// Admin 450
	ADMIN_NOT_FOUND( 404,"4530","message.admin.not_found"),
	ADMIN_PASSWORD_MISMATCH( 404,"4530","message.admin.password_mismatch"),
	ADMIN_EMAIL_DUPLICATION( 409,"4531","message.admin.email_duplication"),
	ADMIN_PHONE_DUPLICATION(409,"4532","message.admin.phone_duplication"),
	
	ADMIN_STATUS_STAY(403,"4533","message.admin.status_stay"),
	ADMIN_STATUS_STOP(401,"4534","message.admin.status_stop"),
	ADMIN_STATUS_DELETE(410,"4535","message.admin.status_delete"),
	ADMIN_STATUS_LOCKED(403,"4536","message.admin.status_locked"),
	ADMIN_STATUS_UNVERIFIED(403,"4537","message.admin.status_unverified"),
	
	// MEMBER 451
//	MEMBER_NOT_FOUND(404,"4530","message.member.not_found"),
//	MEMBER_PASSWORD_MISMATCH(404,"4530","message.member.password_mismatch"),
	
//	EMAIL_ALREADY_EXISTS(409,"4531","message.member.email_already_exists"),
//	PHONE_ALREADY_EXISTS(409,"4532","message.member.phone_already_exists"),
	
	MEMBER_STATUS_STAY(403,"4533","message.member.status_stay"),
	MEMBER_STATUS_STOP(401,"4534","message.member.status_stop"),
	MEMBER_STATUS_DELETE(410,"4535","message.member.status_delete"),
	MEMBER_STATUS_LOCKED(403,"4536","message.member.status_locked"),
	MEMBER_STATUS_UNVERIFIED(403,"4537","message.member.status_unverified"),
	
	// Product 452
//	PRODUCT_NOT_FOUND(404,"4520","message.product.not_found"),
//	SOLD_OUT(409,"4521","message.product.sold_out"),
	STOCK_NOT_ENOUGH(409,"4522","message.product.not_enough_stock"),
	STOCK_VALIDATION_FAILED( 404,"4523","message.product.invalid_stock_quantity"),
	
	
//	DELETED_EMAIL_EXISTS(200,"2072","message.user.deleted_email_exists"),
//	
//	OAUTH_NOT_FOUND(404,"4530","message.oauth.not_found"),
//	OAUTH_STATUS_STAY(403,"4533","message.oauth.status_stay"),
//	OAUTH_STATUS_STOP(401,"4534","message.oauth.status_stop"),
	
	// Address 454
//	ADDRESSS_NOT_FOUND(404,"4540","message.address.not_found"),
	ADDRESS_VALIDATION_FAILED(400, "4541", "message.address.invalid_address"), // 주소 형식 검증 부적합
	ADDRESS_INVALID(400, "4541", "message.address.invalid_address"), // Daum 조회 결과 없는 주소

	// Comment 455
//	COMMENT_NOT_FOUND(404,"4550","message.comment.not_found"),
//	COMMENT_STATUS_DELETE(400,"4551","message.comment.status_delete"),
	
	// Question 465
//	QUESTION_NOT_FOUND(404,"4650","message.question.not_found"),
//	QUESTION_STATUS_DELETE(404,"4651","message.question.status_delete"),
//	QUESTION_PASSWORD_MISMATCH(404,"4652","message.question.password_mismatch"),

	// Answer 466
//	ANSWER_NOT_FOUND(404,"4665","message.answer.not_found"),
//	ANSWER_STATUS_DELETE(404,"4661","message.answer.status_delete"),
	
	// Sign 452
	SIGNIN_FAILED( 404,"4520","message.sign.signin_failed"),
	SIGNIN_FAILED_OUT(404,"4521","message.sign.signin_failed_out"),
//	SIGNUP_EXIST_LOCAL(302,"4522","message.sign.signup_exist_local"),
//	SIGNUP_EXIST_OAUTH(302,"4523","message.sign.signup_exist_oauth"),
//	AUTHENTICATION_FAILED(401,"4523","message.sign.authentication_failed"),
	
	// JWT 457
	JWT_ILLEGAL(400,"4570","meesage.jwt.illegal"), // 토큰 값 부적합 또는 내부 claims 부적합
//	JWT_NOT_FOUND(404,"4571","meesage.jwt.not_found"), // 일치하는 토큰 조회 불가능
	JWT_EXPIRED(403,"4572","message.jwt.expired"), // 토큰 만료
//	JWT_PARSE_FIALED(401,"4573","message.jwt.validate_fail"), // 토큰 파싱 실패
//	JWT_VALIDATE_FIALED(401,"4574","message.jwt.validate_fail"), // 토큰 검증 실패
//	JWT_BLACKLIST_SIGNOUT(401,"4575","message.jwt.blacklist_signout"),
//	JWT_BLACKLIST_UPDATE(401,"4576","message.jwt.blacklist_update"),
//	JWT_CREATE_FIAL(500,"5001","message.jwt.create_fail"), // 토큰 생성 실패
	JWT_REFRESH_FIALURE(400,"4571","message.jwt.refresh_failure"), // 토큰 리프레쉬 실패
//	JWT_REDIS_GET_FIAL(500,"5003","message.common.internal_server_error"), // Redis 토큰 조회 실패
//	JWT_REDIS_SET_FIAL(500,"5004","message.common.internal_server_error"), // Redis 토큰 저장 실패

	
	// Token 458
	TOKEN_ILLEGAL(400,"4580","message.token.illegal"), // 토큰 값 null 또는 없음
//	TOKEN_NOT_FOUND(404,"4581","meesage.token.not_found"), // 일치하는 토큰 조회 불가능
//	TOKEN_EXPIRED(403,"4582","message.token.expired"), // 토큰 만료
//	TOKEN_PARSE_FIALED(401,"4573","message.token.validate_fail"), // 토큰 파싱 실패
//	TOKEN_VALIDATE_FIALED(401,"4574","message.token.validate_fail"), // 토큰 검증 실패
//	TOKEN_CREATE_FIAL(500,"5001","message.token.create_fail"), // 토큰 생성 실패
//	TOKEN_REDIS_GET_FIAL(500,"5003","message.common.internal_server_error"), // Redis 토큰 조회 실패
//	TOKEN_REDIS_SET_FIAL(500,"5004","message.common.internal_server_error"), // Redis 토큰 저장 실패

	// Verification 459
	VERIFICATION_CODE_MISMATCH(401,"4591","message.verification.mismatch_code"),
	VERIFICATION_ILLEGAL(401,"4592","message.verification.mismatch_recipient"),
//	VERIFICATION_EXPIRED(403,"4593","message.verification.expired"),
//	TOO_MANY_VERIFY(429,"4595","message.verification.too_many_verify"),
	
	// Crypto 460
//	ENCRYPT_FAILED(500,"5004","message.common.internal_server_error"),
//	DECRYPT_FAILED(500,"5005","message.common.internal_server_error"),
	
	// File 461
	FILE_ILLEGAL(400,"4610","message.file.illegal"),
//	FILE_UPLOAD_FAILED(500,"4610","message.file.upload_fail"),
//	FILE_DELETE_FAILED(500,"4611","message.file.delete_fail"),
	
	// Email 462
//	EMAIL_SEND_FAIL(500,"5006","message.mail.send_fail"),
	
	// Sms 463
//	SMS_SEND_FAIL(500,"5007","message.sms.send_fail"),
	
	// Map Road Search
//	ROAD_SEARCH_FAIL(500,"5008","message.map.road_search_fail"),
	
	// Transaction 464
//	ENCODING_FAIL(400, "4000", "message.common.illegal_input_value"),
//	DECODING_FAIL(400, "4000", "message.common.illegal_input_value")
	
	//ProductCategory 460
	PRODUCT_CATEGORY_NOT_FOUND( 404,"4601","message.product_category.not_found"),
	PRODUCT_CATEGORY_DELETE_FAILED( 400,"4602","message.product_category.delete_failed"),
	//Product 461
	PRODUCT_NOT_FOUND( 404,"4611","message.product.not_found"),
	//ProductVariant 462
	PRODUCT_VARIANT_ILLEGAL( 400,"4620","message.product_variant.illegal"),
	
	// Order 463
	// OrderItem 464
	ORDER_ITEM_ILLEGAL( 400,"4640","message.order_item.illegal")
	
	; 
	
	private final int status;
	private final String code;
	private final String messageKey;

}
