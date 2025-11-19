package net.chamman.shoppingmall_admin.security.obfuscation;

public interface Obfuscator {

	public long obfuscate(Long id);
	
	public long deobfuscate(Long obfuscatedId);
}
