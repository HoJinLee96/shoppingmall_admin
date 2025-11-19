package net.chamman.shoppingmall_admin.support.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import net.chamman.shoppingmall_admin.support.annotation.ValidateUserName;

public class UserNameValidator implements ConstraintValidator<ValidateUserName, String> {
	
	private static final String USERNAME_REGEX = "^[A-Za-z0-9]{1,15}$";
	
	@Override
	public void initialize(ValidateUserName constraintAnnotation) {
		// 초기화 로직 필요하면 여기에 (지금은 없음)
	}
	
	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		if (value == null || value.isBlank()) return false;
		return value.matches(USERNAME_REGEX);
	}
}
