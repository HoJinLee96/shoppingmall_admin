package net.chamman.shoppingmall_admin.support.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import net.chamman.shoppingmall_admin.support.validator.EmailValidator;

@Documented
@Constraint(validatedBy = EmailValidator.class) 
@Target({ ElementType.FIELD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidateEmail {
	String message() default "validate.email.invalid";
	Class<?>[] groups() default {};
	Class<? extends Payload>[] payload() default {};
}