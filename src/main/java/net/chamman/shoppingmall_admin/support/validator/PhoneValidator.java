package net.chamman.shoppingmall_admin.support.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import net.chamman.shoppingmall_admin.support.annotation.ValidatePhone;

public class PhoneValidator implements ConstraintValidator<ValidatePhone, String> {
	
	private static final String PHONE_REGEX = "^\\d{3,4}-\\d{3,4}-\\d{4}$";
	
	@Override
	public void initialize(ValidatePhone constraintAnnotation) {
		// 초기화 로직 필요하면 여기에 (지금은 없음)
	}
	
	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		if (value == null || value.isBlank()) return false;
		return value.matches(PHONE_REGEX);
	}
}
