package net.chamman.shoppingmall_admin.security.jwt.dto;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import net.chamman.shoppingmall_admin.security.crypto.AesProvider;
import net.chamman.shoppingmall_admin.security.crypto.Cryptable;

@Getter
@ToString
@AllArgsConstructor
public class AdminAccessTokenClaimsDto implements Cryptable<AdminAccessTokenClaimsDto>{
	
	private final String id;
	private final String userName; 
	private final String name; 
	private final List<String> roles; 

	@Override
	public AdminAccessTokenClaimsDto encrypt(AesProvider aesProvider) {
		List<String> encryptedRoles = new ArrayList<>();
		for(String s : this.roles) {
			encryptedRoles.add(aesProvider.encrypt(s));
		}
		
		return new AdminAccessTokenClaimsDto(
				aesProvider.encrypt(this.id), 
				aesProvider.encrypt(this.userName),
				aesProvider.encrypt(this.name),
				encryptedRoles);
	}

	@Override
	public AdminAccessTokenClaimsDto decrypt(AesProvider aesProvider) {
		List<String> decryptedRoles = new ArrayList<>();
		for(String s : this.roles) {
			decryptedRoles.add(aesProvider.decrypt(s));
		}
		return new AdminAccessTokenClaimsDto(
				aesProvider.decrypt(this.id), 
				aesProvider.decrypt(this.userName),
				aesProvider.decrypt(this.name),
				decryptedRoles);
	}

}
