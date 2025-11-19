package net.chamman.shoppingmall_admin.domain.adminSignIn.dto;

public record AdminSignInJwtDto(
		
		String accessToken,
		String refreshToken
		
		) {

}
