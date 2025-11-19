package net.chamman.shoppingmall_admin.support.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import net.chamman.shoppingmall_admin.support.annotation.ValidateEmail;

public class EmailValidator implements ConstraintValidator<ValidateEmail, String> {
	
	private static final String EMAIL_REGEX = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$";
	
	@Override
	public void initialize(ValidateEmail constraintAnnotation) {
		// 초기화 로직 필요하면 여기에 (지금은 없음)
	}
	
	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		if (value == null || value.isBlank()) return false;
		return value.matches(EMAIL_REGEX);
	}
}
