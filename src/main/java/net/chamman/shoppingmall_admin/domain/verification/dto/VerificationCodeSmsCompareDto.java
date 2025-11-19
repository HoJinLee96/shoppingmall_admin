package net.chamman.shoppingmall_admin.domain.verification.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record VerificationCodeSmsCompareDto(
		
	    @NotBlank(message = "validation.user.phone.required")
	    @Pattern(regexp = "^0\\d{2,3}-\\d{3,4}-\\d{4}$", message = "validation.user.phone.invalid")
	    @Size(max = 20, message = "validation.user.phone.length")
	    String phone,
	    
	    @NotBlank(message = "validation.verification.code.required")
	    @Pattern(regexp = "^\\d{6}$", message = "validation.verification.code.invalid")
	    @Size(max = 6, message = "validation.verification.code.length")
	    String code
	    ) {

}
