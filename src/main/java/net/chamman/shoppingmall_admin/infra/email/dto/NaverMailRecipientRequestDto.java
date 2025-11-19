package net.chamman.shoppingmall_admin.infra.email.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class NaverMailRecipientRequestDto {
	
	String address;
	String name;
	String type;
	
}
