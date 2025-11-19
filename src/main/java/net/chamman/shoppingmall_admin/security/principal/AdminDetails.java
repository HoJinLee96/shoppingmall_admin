package net.chamman.shoppingmall_admin.security.principal;


import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import lombok.Getter;
import net.chamman.shoppingmall_admin.security.jwt.dto.AdminAccessTokenClaimsDto;

@Getter
public class AdminDetails implements UserDetails {
	
	private final long id;
	private final String userName; 
	private final String name; 
	private final List<GrantedAuthority> authorities; 
	
	public AdminDetails(AdminAccessTokenClaimsDto adminAccessTokenClaimsDto) {
		super();
		this.id = Long.parseLong(adminAccessTokenClaimsDto.getId());
		this.name = adminAccessTokenClaimsDto.getName();
		this.userName = adminAccessTokenClaimsDto.getUserName(); 
		List<String> roles = adminAccessTokenClaimsDto.getRoles();
		List<GrantedAuthority> authorities = roles.stream().map(SimpleGrantedAuthority::new)
				.collect(Collectors.toList());
		this.authorities = authorities;
	}
	
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return authorities;
	}
	
	@Override
	public String getPassword() {
		return null; 
	}
	
	@Override
	public String getUsername() {
		return userName; 
	}
	
	@Override
	public boolean isAccountNonExpired() {
		return true;
	}
	
	@Override
	public boolean isAccountNonLocked() {
		return true;
	}
	
	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}
	
	@Override
	public boolean isEnabled() {
		return true;
	}

}
