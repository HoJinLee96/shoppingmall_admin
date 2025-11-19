package net.chamman.shoppingmall_admin.security.obfuscation.impl;

import java.math.BigInteger;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import net.chamman.shoppingmall_admin.security.obfuscation.Obfuscator;

@Component
@PropertySource("classpath:application.properties")
public class ObfuscatorImpl implements Obfuscator {
	
	@Value("${obfuscator.salt}")
	private int salt;       // XOR 키
	
	private static final int MULTIPLIER = 8713;
	private static final int MOD = (1 << 31) - 1;
	private static final int INVERSE = BigInteger.valueOf(MULTIPLIER)
	        .modInverse(BigInteger.valueOf(MOD))
	        .intValue(); // ← 이걸로 진짜 값 구해

	
	@Override
	public long obfuscate(Long id) {
		long mixed = (id ^ salt) * MULTIPLIER % MOD;
		return mixed;
	}
	
	@Override
	public long deobfuscate(Long obfuscatedId) {
		long unmixed = (obfuscatedId * INVERSE) % MOD;
		return (unmixed ^ salt);
	}
	
	
}
