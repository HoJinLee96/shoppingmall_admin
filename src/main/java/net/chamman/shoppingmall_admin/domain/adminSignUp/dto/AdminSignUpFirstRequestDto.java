package net.chamman.shoppingmall_admin.domain.adminSignUp.dto;

public record AdminSignUpFirstRequestDto(
		String userName,
		String password,
		String confirmPassword
		) {

}
