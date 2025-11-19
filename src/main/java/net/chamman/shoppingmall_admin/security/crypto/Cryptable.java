package net.chamman.shoppingmall_admin.security.crypto;

public interface Cryptable<T extends Cryptable<T>> {
	T encrypt(AesProvider aesProvider);
	T decrypt(AesProvider aesProvider);
}
